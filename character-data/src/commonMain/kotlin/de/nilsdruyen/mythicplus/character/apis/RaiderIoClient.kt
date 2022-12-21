package de.nilsdruyen.mythicplus.character.apis

import io.ktor.client.HttpClient
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object RaiderIoClient {

  fun build() = HttpClient {
    defaultRequest {
      url("https://raider.io/api/v1/")
    }
    install(HttpCache)
    install(Logging) {
      logger = Logger.DEFAULT
      level = LogLevel.INFO
    }
    install(ContentNegotiation) {
      json(Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
      })
    }
    install(Resources)
    BrowserUserAgent()
  }
}