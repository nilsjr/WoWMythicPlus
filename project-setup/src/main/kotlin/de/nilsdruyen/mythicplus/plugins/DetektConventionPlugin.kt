package de.nilsdruyen.mythicplus.plugins

import de.nilsdruyen.mythicplus.dsl.configureDetekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

internal class DetektConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply(DetektPlugin::class)
      when (this.name) {
        "character-data" -> {
          configureDetekt("src/commonMain/kotlin", "src/androidMain/kotlin", "src/jsMain/kotlin")
        }
        "web" -> {
          configureDetekt("src/jsMain/kotlin")
        }
        "app" -> {
          configureDetekt("src/main/kotlin")
        }
      }
    }
  }
}