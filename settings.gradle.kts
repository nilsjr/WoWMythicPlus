rootProject.name = "WoWMythicPlus"

pluginManagement {
  repositories {
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

includeBuild("project-setup")

include(":web")
include(":character-data")
//include(":app")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")