package de.nilsdruyen.mythicplus.config

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configure() {
  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
  }
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = JavaVersion.VERSION_11.toString()
      languageVersion = "1.8"
      freeCompilerArgs = freeCompilerArgs + listOfNotNull(
        "-progressive", // https://kotlinlang.org/docs/whatsnew13.html#progressive-mode
        "-Xcontext-receivers",
        "-opt-in=kotlin.RequiresOptIn",
      )
    }
  }
}