import de.nilsdruyen.gradle.ftp.UploadExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
  id("de.nilsdruyen.gradle-ftp-upload-plugin")
  id("com.github.gmazzo.buildconfig")
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

        implementation(Deps.Kotlin.coroutines)

        implementation(project(":character-data"))
      }
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = "11"
  }
}

afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    versions.webpackDevServer.version = "4.0.0"
    versions.webpackCli.version = "4.9.0"
  }
}

buildConfig {
  useKotlinOutput()
  packageName("de.nilsdruyen.mythicplus")
  buildConfigField("String", "VERSION", "\"${project.version}\"")
}

configure<UploadExtension> {
  host = properties.getOrDefault("ftp.host", "").toString()
  port = properties.getOrDefault("ftp.port", 22).toString().toInt()
  username = properties.getOrDefault("ftp.username", "").toString()
  password = properties.getOrDefault("ftp.password", "").toString()
  sourceDir = "${project.buildDir}/distributions"
  targetDir = "/html/wowmythicplus"
}

val buildTask = tasks.named("jsBrowserProductionWebpack")
val uploadTask = tasks.named("uploadFilesToFtp")

tasks.register("deployWebsite") {
  group = "deployment"
  description = "Build & deploy website"

  dependsOn(buildTask, uploadTask)
  uploadTask.get().mustRunAfter(buildTask)

  doLast {
    println("deployment finished")
  }
}