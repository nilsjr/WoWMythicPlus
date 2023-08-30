plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.jetbrains.compose) apply false
  alias(libs.plugins.buildconfig) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.gradleVersions)
  alias(libs.plugins.ftpupload) apply false
}

group = "de.nilsdruyen"
version = "0.4.0"