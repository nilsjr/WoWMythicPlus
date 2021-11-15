pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }

}
rootProject.name = "WoWMythicPlus"

include(":web")
include(":character-data")