rootProject.name = "WoWMythicPlus"

pluginManagement {
  includeBuild("project-setup")

  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

include(":web")
include(":character-data")
//include(":app")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")