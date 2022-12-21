package de.nilsdruyen.mythicplus.plugins

import de.nilsdruyen.mythicplus.dsl.applyDetekt
import de.nilsdruyen.mythicplus.dsl.applyDetektFormatting
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

@Suppress("unused")
internal class DetektConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply(DetektPlugin::class)
      applyDetekt()
      applyDetektFormatting()
    }
  }
}