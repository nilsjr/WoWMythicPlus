rootProject.name = "WoWMythicPlus"

pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenCentral()
  }
}

include(":web")
include(":character-data")