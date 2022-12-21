plugins {
  `kotlin-dsl`
}

tasks.withType<JavaCompile>().configureEach {
  sourceCompatibility = JavaVersion.VERSION_11.toString()
  targetCompatibility = JavaVersion.VERSION_11.toString()
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_11.toString()
    freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
  }
}

dependencies {
  implementation(libs.kotlin.plugin)
  implementation(libs.detekt.plugin)
}

gradlePlugin {
  plugins {
    val detektPlugin by registering {
      id = "de.nilsdruyen.mythicplus.plugin.detekt"
      implementationClass = "de.nilsdruyen.mythicplus.plugins.DetektConventionPlugin"
    }
    val kotlinConventionPlugin by registering {
      id = "de.nilsdruyen.mythicplus.plugin.kotlin"
      implementationClass = "de.nilsdruyen.mythicplus.plugins.KotlinConventionPlugin"
    }
  }
}