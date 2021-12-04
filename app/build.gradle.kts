plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id(Plugins.Android.daggerHilt)
}

android {
  compileSdk = Versions.androidCompileSdk
  buildToolsVersion = Versions.androidBuildTools

  defaultConfig {
    applicationId = "de.nilsdruyen.mythicplus"
    minSdk = Versions.androidMinSdk
    targetSdk = Versions.androidTargetSdk
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  sourceSets.getByName("main").java.srcDirs("src/main/kotlin")
  buildFeatures {
    compose = true
    aidl = false
    buildConfig = false
    renderScript = false
    resValues = false
    shaders = false
  }
  composeOptions {
    kotlinCompilerExtensionVersion = Versions.Android.jetpackCompose
  }
  kapt {
    arguments {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
  }
}

dependencies {
  implementation(project(":character-data"))

  implementation(Deps.Android.ktx)
  implementation(Deps.Android.material)

  implementation(Deps.Android.runtimeKtx)
  implementation(Deps.Android.viewmodelKtx)
  implementation(Deps.Android.viewModelCompose)

  implementation(Deps.Android.Compose.activity)
  implementation(Deps.Android.Compose.ui)
  implementation(Deps.Android.Compose.material)
  implementation(Deps.Android.Compose.uiTooling)
  implementation(Deps.Android.Compose.uiToolingPreview)
//  implementation("androidx.hilt:hilt-navigation-compose:1.0.0-beta01")

  implementation(Deps.Android.Dagger.hilt)
  kapt(Deps.Android.Dagger.hiltCompiler)

  implementation(Deps.Android.roomKtx)
  kapt(Deps.Android.roomCompiler)
}

// Allow references to generated code
kapt {
  correctErrorTypes = true
}