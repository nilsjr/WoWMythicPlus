// Shared helpers for the two OSV-driven security workflows: apply-npm-resolutions.mjs
// (yarn.lock -> resolution() pins) and apply-gradle-versions.mjs (Maven advisories ->
// libs.versions.toml bumps). Extracted rather than copied so the severity threshold and
// the "never cross a breaking boundary" rule can only ever have one definition.

const SEVERITIES = ["LOW", "MODERATE", "HIGH", "CRITICAL"];
const THRESHOLD = SEVERITIES.indexOf("HIGH");

export { SEVERITIES, THRESHOLD };

export function fail(message) {
  console.error(`::error::${message}`);
  process.exit(1);
}

// ------------------------------------------------------------------- semver

// Deliberately minimal: every version handled here comes from a lockfile, a version
// catalog or an OSV `fixed` event, so it is always a concrete release, never a range.
//
// `pad` exists for Maven, which does not promise three components - "1.11" and even "3"
// are legal coordinates. Only the numeric fields are padded; `raw` keeps the original
// text, because that is what gets written back into the file.
export function parseVersion(raw, { pad = false } = {}) {
  const strict = /^v?(\d+)\.(\d+)\.(\d+)(?:-([0-9A-Za-z.-]+))?(?:\+[0-9A-Za-z.-]+)?$/;
  const loose = /^v?(\d+)(?:\.(\d+))?(?:\.(\d+))?(?:[-.]([0-9A-Za-z.-]+))?$/;
  const trimmed = String(raw).trim();
  const match = (pad ? loose : strict).exec(trimmed);
  if (!match) return null;
  return {
    major: Number(match[1]),
    minor: Number(match[2] ?? 0),
    patch: Number(match[3] ?? 0),
    prerelease: match[4] ?? null,
    // `raw` is written straight into the file, so drop any leading "v": OSV does not
    // forbid a `fixed: "v1.2.3"` event, and neither yarn nor Gradle would accept it.
    raw: trimmed.replace(/^v/, ""),
  };
}

export function compareVersions(a, b) {
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
export function lineOf(version) {
  if (version.major > 0) return `${version.major}.x`;
  if (version.minor > 0) return `0.${version.minor}.x`;
  return `0.0.${version.patch}`;
}

export function sameLine(current, candidate) {
  if (current.major !== candidate.major) return false;
  if (current.major === 0 && current.minor !== candidate.minor) return false;
  if (current.major === 0 && current.minor === 0) return false;
  return true;
}

// ------------------------------------------------------------------ scanning

export function severityOf(vulnerability) {
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
export function fixedVersionsFor(vulnerability, packageName, ecosystem, options = {}) {
  const versions = [];
  for (const affected of vulnerability?.affected ?? []) {
    const pkg = affected?.package;
    if (!pkg || pkg.name !== packageName) continue;
    if (!String(pkg.ecosystem ?? "").startsWith(ecosystem)) continue;
    for (const range of affected?.ranges ?? []) {
      if (range?.type === "GIT") continue;
      for (const event of range?.events ?? []) {
        if (!event?.fixed) continue;
        const parsed = parseVersion(event.fixed, options);
        if (parsed) versions.push(parsed);
      }
    }
  }
  return versions;
}

// ----------------------------------------------------------------- reporting

export function table(header, rows) {
  return [
    `| ${header.join(" | ")} |`,
    `|${header.map(() => "---").join("|")}|`,
    ...rows.map((row) => `| ${row.join(" | ")} |`),
  ].join("\n");
}

// The working tree may be CRLF (core.autocrlf=true) while git blobs are LF. Getting
// this wrong rewrites every line of the file instead of the handful that changed.
export function readLines(text) {
  const eol = text.includes("\r\n") ? "\r\n" : "\n";
  const trailingNewline = /\r?\n$/.test(text);
  const lines = text.split(/\r?\n/);
  // A trailing newline leaves an empty final element behind; drop it here and add the
  // newline back on join, so the flag is the single source of truth either way.
  if (trailingNewline) lines.pop();
  return { lines, eol, trailingNewline };
}

export function joinLines({ lines, eol, trailingNewline }) {
  return lines.join(eol) + (trailingNewline ? eol : "");
}
