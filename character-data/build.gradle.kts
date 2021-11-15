plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  js(IR) {
    browser()
  }
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

        implementation("io.ktor:ktor-client-core:1.6.5")
        implementation("io.ktor:ktor-client-serialization:1.6.5")
        implementation("io.ktor:ktor-client-logging:1.6.5")
      }
    }
  }
}