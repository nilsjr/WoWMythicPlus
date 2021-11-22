object Deps {

  object Kotlin {

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
  }

  object Ktor {

    const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val serial = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val logging = "io.ktor:ktor-client-logging:${Versions.ktor}"
  }
}