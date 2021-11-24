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
        implementation(Deps.Kotlin.coroutines)
        implementation(Deps.Kotlin.dateTime)

        implementation(Deps.Ktor.core)
        implementation(Deps.Ktor.serial)
        implementation(Deps.Ktor.logging)
      }
    }
  }
}