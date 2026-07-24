
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
  compilerOptions {
    optIn.add("kotlin.time.ExperimentalTime")
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

    resolution("ajv", "8.20.0")
    resolution("async", "2.6.4")
    resolution("body-parser", "1.20.6")
    resolution("brace-expansion", "5.0.8")
    resolution("braces", "3.0.3")
    resolution("cookie", "0.7.2")
    resolution("cross-spawn", "7.0.6")
    resolution("diff", "5.2.2")
    resolution("engine.io", "6.6.9")
    resolution("eventsource", "1.1.1")
    resolution("express", "4.22.2")
    resolution("flatted", "3.4.2")
    resolution("follow-redirects", "1.16.0")
    resolution("glob", "10.5.0")
    resolution("http-proxy-middleware", "2.0.10")
    resolution("js-yaml", "4.3.0")
    resolution("json5", "2.2.2")
    resolution("launch-editor", "2.14.1")
    resolution("loader-utils", "2.0.4")
    resolution("lodash", "4.18.1")
    resolution("micromatch", "4.0.8")
    resolution("minimatch", "9.0.7")
    resolution("minimist", "1.2.6")
    resolution("nanoid", "3.3.16")
    resolution("node-forge", "1.4.0")
    resolution("on-headers", "1.1.0")
    resolution("path-to-regexp", "0.1.13")
    resolution("picomatch", "2.3.2")
    resolution("qs", "6.15.3")
    resolution("send", "0.19.2")
    resolution("serialize-javascript", "7.0.5")
    resolution("serve-static", "1.16.3")
    resolution("shell-quote", "1.10.0")
    resolution("socket.io", "4.8.3")
    resolution("socket.io-parser", "4.2.6")
    resolution("tmp", "0.2.7")
    resolution("ua-parser-js", "0.7.33")
    resolution("uuid", "11.1.1")
    resolution("websocket-driver", "0.7.5")
    resolution("ws", "8.21.0")
  }
  rootProject.the<NodeJsRootExtension>().apply {
    versions.webpackDevServer.version = "5.2.6"
    versions.webpack.version = "5.109.0"
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
  targetDir = "${properties["wowmyhthicplus.serverPath"]}/web/wowmythicplus"
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