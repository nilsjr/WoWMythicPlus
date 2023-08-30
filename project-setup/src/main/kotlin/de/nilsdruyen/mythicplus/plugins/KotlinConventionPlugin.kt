package de.nilsdruyen.mythicplus.plugins

import de.nilsdruyen.mythicplus.config.configure
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class KotlinConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      configure()
    }
  }
}