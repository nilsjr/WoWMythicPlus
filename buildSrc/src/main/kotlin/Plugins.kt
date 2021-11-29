object Plugins {

  object Kotlin {
    const val androidGradle = "android"
    const val android = "kotlin-android"
    const val dokka = "org.jetbrains.dokka"
    const val multiplatform = "multiplatform"
    const val serial = "plugin.serialization"

    const val compose = "org.jetbrains.compose"
  }

  object Android {
    const val application = "com.android.application"
    const val library = "com.android.library"

    const val daggerHilt = "dagger.hilt.android.plugin"
  }

  const val detekt = "io.gitlab.arturbosch.detekt"
  const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
  const val gradleVersions = "com.github.ben-manes.versions"
  const val buildConfig = "com.github.gmazzo.buildconfig"
  const val uploadPlugin = "de.nilsdruyen.gradle-ftp-upload-plugin"
}