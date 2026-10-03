import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
  dependencies {
    constraints {
      // gradle-ftp-upload-plugin pulls BouncyCastle 1.75 in via sshj; force a patched version.
      // Remove once the plugin ships an sshj that depends on bcprov >= 1.85.
      classpath(libs.bouncycastle.bcprov)
      classpath(libs.bouncycastle.bcpkix)
      classpath(libs.bouncycastle.bcutil)
    }
  }
}

plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.jetbrains.compose) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.buildconfig) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.gradleVersions)
  alias(libs.plugins.ftpupload) apply false
}

group = "de.nilsdruyen"
version = "0.7.0"

tasks.withType<DependencyUpdatesTask> {
  rejectVersionIf {
    isNonStable(candidate.version) && !isNonStable(currentVersion)
  }
}

fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}