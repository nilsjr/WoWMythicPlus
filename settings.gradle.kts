rootProject.name = "WoWMythicPlus"

pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
  resolutionStrategy {
    eachPlugin {
      when (requested.id.id) {
        "com.android.application", "com.android.library" -> {
          useModule("com.android.tools.build:gradle:${requested.version}")
        }
        "dagger.hilt.android.plugin" -> useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
      }
    }
  }
}

include(":web")
include(":character-data")
include(":app")