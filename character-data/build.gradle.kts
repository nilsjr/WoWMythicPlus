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
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("test").java.srcDirs("src/test/kotlin")
  }
  buildFeatures {
    aidl = false
    buildConfig = false
    renderScript = false
    resValues = false
    shaders = false
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}