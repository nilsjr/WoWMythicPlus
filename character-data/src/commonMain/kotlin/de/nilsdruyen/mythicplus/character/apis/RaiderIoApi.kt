package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.entities.AffixesWebEntity
import de.nilsdruyen.mythicplus.character.entities.PeriodWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileRequest
import de.nilsdruyen.mythicplus.character.entities.ProfileWebEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreTierWebEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataWebEntity
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import de.nilsdruyen.mythicplus.character.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object RaiderIoApi {

  private val client = HttpClient {
    defaultRequest {
      url("https://raider.io/api/v1/")
    }
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

  suspend fun getCharacter(realm: String, name: String): ProfileWebEntity {
    return client.get(ProfileRequest(realm, name, Constants.Api.Fields.all())).body()
  }

  suspend fun getCurrentAffixIds(): List<Int> = client.get("mythic-plus/affixes") {
    parameter("region", "eu")
    parameter("locale", "en")
  }.body<AffixesWebEntity>().details.map { it.id }

  suspend fun getStaticData(): StaticDataWebEntity = client.get("mythic-plus/static-data") {
    parameter("expansion_id", "8")
  }.body()

  suspend fun getScoreTiers(): List<ScoreTier> = client.get("mythic-plus/score-tiers")
    .body<List<ScoreTierWebEntity>>()
    .map {
      ScoreTier(it.score, it.rgbHex)
    }

  suspend fun getCurrentPeriod(): PeriodWebEntity = client.get("periods").body()
}