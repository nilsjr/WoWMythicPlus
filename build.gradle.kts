import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  kotlin("multiplatform") version libs.versions.kotlin.get() apply false
  kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
  id("org.jetbrains.compose") version libs.versions.compose.get() apply false
  // android
//  id(Plugins.Android.application) version Versions.Android.gradle apply false
//  kotlin(Plugins.Kotlin.androidGradle) version Versions.kotlin apply false
//  id(Plugins.Android.daggerHilt) version Versions.Android.daggerHilt apply false

  id("com.github.gmazzo.buildconfig") version libs.versions.buildKonfig.get() apply false
  id("com.github.ben-manes.versions") version libs.versions.versionUpdates.get()
  id("io.gitlab.arturbosch.detekt") version libs.versions.detekt.get()

  alias(libs.plugins.ftpupload) apply false
}

group = "de.nilsdruyen"
version = "0.1.1"

allprojects {
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

subprojects {
  apply(plugin = "io.gitlab.arturbosch.detekt")

  version = rootProject.version

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

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "11"
    }
  }
}

fun Project.configureDetekt(vararg paths: String) {
  val version = rootProject.libs.versions.detekt.get()
  configure<DetektExtension> {
    toolVersion = version
    source = files(paths)
    parallel = true
    config = files("$rootDir/detekt-config.yml")
    buildUponDefaultConfig = false
    ignoreFailures = false
  }
  tasks.withType<Detekt>().configureEach {
    reports {
      xml {
        required.set(true)
        outputLocation.set(file("$buildDir/reports/detekt/detekt.xml"))
      }
      html.required.set(false)
      txt.required.set(true)
    }
  }
  dependencies {
    "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:$version")
  }
}

// On Apple Silicon we need Node.js 16.0.0
// https://youtrack.jetbrains.com/issue/KT-49109
rootProject.plugins.withType(NodeJsRootPlugin::class) {
  rootProject.the(NodeJsRootExtension::class).nodeVersion = "16.0.0"
}