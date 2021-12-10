plugins {
  kotlin(Plugins.Kotlin.multiplatform)
  kotlin(Plugins.Kotlin.serial)
  id(Plugins.Android.library)
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
    val androidMain by getting {
      dependencies {
        implementation("javax.inject:javax.inject:1")
        implementation(Deps.Android.roomKtx)
      }
    }
    val jsMain by getting
  }
}

android {
  compileSdk = Versions.androidCompileSdk
  defaultConfig.minSdk = Versions.androidMinSdk
  sourceSets.getByName("main") {
    manifest.srcFile("src/androidMain/AndroidManifest.xml")
    java.srcDirs("src/main/kotlin")
  }
  buildFeatures {
    aidl = false
    buildConfig = false
    renderScript = false
    resValues = false
    shaders = false
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}