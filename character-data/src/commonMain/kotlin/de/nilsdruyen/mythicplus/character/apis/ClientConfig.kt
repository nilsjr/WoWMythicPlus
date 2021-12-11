package de.nilsdruyen.mythicplus.character.apis

import io.ktor.client.HttpClientConfig
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.host
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.applyDefaultConfig(baseUrl: String) = with(this) {
  defaultRequest {
    host = baseUrl
    url {
      protocol = URLProtocol.HTTPS
    }
  }
  install(Logging) {
    logger = Logger.DEFAULT
    level = LogLevel.INFO
  }
  install(JsonFeature) {
    serializer = KotlinxSerializer(Json {
      isLenient = true
      ignoreUnknownKeys = true
    })
  }
}