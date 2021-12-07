package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.entities.AffixesWebEntity
import de.nilsdruyen.mythicplus.character.entities.PeriodWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileWebEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreTierWebEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataWebEntity
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import io.ktor.client.HttpClient
import io.ktor.client.features.BrowserUserAgent
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.host
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json

object RaiderIoApi {

  private val client = HttpClient {
    defaultRequest {
      host = "raider.io/api/v1"
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
    BrowserUserAgent()
  }

  suspend fun getCharacter(realm: String, name: String): ProfileWebEntity = client.get("characters/profile") {
    parameter("region", "eu")
    parameter("realm", realm)
    parameter("name", name)
    parameter(
      "fields",
      "mythic_plus_best_runs:all,mythic_plus_alternate_runs:all," +
        "mythic_plus_scores_by_season:current,gear,mythic_plus_recent_runs"
    )
  }

  suspend fun getCurrentAffixIds(): List<Int> = client.get<AffixesWebEntity>("mythic-plus/affixes") {
    parameter("region", "eu")
    parameter("locale", "en")
  }.details.map { it.id }

  suspend fun getStaticData(): StaticDataWebEntity = client.get("mythic-plus/static-data") {
    parameter("expansion_id", "8")
  }

  suspend fun getScoreTiers(): List<ScoreTier> = client.get<List<ScoreTierWebEntity>>("mythic-plus/score-tiers").map {
    ScoreTier(it.score, it.rgbHex)
  }

  suspend fun getCurrentPeriod(): PeriodWebEntity = client.get("periods")
}