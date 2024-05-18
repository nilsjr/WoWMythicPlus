rootProject.name = "WoWMythicPlus"

pluginManagement {
  includeBuild("project-setup")

  repositories {
    mavenLocal()
    google()
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}
dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

include(":web")
include(":character-data")
//include(":app")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")