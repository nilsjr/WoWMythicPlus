plugins {
  kotlin("multiplatform")
  id(Plugins.Android.library)
  kotlin("plugin.serialization")
}

kotlin {
  js(IR) {
    browser()
  }
  android {
    publishLibraryVariants("release", "debug")
    publishLibraryVariantsGroupedByFlavor = true
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
    val androidMain by getting
  }
}

android {
  compileSdk = Versions.androidCompileSdk
  defaultConfig.minSdk = Versions.androidMinSdk
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  buildFeatures.buildConfig = false
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}