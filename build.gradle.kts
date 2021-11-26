plugins {
  kotlin(Plugins.Kotlin.multiplatform) version Versions.kotlin apply false
  kotlin(Plugins.Kotlin.serial) version Versions.kotlin apply false
  id(Plugins.Android.application) version Versions.androidGradle apply false
  kotlin(Plugins.Kotlin.androidGradle) version Versions.kotlin apply false
  id(Plugins.Kotlin.compose) version Versions.compose apply false
  id(Plugins.gradleVersions) version Versions.benManesVersions
  id(Plugins.detekt) version Versions.detekt
  id(Plugins.buildConfig) version Versions.buildConfig apply false
  id(Plugins.uploadPlugin) version Versions.ftpUploadPlugin apply false
}

group = "de.nilsdruyen"
version = "0.1.0"

subprojects {
  apply(plugin = Plugins.detekt)

  version = rootProject.version

  when (this.name) {
    "character-data" -> {
      configureDetekt("src/commonMain/kotlin")
    }
    "web" -> {
      configureDetekt("src/jsMain/kotlin")
    }
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "11"
    }
  }
}

fun Project.configureDetekt(vararg paths: String) {
  configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    toolVersion = Versions.detekt
    source = files(paths)
    parallel = true
    config = files("$rootDir/detekt-config.yml")
    buildUponDefaultConfig = true
    ignoreFailures = false
    reports {
      xml {
        enabled = true
        destination = file("$buildDir/reports/detekt/detekt.xml")
      }
      html.enabled = false
      txt.enabled = true
    }
  }
}