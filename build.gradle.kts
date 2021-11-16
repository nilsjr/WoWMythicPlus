plugins {
  kotlin("multiplatform") version "1.5.31" apply false
  kotlin("plugin.serialization") version "1.5.31" apply false
  id("org.jetbrains.compose") version "1.0.0-beta5" apply false
  id("com.github.ben-manes.versions") version "0.39.0"
  id("de.nilsdruyen.gradle-ftp-upload-plugin") version "0.0.1" apply false
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