import de.nilsdruyen.gradle.ftp.UploadExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
  id("de.nilsdruyen.gradle-ftp-upload-plugin")
  id("com.github.gmazzo.buildconfig")
  id("de.nilsdruyen.mythicplus.plugin.detekt")
}

kotlin {
  js(IR) {
    browser()
    binaries.executable()
  }
  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(compose.web.core)
        implementation(compose.runtime)

        implementation(libs.coroutines)

        implementation(projects.characterData)
      }
    }
  }
}

rootProject.plugins.withType<YarnPlugin> {
  rootProject.the<YarnRootExtension>().apply {
    lockFileDirectory = project.rootDir.resolve("kotlin-js-store")
    resolution("async", "2.6.4")
    resolution("minimist", "1.2.6")
    resolution("eventsource", "1.1.1")
    resolution("loader-utils", "2.0.3")
  }
  rootProject.the<NodeJsRootExtension>().apply {
    versions.webpackDevServer.version = "4.9.3"
    versions.webpack.version = "5.73.0"
    versions.webpackCli.version = "4.10.0"
    versions.karma.version = "6.4.0"
    versions.mocha.version = "10.0.0"
  }
}

buildConfig {
  useKotlinOutput()
  packageName("de.nilsdruyen.mythicplus")
  buildConfigField("String", "VERSION", "\"${rootProject.version}\"")
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