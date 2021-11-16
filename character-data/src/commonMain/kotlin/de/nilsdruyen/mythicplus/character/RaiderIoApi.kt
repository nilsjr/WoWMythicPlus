package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.entities.AffixesWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileRioEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreTierEntity
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.Score
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import de.nilsdruyen.mythicplus.character.utils.Constants
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
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

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
      level = LogLevel.ALL
    }
    install(JsonFeature) {
      serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
        isLenient = true
        ignoreUnknownKeys = true
      })
    }
    BrowserUserAgent()
  }

  suspend fun getCharacter(realm: String, name: String): Character {
    val entity = client.get<ProfileRioEntity>("characters/profile") {
      parameter("region", "eu")
      parameter("realm", realm)
      parameter("name", name)
      parameter("fields", "mythic_plus_best_runs,mythic_plus_alternate_runs,mythic_plus_scores_by_season:current")
    }

    val tiers = getScoreTiers()

    val list = Constants.Dungeons.map { dungeon ->
      val dList = (entity.bestRuns + entity.altRuns).filter {
        it.shortName == dungeon
      }

      val tyrann = dList.firstOrNull { it.affixes.any { affix -> affix.id == Constants.TYRANN } }
      val fort = dList.firstOrNull { it.affixes.any { affix -> affix.id == Constants.FORT } }

      val tyrannScore = if (tyrann == null) {
        Score(Constants.TYRANN, 0.0, "", 0, 0)
      } else {
        Score(Constants.TYRANN, tyrann.score, tiers.getColorForScore(tyrann.score), tyrann.level, tyrann.upgrades)
      }
      val fortScore = if (fort == null) {
        Score(Constants.FORT, 0.0, "", 0, 0)
      } else {
        Score(Constants.FORT, fort.score, tiers.getColorForScore(fort.score), fort.level, fort.upgrades)
      }

      Dungeon(dungeon, tyrannScore, fortScore)
    }

    return Character(entity.name, entity.scoreBySeason.first().scores.all.roundToInt(), list)
  }

  private fun List<ScoreTier>.getColorForScore(score: Double): String {
    val colorForScore = this.map {
      it.hexColor to (it.score - score).absoluteValue
    }.minByOrNull { it.second }
    println("score color: $colorForScore")
    return colorForScore?.first ?: ""
  }

  suspend fun getCurrentAffixIds(): List<Int> {
    return client.get<AffixesWebEntity>("mythic-plus/affixes") {
      parameter("region", "eu")
      parameter("locale", "en")
    }.details.map { it.id }
  }

  private suspend fun getScoreTiers(): List<ScoreTier> {
    return client.get<List<ScoreTierEntity>>("mythic-plus/score-tiers").map {
      ScoreTier(it.score, it.rgbHex)
    }
  }
}