package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.entities.AffixesWebEntity
import de.nilsdruyen.mythicplus.character.entities.MythicPlusDungeonWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileWebEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreTierWebEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataEntity
import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.enums.toSlot
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.DominationShard
import de.nilsdruyen.mythicplus.character.models.DungeonScore
import de.nilsdruyen.mythicplus.character.models.Gear
import de.nilsdruyen.mythicplus.character.models.Item
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
import kotlinx.serialization.json.Json
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

  suspend fun getCharacter(realm: String, name: String, tiers: List<ScoreTier>): Character {
    val entity = client.get<ProfileWebEntity>("characters/profile") {
      parameter("region", "eu")
      parameter("realm", realm)
      parameter("name", name)
      parameter(
        "fields",
        "mythic_plus_best_runs:all,mythic_plus_alternate_runs:all,mythic_plus_scores_by_season:current,gear"
      )
    }

    val charScore = entity.scoreBySeason.first().scores.all
    val list = Constants.Dungeons.map { dungeon ->
      val filteredDungeons = (entity.bestRuns + entity.altRuns).filter { it.shortName == dungeon }
      val tyrannical = filteredDungeons.mapToScore(Constants.TYRANNICAL)
      val fortified = filteredDungeons.mapToScore(Constants.FORTIFIED)
      DungeonScore(dungeon, tyrannical, fortified)
    }

    val items = entity.gear.items.map {
      val (key, data) = it
      Item(
        data.id,
        key.toSlot() ?: ItemSlot.Neck,
        data.name,
        data.itemLevel,
        data.icon,
        data.isLegendary,
        data.dominationShards.map { shard ->
          DominationShard(shard.quality, shard.name, shard.icon, shard.itemId)
        },
        data.gems,
        data.bonuses,
      )
    }
    val gear = Gear(entity.gear.iLvlEquipped, items)

    return Character(entity.name, charScore, tiers.getColorForScore(charScore), list, gear)
  }

  private fun List<MythicPlusDungeonWebEntity>.mapToScore(type: Int): Score {
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

  suspend fun getScoreTiers(): List<ScoreTier> {
    return client.get<List<ScoreTierWebEntity>>("mythic-plus/score-tiers").map {
      ScoreTier(it.score, it.rgbHex)
    }
  }
}