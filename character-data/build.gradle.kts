plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("de.nilsdruyen.mythicplus.plugin.kotlin")
  id("de.nilsdruyen.mythicplus.plugin.detekt")
}

kotlin {
  js(IR) {
    browser()
  }
//  android {
//    publishLibraryVariants("release", "debug")
//    publishLibraryVariantsGroupedByFlavor = true
//  }
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.coroutines)
        implementation(libs.datetime)

        implementation(libs.ktorClientCore)
        implementation(libs.ktorClientContentNegotiation)
        implementation(libs.ktorClientLogging)
        implementation(libs.ktorClientRes)

        implementation(libs.ktorSerialJson)
      }
    }
//    val androidMain by getting {
//      dependencies {
//        implementation("javax.inject:javax.inject:1")
//        implementation(Deps.Android.roomKtx)
//      }
//    }
    val jsMain by getting {
      dependencies {
        implementation(libs.ktor.client.js)
      }
    }
  }
}

// android {
//  compileSdk = Versions.androidCompileSdk
//  defaultConfig.minSdk = Versions.androidMinSdk
//  sourceSets.getByName("main") {
//    manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    java.srcDirs("src/main/kotlin")
//  }
//  buildFeatures {
//    aidl = false
//    buildConfig = false
//    renderScript = false
//    resValues = false
//    shaders = false
//  }
//  compileOptions {
//    sourceCompatibility = JavaVersion.VERSION_17
//    targetCompatibility = JavaVersion.VERSION_17
//  }
// }