package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.entities.AffixesWebEntity
import de.nilsdruyen.mythicplus.character.entities.MythicPlusDungeonEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileRioEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreTierEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataEntity
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.DungeonScore
import de.nilsdruyen.mythicplus.character.models.Score
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import de.nilsdruyen.mythicplus.character.utils.Constants
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.math.absoluteValue

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
    val charScore = entity.scoreBySeason.first().scores.all
    val list = Constants.Dungeons.map { dungeon ->
      val filteredDungeons = (entity.bestRuns + entity.altRuns).filter { it.shortName == dungeon }
      val tyrannical = filteredDungeons.mapToScore(Constants.TYRANNICAL)
      val fortified = filteredDungeons.mapToScore(Constants.FORTIFIED)
      DungeonScore(dungeon, tyrannical, fortified)
    }

    return Character(entity.name, charScore, tiers.getColorForScore(charScore), list)
  }

  private fun List<MythicPlusDungeonEntity>.mapToScore(type: Int): Score {
    val dungeon = this.firstOrNull { it.affixes.any { affix -> affix.id == type } }
    return if (dungeon == null) {
      Score.empty(type)
    } else {
      Score(type, dungeon.score, dungeon.level, dungeon.upgrades, dungeon.clearTimeMs)
    }
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

  suspend fun getStaticData(): StaticDataEntity {
    return client.get("mythic-plus/static-data") {
      parameter("expansion_id", "8")
    }
  }

  private suspend fun getScoreTiers(): List<ScoreTier> {
    return client.get<List<ScoreTierEntity>>("mythic-plus/score-tiers").map {
      ScoreTier(it.score, it.rgbHex)
    }
  }
}