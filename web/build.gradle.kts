import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
}

kotlin {
  js(IR) {
    browser {
      testTask {
        testLogging.showStandardStreams = true
        useKarma {
          useChromeHeadless()
          useFirefox()
        }
      }
    }
    binaries.executable()
  }
  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(compose.web.core)
        implementation(compose.runtime)

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

        implementation(project(":character-data"))
      }
    }
  }
}

afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    versions.webpackDevServer.version = "4.0.0"
    versions.webpackCli.version = "4.9.0"
  }
}