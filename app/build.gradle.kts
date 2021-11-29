plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("org.jetbrains.compose")
  id("dagger.hilt.android.plugin")
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
  }
  composeOptions {
    kotlinCompilerExtensionVersion = Versions.jetpackCompose
  }
}

dependencies {
  implementation(project(":character-data"))

  implementation("androidx.core:core-ktx:1.7.0")

  implementation("com.google.android.material:material:1.5.0-beta01")

  implementation("androidx.compose.ui:ui:${Versions.jetpackCompose}")
  implementation("androidx.activity:activity-compose:1.4.0")
  implementation("androidx.compose.material3:material3:1.0.0-alpha01")
  implementation("androidx.compose.ui:ui-tooling:${Versions.jetpackCompose}")
  implementation("androidx.compose.ui:ui-tooling-preview:${Versions.jetpackCompose}")

  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")

  implementation("com.google.dagger:hilt-android:2.38.1")
  kapt("com.google.dagger:hilt-android-compiler:2.38.1")

  // startup,
}

// Allow references to generated code
kapt {
  correctErrorTypes = true
}