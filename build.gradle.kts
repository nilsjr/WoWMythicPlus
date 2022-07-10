import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
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
version = "0.2.2"

subprojects {
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      val arguments = listOf(
        "-progressive",
        "-Xopt-in=kotlin.RequiresOptIn"
      )
      freeCompilerArgs = freeCompilerArgs + arguments
      jvmTarget = "11"
    }
  }
}

// On Apple Silicon we need Node.js 16.0.0
// https://youtrack.jetbrains.com/issue/KT-49109
//rootProject.plugins.withType(NodeJsRootPlugin::class) {
//  rootProject.the(NodeJsRootExtension::class).nodeVersion = "16.0.0"
//}