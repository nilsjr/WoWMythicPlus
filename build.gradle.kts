plugins {
  kotlin("multiplatform") version Versions.kotlin apply false
  kotlin("plugin.serialization") version Versions.kotlin apply false
  id("org.jetbrains.compose") version Versions.compose apply false
  id("com.github.ben-manes.versions") version Versions.benManesVersions
  id("io.gitlab.arturbosch.detekt") version Versions.detekt
  id("de.nilsdruyen.gradle-ftp-upload-plugin") version Versions.ftpUploadPlugin apply false
}

group = "de.nilsdruyen"
version = "1.0"

allprojects {
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

subprojects {
  apply(plugin = Plugins.detekt)

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