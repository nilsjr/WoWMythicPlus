import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  `kotlin-dsl`
}

tasks.withType<JavaCompile>().configureEach {
  sourceCompatibility = JavaVersion.VERSION_17.toString()
  targetCompatibility = JavaVersion.VERSION_17.toString()
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
    allWarningsAsErrors.set(false)
    progressiveMode.set(true)
    explicitApiMode.set(ExplicitApiMode.Strict)
    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
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