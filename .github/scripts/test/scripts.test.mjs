// Run with: node --test .github/scripts/test/
//
// Both apply scripts edit files that the build depends on, from data fetched at 3am on
// a schedule nobody watches. The cases below are the ones where a silent mistake would
// be expensive: a cross-major bump applied automatically, a shared version ref moved
// without saying so, or a finding quietly dropped instead of reported.

import { test } from "node:test";
import assert from "node:assert/strict";
import { execFileSync } from "node:child_process";
import { mkdtempSync, readFileSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";

const SCRIPTS = new URL("../", import.meta.url).pathname;

// A minimal osv-scanner report: one package, one advisory, one fixed version.
function report(ecosystem, entries) {
  return {
    results: [
      {
        packages: entries.map(({ name, version, id = "GHSA-test", severity = "HIGH", fixed }) => ({
          package: { name, version, ecosystem },
          vulnerabilities: [
            {
              id,
              database_specific: { severity },
              affected: [
                {
                  package: { name, ecosystem },
                  ranges: fixed
                    ? [{ type: "ECOSYSTEM", events: [{ introduced: "0" }, { fixed }] }]
                    : [],
                },
              ],
            },
          ],
        })),
      },
    ],
  };
}

function run(script, dir, args) {
  const out = execFileSync("node", [join(SCRIPTS, script), ...args], { encoding: "utf8", cwd: dir });
  return out;
}

// ------------------------------------------------------------------- gradle

const CATALOG = `[versions]
coroutines = "1.11.0"
kotlin = "2.4.10"
tiny = "0.0.3"

[libraries]
kotlinx-coroutines = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }
tiny-lib = { module = "com.example:tiny", version.ref = "tiny" }
pinned = { module = "com.example:pinned", version = "1.2.3" }

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
`;

function gradleCase(entries) {
  const dir = mkdtempSync(join(tmpdir(), "osv-gradle-"));
  writeFileSync(join(dir, "libs.versions.toml"), CATALOG);
  writeFileSync(join(dir, "scan.json"), JSON.stringify(report("Maven", entries)));
  run("apply-gradle-versions.mjs", dir, [
    "--scan", "scan.json", "--catalog", "libs.versions.toml", "--json", "out.json",
  ]);
  return {
    summary: JSON.parse(readFileSync(join(dir, "out.json"), "utf8")),
    catalog: readFileSync(join(dir, "libs.versions.toml"), "utf8"),
  };
}

test("gradle: bumps the [versions] ref behind a direct catalog entry", () => {
  const { summary, catalog } = gradleCase([
    { name: "org.jetbrains.kotlinx:kotlinx-coroutines-core", version: "1.11.0", fixed: "1.11.1" },
  ]);
  assert.equal(summary.changed, true);
  assert.equal(summary.updates[0].key, "coroutines");
  assert.match(catalog, /coroutines = "1\.11\.1"/);
  // Untouched lines stay byte-identical.
  assert.match(catalog, /kotlin = "2\.4\.10"/);
});

test("gradle: reports the other users of a shared version ref", () => {
  const { summary } = gradleCase([
    {
      name: "org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin",
      version: "2.4.10",
      fixed: "2.4.20",
    },
  ]);
  assert.deepEqual(summary.updates[0].alsoMoves, [
    "org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin",
  ]);
});

test("gradle: never bumps across a major", () => {
  const { summary, catalog } = gradleCase([
    { name: "org.jetbrains.kotlinx:kotlinx-coroutines-core", version: "1.11.0", fixed: "2.0.0" },
  ]);
  assert.equal(summary.changed, false);
  assert.match(summary.skipped[0].reason, /no fix within the 1\.x line/);
  assert.equal(catalog, CATALOG);
});

test("gradle: never bumps a 0.0.x dependency automatically", () => {
  const { summary } = gradleCase([
    { name: "com.example:tiny", version: "0.0.3", fixed: "0.0.4" },
  ]);
  assert.equal(summary.changed, false);
  assert.match(summary.skipped[0].reason, /no fix within the 0\.0\.3 line/);
});

test("gradle: transitive findings are reported, not silently dropped", () => {
  const { summary } = gradleCase([
    { name: "com.squareup.okio:okio", version: "3.0.0", fixed: "3.0.1" },
  ]);
  assert.equal(summary.changed, false);
  assert.equal(summary.unfixable, 1);
  assert.match(summary.skipped[0].reason, /not a direct version-catalog entry/);
});

test("gradle: bumps an inline version on the entry's own line", () => {
  const { summary, catalog } = gradleCase([
    { name: "com.example:pinned", version: "1.2.3", fixed: "1.2.4" },
  ]);
  assert.equal(summary.changed, true);
  assert.match(catalog, /module = "com\.example:pinned", version = "1\.2\.4"/);
});

test("gradle: MODERATE advisories are below the threshold", () => {
  const { summary } = gradleCase([
    {
      name: "org.jetbrains.kotlinx:kotlinx-coroutines-core",
      version: "1.11.0",
      fixed: "1.11.1",
      severity: "MODERATE",
    },
  ]);
  assert.equal(summary.changed, false);
  assert.equal(summary.skipped.length, 0);
});

test("gradle: handles a two-component Maven version", () => {
  const dir = mkdtempSync(join(tmpdir(), "osv-gradle-"));
  writeFileSync(join(dir, "libs.versions.toml"), '[versions]\nshort = "1.11"\n\n[libraries]\na = { module = "com.example:short", version.ref = "short" }\n');
  writeFileSync(join(dir, "scan.json"), JSON.stringify(report("Maven", [
    { name: "com.example:short", version: "1.11", fixed: "1.12" },
  ])));
  run("apply-gradle-versions.mjs", dir, ["--scan", "scan.json", "--catalog", "libs.versions.toml", "--json", "out.json"]);
  assert.match(readFileSync(join(dir, "libs.versions.toml"), "utf8"), /short = "1\.12"/);
});

// ---------------------------------------------------------------------- npm
// Regression cover for the shared-helper extraction: these are the behaviours the
// yarn workflow has been relying on in production.

const BUILD_GRADLE = `rootProject.plugins.withType<YarnPlugin> {
  rootProject.the<YarnRootExtension>().apply {
    resolution("braces", "3.0.3")
    resolution("ws", "8.21.0")
  }
}
`;

function npmCase(entries) {
  const dir = mkdtempSync(join(tmpdir(), "osv-npm-"));
  writeFileSync(join(dir, "build.gradle.kts"), BUILD_GRADLE);
  writeFileSync(join(dir, "scan.json"), JSON.stringify(report("npm", entries)));
  run("apply-npm-resolutions.mjs", dir, [
    "--scan", "scan.json", "--gradle", "build.gradle.kts", "--json", "out.json",
  ]);
  return {
    summary: JSON.parse(readFileSync(join(dir, "out.json"), "utf8")),
    gradle: readFileSync(join(dir, "build.gradle.kts"), "utf8"),
  };
}

test("npm: adds a pin for a package that was never pinned, in sorted order", () => {
  const { summary, gradle } = npmCase([{ name: "nanoid", version: "3.3.6", fixed: "3.3.8" }]);
  assert.equal(summary.changed, true);
  const lines = gradle.split("\n").filter((l) => l.includes("resolution("));
  assert.deepEqual(lines.map((l) => l.trim()), [
    'resolution("braces", "3.0.3")',
    'resolution("nanoid", "3.3.8")',
    'resolution("ws", "8.21.0")',
  ]);
});

test("npm: raises an existing pin", () => {
  const { summary, gradle } = npmCase([{ name: "ws", version: "8.21.0", fixed: "8.22.0" }]);
  assert.equal(summary.count, 1);
  assert.match(gradle, /resolution\("ws", "8\.22\.0"\)/);
});

test("npm: never bumps across a major", () => {
  const { summary, gradle } = npmCase([{ name: "braces", version: "3.0.3", fixed: "4.0.0" }]);
  assert.equal(summary.changed, false);
  assert.equal(gradle, BUILD_GRADLE);
  assert.match(summary.skipped[0].reason, /no fix within the 3\.x line/);
});

test("npm: an advisory with no fixed version is reported", () => {
  const { summary } = npmCase([{ name: "braces", version: "3.0.3" }]);
  assert.equal(summary.changed, false);
  assert.match(summary.skipped[0].reason, /publishes no fixed version/);
});
