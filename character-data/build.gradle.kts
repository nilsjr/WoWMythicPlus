import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  kotlin(Plugins.Kotlin.multiplatform)
  kotlin(Plugins.Kotlin.serial)
  kotlin("kapt")
  id(Plugins.Android.library)
  id("com.github.gmazzo.buildconfig")
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
        implementation(Deps.Ktor.auth)

        api(Deps.kermit)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation("javax.inject:javax.inject:1")

        implementation(Deps.Android.roomKtx)
        implementation(Deps.Ktor.okHttp)

        implementation(Deps.Android.Dagger.hilt)
        configurations["kapt"].dependencies.add(project.dependencies.create(Deps.Android.Dagger.hiltCompiler))
      }
    }
    val jsMain by getting
  }
}

buildConfig {
  useKotlinOutput()
  packageName("de.nilsdruyen.mythicplus.character")
  buildConfigField("String", "CLIENT_ID", "\"${gradleLocalProperties(rootDir).getProperty("battlenet.client_id")}\"")
  buildConfigField("String", "CLIENT_SECRET", "\"${gradleLocalProperties(rootDir).getProperty("battlenet.client_secret")}\"")
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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}