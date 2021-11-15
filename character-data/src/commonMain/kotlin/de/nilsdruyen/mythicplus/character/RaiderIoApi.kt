package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.entities.AffixesWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileRioEntity
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.Score
import de.nilsdruyen.mythicplus.character.utils.Constants
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
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

    val list = Constants.Dungeons.map { dungeon ->
      val dList = (entity.bestRuns + entity.altRuns).filter {
        it.shortName == dungeon
      }

      val tyrann = dList.firstOrNull { it.affixes.any { affix -> affix.id == Constants.TYRANN } }
      val fort = dList.firstOrNull { it.affixes.any { affix -> affix.id == Constants.FORT } }

      val tyrannScore = if (tyrann == null) {
        Score(Constants.TYRANN, 0.0, 0, 0)
      } else {
        Score(Constants.TYRANN, tyrann.score, tyrann.level, tyrann.upgrades)
      }
      val fortScore = if (fort == null) {
        Score(Constants.FORT, 0.0, 0, 0)
      } else {
        Score(Constants.FORT, fort.score, fort.level, fort.upgrades)
      }

      Dungeon(dungeon, tyrannScore, fortScore)
    }

    return Character(entity.name, entity.scoreBySeason.first().scores.all.roundToInt(), list)
  }

  suspend fun getCurrentAffixIds(): List<Int> {
    val affixes = client.get<AffixesWebEntity>("mythic-plus/affixes") {
      parameter("region", "eu")
      parameter("locale", "en")
    }
    return affixes.details.map { it.id }
  }
}