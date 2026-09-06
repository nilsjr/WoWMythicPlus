#!/usr/bin/env node
// Turn an osv-scanner report over .kotlin-js-store/yarn.lock into yarn `resolution(...)`
// pins in web/build.gradle.kts.
//
// The lockfile has no package.json, so neither Dependabot nor Renovate can patch it.
// The only lever is the YarnRootExtension resolution block, and Renovate's customManager
// can only refresh lines that already exist - it can never add one for a package that
// has never been pinned. This script closes that gap.
//
// Usage:
//   node .github/scripts/apply-npm-resolutions.mjs \
//     --scan <osv.json> --gradle web/build.gradle.kts \
//     [--json <summary.json>] [--md <pr-body.md>] [--dry-run]
//
// Exits 0 whenever it ran correctly - "nothing to fix" is a success, not a failure.
// Exits 1 only on a genuine error (bad input, resolution block not found).

import { readFileSync, writeFileSync, appendFileSync } from "node:fs";

const SEVERITIES = ["LOW", "MODERATE", "HIGH", "CRITICAL"];
const THRESHOLD = SEVERITIES.indexOf("HIGH");

// ---------------------------------------------------------------- arguments

function fail(message) {
  console.error(`::error::${message}`);
  process.exit(1);
}

function parseArgs(argv) {
  const out = { dryRun: false };
  for (let i = 0; i < argv.length; i++) {
    const arg = argv[i];
    if (arg === "--dry-run") out.dryRun = true;
    else if (arg === "--scan") out.scan = argv[++i];
    else if (arg === "--gradle") out.gradle = argv[++i];
    else if (arg === "--json") out.json = argv[++i];
    else if (arg === "--md") out.md = argv[++i];
    else fail(`unknown argument: ${arg}`);
  }
  if (!out.scan) fail("--scan is required");
  if (!out.gradle) fail("--gradle is required");
  return out;
}

// ------------------------------------------------------------------- semver

// Deliberately minimal: every version handled here comes from a lockfile or from an
// OSV `fixed` event, so it is always a concrete release, never a range.
function parseVersion(raw) {
  const pattern = /^v?(\d+)\.(\d+)\.(\d+)(?:-([0-9A-Za-z.-]+))?(?:\+[0-9A-Za-z.-]+)?$/;
  const trimmed = String(raw).trim();
  const match = pattern.exec(trimmed);
  if (!match) return null;
  return {
    major: Number(match[1]),
    minor: Number(match[2]),
    patch: Number(match[3]),
    prerelease: match[4] ?? null,
    // `raw` is written straight into resolution("<pkg>", "<raw>"), so drop any leading
    // "v". OSV does not forbid a `fixed: "v1.2.3"` event, and yarn would reject it.
    raw: trimmed.replace(/^v/, ""),
  };
}

function compareVersions(a, b) {
  if (a.major !== b.major) return a.major - b.major;
  if (a.minor !== b.minor) return a.minor - b.minor;
  if (a.patch !== b.patch) return a.patch - b.patch;
  // A release outranks any prerelease of the same X.Y.Z.
  if (a.prerelease === null && b.prerelease === null) return 0;
  if (a.prerelease === null) return 1;
  if (b.prerelease === null) return -1;
  // Simplification: identifiers compare as plain strings rather than per semver
  // section 11. Harmless here - prerelease candidates are rejected outright below.
  if (a.prerelease < b.prerelease) return -1;
  return a.prerelease > b.prerelease ? 1 : 0;
}

// `sameMajor` is not enough: under semver a 0.x minor bump is a breaking change, and
// below 0.1.0 even a patch bump is, so 0.1.4 -> 0.2.0 and 0.0.3 -> 0.0.4 must never be
// applied automatically. A 0.0.x package therefore always lands in the skipped table
// for a human to decide - deliberate, since nothing can be assumed compatible there.
// How `sameLine` describes the range it will stay inside, for the skip reasons.
function lineOf(version) {
  if (version.major > 0) return `${version.major}.x`;
  if (version.minor > 0) return `0.${version.minor}.x`;
  return `0.0.${version.patch}`;
}

function sameLine(current, candidate) {
  if (current.major !== candidate.major) return false;
  if (current.major === 0 && current.minor !== candidate.minor) return false;
  if (current.major === 0 && current.minor === 0) return false;
  return true;
}

// ------------------------------------------------------------------ scanning

function severityOf(vulnerability) {
  const explicit = vulnerability?.database_specific?.severity;
  if (typeof explicit === "string" && SEVERITIES.includes(explicit.toUpperCase())) {
    return explicit.toUpperCase();
  }
  // Fallback only. `groups[].max_severity` is a CVSS *score* string ("8.7") and is
  // empty for advisories without a CVSS vector, so it is never the primary signal:
  // brace-expansion GHSA-3jxr scores 5.3 on CVSS yet GitHub rates it HIGH.
  for (const affected of vulnerability?.affected ?? []) {
    const nested = affected?.database_specific?.severity;
    if (typeof nested === "string" && SEVERITIES.includes(nested.toUpperCase())) {
      return nested.toUpperCase();
    }
  }
  return null;
}

// Advisories carry one `affected` entry per maintained major line, so every entry has
// to be inspected - taking the first `fixed` event would pick an arbitrary major.
function fixedVersionsFor(vulnerability, packageName) {
  const versions = [];
  for (const affected of vulnerability?.affected ?? []) {
    const pkg = affected?.package;
    if (!pkg || pkg.name !== packageName) continue;
    if (!String(pkg.ecosystem ?? "").startsWith("npm")) continue;
    for (const range of affected?.ranges ?? []) {
      if (range?.type === "GIT") continue;
      for (const event of range?.events ?? []) {
        if (!event?.fixed) continue;
        const parsed = parseVersion(event.fixed);
        if (parsed) versions.push(parsed);
      }
    }
  }
  return versions;
}

function record(byPackage, name, installed, advisory) {
  let bucket = byPackage.get(name);
  if (!bucket) {
    bucket = { name, advisories: [] };
    byPackage.set(name, bucket);
  }
  // The installed version belongs to the advisory, not the bucket: yarn can hoist the
  // same package at two different versions, which osv-scanner reports as two separate
  // entries. Keeping one per bucket would label the second one with the first's version.
  bucket.advisories.push({ ...advisory, installed });
}

function collectFindings(report) {
  const byPackage = new Map();

  for (const result of report?.results ?? []) {
    for (const entry of result?.packages ?? []) {
      const pkg = entry?.package;
      if (!pkg?.name || !String(pkg.ecosystem ?? "").startsWith("npm")) continue;
      // The scan runs against the lockfile, so this is the resolved version - no
      // yarn.lock parsing is needed anywhere in this script.
      const installed = parseVersion(pkg.version);
      if (!installed) continue;

      for (const vulnerability of entry?.vulnerabilities ?? []) {
        const severity = severityOf(vulnerability);
        const id = vulnerability?.id ?? "unknown";

        if (severity === null) {
          record(byPackage, pkg.name, installed, {
            id,
            severity: "UNKNOWN",
            skipped: "advisory carries no severity rating",
          });
          continue;
        }
        if (SEVERITIES.indexOf(severity) < THRESHOLD) continue;

        const fixes = fixedVersionsFor(vulnerability, pkg.name);
        const newer = fixes.filter((fix) => compareVersions(fix, installed) > 0);
        const candidates = newer
          // Never jump onto a prerelease unless the installed version already is one.
          .filter((c) => c.prerelease === null || installed.prerelease !== null)
          .filter((candidate) => sameLine(installed, candidate))
          .sort(compareVersions);

        if (candidates.length === 0) {
          const available = fixes.map((fix) => fix.raw).join(", ");
          let reason;
          if (fixes.length === 0) {
            reason = "advisory publishes no fixed version";
          } else if (newer.length === 0) {
            // Nothing to do here - worth surfacing rather than hiding, since it means
            // the scanner and the lockfile disagree about what is actually installed.
            reason = `already at or above every published fix (${available})`;
          } else {
            reason = `no fix within the ${lineOf(installed)} line (available: ${available})`;
          }
          record(byPackage, pkg.name, installed, { id, severity, skipped: reason });
          continue;
        }

        // Lowest fix that clears this advisory, mirroring Renovate's
        // `vulnerabilityFixStrategy: "lowest"`.
        record(byPackage, pkg.name, installed, { id, severity, target: candidates[0] });
      }
    }
  }

  return byPackage;
}

// ------------------------------------------------------------- gradle file io

function readGradle(path) {
  const text = readFileSync(path, "utf8");
  // The working tree is CRLF (core.autocrlf=true) while git blobs are LF. Getting
  // this wrong rewrites every line of the file instead of the handful that changed.
  const eol = text.includes("\r\n") ? "\r\n" : "\n";
  const trailingNewline = /\r?\n$/.test(text);
  const lines = text.split(/\r?\n/);
  // A trailing newline leaves an empty final element behind; drop it here and add the
  // newline back on join, so the flag is the single source of truth either way.
  if (trailingNewline) lines.pop();
  return { lines, eol, trailingNewline };
}

const RESOLUTION_RE = /^(\s*)resolution\("([^"]+)",\s*"([^"]+)"\)\s*$/;

function locateResolutions(lines) {
  let first = -1;
  let last = -1;
  const entries = new Map();
  let indent = "    ";

  for (let i = 0; i < lines.length; i++) {
    const match = RESOLUTION_RE.exec(lines[i]);
    if (!match) continue;
    if (first === -1) {
      first = i;
      // Read the indent from the file rather than hard-coding it.
      indent = match[1];
    }
    last = i;
    entries.set(match[2], match[3]);
  }

  if (first === -1) return null;
  // Anything other than a resolution line inside the span would be silently dropped
  // by the rebuild below, so refuse rather than guess.
  for (let i = first; i <= last; i++) {
    if (!RESOLUTION_RE.test(lines[i])) return null;
  }
  return { first, last, entries, indent };
}

// ----------------------------------------------------------------- reporting

function table(header, rows) {
  return [
    `| ${header.join(" | ")} |`,
    `|${header.map(() => "---").join("|")}|`,
    ...rows.map((row) => `| ${row.join(" | ")} |`),
  ].join("\n");
}

function renderBody(updates, skipped) {
  const out = ["## Automated npm security fixes", ""];

  if (updates.length > 0) {
    out.push(
      `Pinned ${updates.length} package${updates.length === 1 ? "" : "s"} in the ` +
        "`YarnRootExtension` resolution block of `web/build.gradle.kts` and regenerated " +
        "`.kotlin-js-store/yarn.lock`.",
      "",
      table(
        ["Package", "Installed", "Pinned to", "Severity", "Advisories"],
        updates.map((u) => [
          `\`${u.name}\``,
          u.installed,
          `**${u.target}**`,
          u.severity,
          u.ids.join(", "),
        ]),
      ),
      "",
    );
  } else {
    out.push("No HIGH or CRITICAL npm advisory needs a new pin.", "");
  }

  if (skipped.length > 0) {
    out.push(
      "### Not applied - needs a human",
      "",
      // The only place a cross-major fix ever becomes visible: it can never show up
      // as a change, so without this table it would silently vanish every week.
      table(
        ["Package", "Installed", "Severity", "Advisory", "Reason"],
        skipped.map((s) => [`\`${s.name}\``, s.installed, s.severity, s.id, s.reason]),
      ),
      "",
    );
  }

  out.push("---", "", "Opened by `.github/workflows/security-yarn-lock.yml`.");
  return out.join("\n");
}

// ---------------------------------------------------------------------- main

const args = parseArgs(process.argv.slice(2));

let report;
try {
  report = JSON.parse(readFileSync(args.scan, "utf8"));
} catch (error) {
  fail(`could not read scan report ${args.scan}: ${error.message}`);
}

const gradle = readGradle(args.gradle);
const block = locateResolutions(gradle.lines);
if (!block) {
  fail(`no contiguous resolution(...) block found in ${args.gradle}`);
}

const findings = collectFindings(report);
const updates = [];
const skipped = [];

const buckets = [...findings.values()].sort((a, b) => (a.name < b.name ? -1 : 1));

// Distinct installed versions, in the order first seen - usually exactly one.
function installedOf(advisories) {
  return [...new Set(advisories.map((advisory) => advisory.installed.raw))].join(", ");
}

for (const bucket of buckets) {
  for (const advisory of bucket.advisories) {
    if (advisory.target) continue;
    skipped.push({
      name: bucket.name,
      installed: advisory.installed.raw,
      severity: advisory.severity,
      id: advisory.id,
      reason: advisory.skipped,
    });
  }

  const fixable = bucket.advisories.filter((advisory) => advisory.target);
  if (fixable.length === 0) continue;

  // Highest across advisories - a single pin has to clear all of them at once.
  const targets = fixable.map((advisory) => advisory.target).sort(compareVersions);
  const target = targets[targets.length - 1];

  const currentPin = block.entries.get(bucket.name);
  const parsedPin = currentPin ? parseVersion(currentPin) : null;

  if (parsedPin && compareVersions(parsedPin, target) >= 0) {
    // Already pinned high enough; only the lockfile is behind.
    skipped.push({
      name: bucket.name,
      installed: installedOf(fixable),
      severity: fixable[0].severity,
      id: fixable.map((advisory) => advisory.id).join(", "),
      reason: `already pinned to ${currentPin}; run kotlinUpgradeYarnLock`,
    });
    continue;
  }

  const severities = fixable.map((advisory) => advisory.severity);
  updates.push({
    name: bucket.name,
    installed: installedOf(fixable),
    from: currentPin ?? null,
    target: target.raw,
    severity: severities.includes("CRITICAL") ? "CRITICAL" : "HIGH",
    ids: fixable.map((advisory) => advisory.id),
  });
}

if (updates.length > 0 && !args.dryRun) {
  for (const update of updates) block.entries.set(update.name, update.target);

  // Rebuild the whole block rather than splicing line by line: several insertions
  // with shifting indices is exactly where this kind of script goes wrong.
  const rebuilt = [...block.entries.entries()]
    // Plain code-unit sort - deterministic, locale-independent, and it reproduces the
    // existing order (`brace-expansion` before `braces`, since "-" sorts below "s").
    .sort((a, b) => (a[0] < b[0] ? -1 : a[0] > b[0] ? 1 : 0))
    .map(([name, version]) => `${block.indent}resolution("${name}", "${version}")`);

  gradle.lines.splice(block.first, block.last - block.first + 1, ...rebuilt);
  const text = gradle.lines.join(gradle.eol) + (gradle.trailingNewline ? gradle.eol : "");
  writeFileSync(args.gradle, text);
}

const summary = {
  changed: updates.length > 0,
  count: updates.length,
  dryRun: args.dryRun,
  updates,
  skipped,
};

if (args.json) writeFileSync(args.json, JSON.stringify(summary, null, 2));
if (args.md) writeFileSync(args.md, renderBody(updates, skipped));

if (process.env.GITHUB_OUTPUT) {
  const lines = `changed=${summary.changed}\ncount=${summary.count}\n`;
  appendFileSync(process.env.GITHUB_OUTPUT, lines);
}

for (const update of updates) {
  const from = update.from ?? "(unpinned)";
  console.log(
    `${update.name}: ${from} -> ${update.target} [${update.severity}] ${update.ids.join(", ")}`,
  );
}
for (const entry of skipped) {
  console.log(`skipped ${entry.name}@${entry.installed} (${entry.id}): ${entry.reason}`);
}
console.log(`${updates.length} package(s) to pin, ${skipped.length} skipped.`);
