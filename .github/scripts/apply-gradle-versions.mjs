#!/usr/bin/env node
// Turn an osv-scanner report over gradle/verification-metadata.xml into version bumps
// in gradle/libs.versions.toml.
//
// The Maven counterpart to apply-npm-resolutions.mjs, but the fixable surface is much
// smaller and the script is honest about that. A Maven advisory can only be fixed here
// when the vulnerable coordinate is a *direct* entry of the version catalog: then the
// `[versions]` line behind its `version.ref` is bumped. Everything else - a transitive
// dependency, or a plugin's own implementation artifact, neither of which the catalog
// names - is reported for a human instead. Forcing a transitive Maven version means
// injecting a resolutionStrategy or a constraint into a Kotlin Multiplatform build,
// which is a change to how the build resolves rather than a version pin, and is not
// something to do unattended.
//
// Usage:
//   node .github/scripts/apply-gradle-versions.mjs \
//     --scan <osv.json> --catalog gradle/libs.versions.toml \
//     [--json <summary.json>] [--md <pr-body.md>] [--issue <issue.md>] [--dry-run]
//
// Exits 0 whenever it ran correctly - "nothing to fix" is a success, not a failure.
// Exits 1 only on a genuine error (bad input, unparseable catalog).

import { readFileSync, writeFileSync, appendFileSync } from "node:fs";

import {
  SEVERITIES,
  THRESHOLD,
  compareVersions,
  fail,
  fixedVersionsFor,
  joinLines,
  lineOf,
  parseVersion,
  readLines,
  sameLine,
  severityOf,
  table,
} from "./lib/osv-common.mjs";

// Maven coordinates are not semver: "1.11" and "2.0.0-alpha.6" are both legal.
const MAVEN = { pad: true };

// ---------------------------------------------------------------- arguments

function parseArgs(argv) {
  const out = { dryRun: false };
  for (let i = 0; i < argv.length; i++) {
    const arg = argv[i];
    if (arg === "--dry-run") out.dryRun = true;
    else if (arg === "--scan") out.scan = argv[++i];
    else if (arg === "--catalog") out.catalog = argv[++i];
    else if (arg === "--json") out.json = argv[++i];
    else if (arg === "--md") out.md = argv[++i];
    else if (arg === "--issue") out.issue = argv[++i];
    else fail(`unknown argument: ${arg}`);
  }
  if (!out.scan) fail("--scan is required");
  if (!out.catalog) fail("--catalog is required");
  return out;
}

// ------------------------------------------------------------- catalog parsing

// A deliberately small TOML reader: it understands exactly the three table headers and
// the entry shapes this catalog uses, and refuses anything else rather than guessing.
// Bringing in a real TOML parser would mean a node_modules install in a workflow whose
// whole point is supply-chain hygiene.
function parseCatalog(lines) {
  const versions = new Map(); // ref -> { line, value }
  const modules = new Map(); // "group:artifact" -> { ref } | { line, value }
  let section = null;

  const SECTION_RE = /^\s*\[([^\]]+)\]\s*$/;
  const ENTRY_RE = /^\s*([A-Za-z0-9_.-]+)\s*=\s*(.+?)\s*$/;

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    if (!line.trim() || line.trim().startsWith("#")) continue;

    const sectionMatch = SECTION_RE.exec(line);
    if (sectionMatch) {
      section = sectionMatch[1].trim();
      continue;
    }

    const entry = ENTRY_RE.exec(line);
    if (!entry) continue;
    const [, key, rawValue] = entry;

    if (section === "versions") {
      const value = /^"([^"]*)"$/.exec(rawValue);
      if (value) versions.set(key, { line: i, value: value[1] });
      continue;
    }

    if (section !== "libraries" && section !== "plugins") continue;

    // `version.ref = "x"` is the only form that can be bumped centrally. An inline
    // `version = "x"` is bumped on the entry's own line instead.
    const ref = /version\.ref\s*=\s*"([^"]+)"/.exec(rawValue);
    const inline = /(?:^|[,{]\s*)version\s*=\s*"([^"]+)"/.exec(rawValue);

    let coordinate = null;
    if (section === "libraries") {
      const module = /module\s*=\s*"([^":]+:[^"]+)"/.exec(rawValue);
      const group = /group\s*=\s*"([^"]+)"/.exec(rawValue);
      const name = /name\s*=\s*"([^"]+)"/.exec(rawValue);
      if (module) coordinate = module[1];
      else if (group && name) coordinate = `${group[1]}:${name[1]}`;
    } else {
      const id = /id\s*=\s*"([^"]+)"/.exec(rawValue);
      // How Gradle resolves a plugin id: the marker artifact carries the version, and
      // it is the marker that shows up in verification-metadata.xml. An advisory filed
      // against the plugin's *implementation* artifact will not match this and lands in
      // the needs-a-human table, which is the honest outcome.
      if (id) coordinate = `${id[1]}:${id[1]}.gradle.plugin`;
    }

    if (!coordinate) continue;
    if (ref) modules.set(coordinate, { ref: ref[1] });
    else if (inline) modules.set(coordinate, { line: i, value: inline[1] });
  }

  return { versions, modules };
}

// Resolve a coordinate to the thing that would have to change, or null when the
// catalog does not name it (transitive, or a plugin implementation artifact).
function targetFor(catalog, coordinate) {
  const entry = catalog.modules.get(coordinate);
  if (!entry) return null;
  if (entry.ref === undefined) return { kind: "inline", ...entry };
  const version = catalog.versions.get(entry.ref);
  if (!version) return null;
  return { kind: "ref", ref: entry.ref, line: version.line, value: version.value };
}

// A `[versions]` ref can back several catalog entries (the `kotlin` ref backs both
// Kotlin plugins). Bumping it moves all of them, which is correct for a release train
// but has to be spelled out in the PR body.
function sharedBy(catalog, ref) {
  const users = [];
  for (const [coordinate, entry] of catalog.modules) {
    if (entry.ref === ref) users.push(coordinate);
  }
  return users.sort();
}

// ------------------------------------------------------------------ scanning

function collectFindings(report) {
  const byPackage = new Map();

  for (const result of report?.results ?? []) {
    for (const entry of result?.packages ?? []) {
      const pkg = entry?.package;
      if (!pkg?.name || !String(pkg.ecosystem ?? "").startsWith("Maven")) continue;
      const installed = parseVersion(pkg.version, MAVEN);
      if (!installed) continue;

      for (const vulnerability of entry?.vulnerabilities ?? []) {
        const severity = severityOf(vulnerability);
        const id = vulnerability?.id ?? "unknown";

        let bucket = byPackage.get(pkg.name);
        if (!bucket) {
          bucket = { name: pkg.name, installed, advisories: [] };
          byPackage.set(pkg.name, bucket);
        }

        if (severity === null) {
          bucket.advisories.push({ id, severity: "UNKNOWN", skipped: "advisory carries no severity rating" });
          continue;
        }
        if (SEVERITIES.indexOf(severity) < THRESHOLD) continue;

        const fixes = fixedVersionsFor(vulnerability, pkg.name, "Maven", MAVEN);
        const newer = fixes.filter((fix) => compareVersions(fix, installed) > 0);
        const candidates = newer
          // Never jump onto a prerelease unless the installed version already is one.
          .filter((c) => c.prerelease === null || installed.prerelease !== null)
          .filter((candidate) => sameLine(installed, candidate))
          .sort(compareVersions);

        if (candidates.length === 0) {
          const available = fixes.map((fix) => fix.raw).join(", ");
          let reason;
          if (fixes.length === 0) reason = "advisory publishes no fixed version";
          else if (newer.length === 0) reason = `already at or above every published fix (${available})`;
          else reason = `no fix within the ${lineOf(installed)} line (available: ${available})`;
          bucket.advisories.push({ id, severity, skipped: reason });
          continue;
        }

        // Lowest fix that clears this advisory, mirroring Renovate's
        // `vulnerabilityFixStrategy: "lowest"`.
        bucket.advisories.push({ id, severity, target: candidates[0] });
      }
    }
  }

  return byPackage;
}

// ----------------------------------------------------------------- reporting

function renderBody(updates, skipped) {
  const out = ["## Automated Gradle security fixes", ""];

  if (updates.length > 0) {
    out.push(
      `Bumped ${updates.length} entr${updates.length === 1 ? "y" : "ies"} in ` +
        "`gradle/libs.versions.toml`.",
      "",
      table(
        ["Coordinate", "Catalog key", "Installed", "Bumped to", "Severity", "Advisories"],
        updates.map((u) => [
          `\`${u.name}\``,
          `\`${u.key}\``,
          u.installed,
          `**${u.target}**`,
          u.severity,
          u.ids.join(", "),
        ]),
      ),
      "",
    );
    const shared = updates.filter((u) => u.alsoMoves.length > 0);
    if (shared.length > 0) {
      out.push(
        "Some of these bumps move a shared version ref, so they also move:",
        "",
        ...shared.map((u) => `- \`${u.key}\` -> ${u.alsoMoves.map((m) => `\`${m}\``).join(", ")}`),
        "",
      );
    }
  } else {
    out.push("No HIGH or CRITICAL Maven advisory maps to a version-catalog entry.", "");
  }

  if (skipped.length > 0) {
    out.push("### Not applied - needs a human", "", renderSkipped(skipped), "");
  }

  out.push("---", "", "Opened by `.github/workflows/security-gradle.yml`.");
  return out.join("\n");
}

function renderSkipped(skipped) {
  return table(
    ["Coordinate", "Installed", "Severity", "Advisory", "Reason"],
    skipped.map((s) => [`\`${s.name}\``, s.installed, s.severity, s.id, s.reason]),
  );
}

// The tracking issue exists because an unfixable Maven finding has nowhere else to go:
// there is no PR to carry it (nothing changed), and a job summary is read by nobody.
function renderIssue(skipped) {
  return [
    "The weekly Gradle scan found HIGH or CRITICAL advisories on the build classpath",
    "that cannot be fixed by bumping `gradle/libs.versions.toml`, because the affected",
    "coordinate is not a direct catalog entry - it is pulled in transitively, or it is a",
    "plugin's implementation artifact.",
    "",
    "Fixing one of these means either waiting for the direct dependency to update, or",
    "adding a dependency constraint by hand.",
    "",
    renderSkipped(skipped),
    "",
    "---",
    "",
    "Updated by `.github/workflows/security-gradle.yml`. Closed automatically once the",
    "findings are gone.",
  ].join("\n");
}

// ---------------------------------------------------------------------- main

const args = parseArgs(process.argv.slice(2));

let report;
try {
  report = JSON.parse(readFileSync(args.scan, "utf8"));
} catch (error) {
  fail(`could not read scan report ${args.scan}: ${error.message}`);
}

const file = readLines(readFileSync(args.catalog, "utf8"));
const catalog = parseCatalog(file.lines);
if (catalog.versions.size === 0 && catalog.modules.size === 0) {
  fail(`no [versions]/[libraries] entries found in ${args.catalog}`);
}

const findings = collectFindings(report);
const updates = [];
const skipped = [];
const buckets = [...findings.values()].sort((a, b) => (a.name < b.name ? -1 : 1));

for (const bucket of buckets) {
  for (const advisory of bucket.advisories) {
    if (advisory.target) continue;
    skipped.push({
      name: bucket.name,
      installed: bucket.installed.raw,
      severity: advisory.severity,
      id: advisory.id,
      reason: advisory.skipped,
    });
  }

  const fixable = bucket.advisories.filter((advisory) => advisory.target);
  if (fixable.length === 0) continue;

  const ids = fixable.map((advisory) => advisory.id);
  const severity = fixable.some((a) => a.severity === "CRITICAL") ? "CRITICAL" : "HIGH";

  const slot = targetFor(catalog, bucket.name);
  if (!slot) {
    skipped.push({
      name: bucket.name,
      installed: bucket.installed.raw,
      severity,
      id: ids.join(", "),
      reason: "not a direct version-catalog entry (transitive or plugin implementation)",
    });
    continue;
  }

  // Highest across advisories - one version has to clear all of them at once.
  const targets = fixable.map((advisory) => advisory.target).sort(compareVersions);
  const target = targets[targets.length - 1];

  const current = parseVersion(slot.value, MAVEN);
  if (current && compareVersions(current, target) >= 0) {
    skipped.push({
      name: bucket.name,
      installed: bucket.installed.raw,
      severity,
      id: ids.join(", "),
      reason: `catalog already declares ${slot.value}; the resolved version comes from elsewhere`,
    });
    continue;
  }
  // A catalog version that is a range or a rich version ("[1.0, 2.0)", "1.2+") is not
  // something to rewrite blindly.
  if (!current) {
    skipped.push({
      name: bucket.name,
      installed: bucket.installed.raw,
      severity,
      id: ids.join(", "),
      reason: `catalog version \`${slot.value}\` is not a plain version`,
    });
    continue;
  }

  updates.push({
    name: bucket.name,
    key: slot.kind === "ref" ? slot.ref : bucket.name,
    line: slot.line,
    from: slot.value,
    installed: bucket.installed.raw,
    target: target.raw,
    severity,
    ids,
    alsoMoves: slot.kind === "ref" ? sharedBy(catalog, slot.ref).filter((m) => m !== bucket.name) : [],
  });
}

if (updates.length > 0 && !args.dryRun) {
  for (const update of updates) {
    const line = file.lines[update.line];
    // Replace only the quoted version on that line, so comments and spacing survive.
    const rewritten = line.replace(`"${update.from}"`, `"${update.target}"`);
    if (rewritten === line) {
      fail(`could not rewrite ${update.from} -> ${update.target} on line ${update.line + 1}`);
    }
    file.lines[update.line] = rewritten;
  }
  writeFileSync(args.catalog, joinLines(file));
}

const summary = {
  changed: updates.length > 0,
  count: updates.length,
  unfixable: skipped.length,
  dryRun: args.dryRun,
  updates,
  skipped,
};

if (args.json) writeFileSync(args.json, JSON.stringify(summary, null, 2));
if (args.md) writeFileSync(args.md, renderBody(updates, skipped));
if (args.issue && skipped.length > 0) writeFileSync(args.issue, renderIssue(skipped));

if (process.env.GITHUB_OUTPUT) {
  const lines =
    `changed=${summary.changed}\ncount=${summary.count}\nunfixable=${summary.unfixable}\n`;
  appendFileSync(process.env.GITHUB_OUTPUT, lines);
}

for (const update of updates) {
  console.log(
    `${update.name}: ${update.from} -> ${update.target} via ${update.key} ` +
      `[${update.severity}] ${update.ids.join(", ")}`,
  );
}
for (const entry of skipped) {
  console.log(`skipped ${entry.name}@${entry.installed} (${entry.id}): ${entry.reason}`);
}
console.log(`${updates.length} catalog entr(y/ies) to bump, ${skipped.length} skipped.`);
