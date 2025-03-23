import de.nilsdruyen.gradle.ftp.UploadExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
  id("de.nilsdruyen.gradle-ftp-upload-plugin")
  id("com.github.gmazzo.buildconfig")
  id("de.nilsdruyen.mythicplus.plugin.kotlin")
  id("de.nilsdruyen.mythicplus.plugin.detekt")
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  js(IR) {
    browser()
    binaries.executable()
  }
  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.html.core)

        implementation(libs.coroutines)

        implementation(projects.characterData)

        implementation(libs.ktorClientCore)
        implementation(libs.ktor.client.js)
      }
    }
  }
}

rootProject.plugins.withType<YarnPlugin> {
  rootProject.the<YarnRootExtension>().apply {
    lockFileDirectory = project.rootDir.resolve(".kotlin-js-store")
    yarnLockMismatchReport = YarnLockMismatchReport.WARNING
    yarnLockAutoReplace = false

    resolution("async", "2.6.4")
    resolution("eventsource", "1.1.1")
    resolution("json5", "2.2.2")
    resolution("loader-utils", "2.0.4")
    resolution("minimist", "1.2.6")
    resolution("socket.io-parser", "4.2.3")
    resolution("ua-parser-js", "0.7.33")
    resolution("http-proxy-middleware", "2.0.7")
  }
  rootProject.the<NodeJsRootExtension>().apply {
    versions.webpackDevServer.version = "5.0.4"
    versions.webpack.version = "5.91.0"
    versions.webpackCli.version = "5.1.4"
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
  sourceDir = "${project.layout.buildDirectory.get()}/processedResources/js/main"
  targetDir =  "${properties["wowmyhthicplus.serverPath"].toString()}/web/wowmythicplus"
  clearDirectoryBeforeUpload = true
}

val buildTask = tasks.named("jsBrowserProductionWebpack")
val uploadTask = tasks.named("uploadFilesToFtp")

tasks.register<Copy>("copyJs") {
  from("${project.layout.buildDirectory.get()}/kotlin-webpack/js/productionExecutable")
  into("${project.layout.buildDirectory.get()}/processedResources/js/main")
}

val copyTask = tasks.named("copyJs")

tasks.register("deployWebsite") {
  group = "deployment"
  description = "Build & deploy website"

  dependsOn(buildTask, copyTask, uploadTask)
  copyTask.get().mustRunAfter(buildTask)
  uploadTask.get().mustRunAfter(buildTask)

  doLast {
    println("deployment finished")
  }
}