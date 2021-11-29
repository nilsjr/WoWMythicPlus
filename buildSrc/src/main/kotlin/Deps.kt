object Deps {

  object Kotlin {

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.dateTime}"
  }

  object Ktor {

    const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val serial = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val logging = "io.ktor:ktor-client-logging:${Versions.ktor}"
  }

  object Android {

    const val ktx = "androidx.core:core-ktx:1.7.0"

    const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"
    const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"

    object Compose {

      const val activity = "androidx.activity:activity-compose:1.4.0"

      const val ui = "androidx.compose.ui:ui:${Versions.jetpackCompose}"
      const val material = "androidx.compose.material3:material3:1.0.0-alpha01"
      const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.jetpackCompose}"
      const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.jetpackCompose}"
    }

    object Dagger {

      const val hilt = "com.google.dagger:hilt-android:2.38.1"
      const val hiltCompiler = "com.google.dagger:hilt-android-compiler:2.38.1"
    }
  }
}