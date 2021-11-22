package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileWebEntity(
  val name: String,
  @SerialName("class") val clazz: String,
  @SerialName("thumbnail_url") val thumbnailUrl: String,
  @SerialName("mythic_plus_best_runs") val bestRuns: List<MythicPlusDungeonWebEntity>,
  @SerialName("mythic_plus_alternate_runs") val altRuns: List<MythicPlusDungeonWebEntity>,
  @SerialName("mythic_plus_scores_by_season") val scoreBySeason: List<SeasonWebEntity>,
  val gear: GearWebEntity,
)

@Serializable
data class MythicPlusDungeonWebEntity(
   val dungeon: String,
  @SerialName("short_name") val shortName: String,
  @SerialName("mythic_level") val level: Int,
  @SerialName("num_keystone_upgrades") val upgrades: Int,
  @SerialName("zone_id") val id: Int,
  val score: Double,
  @SerialName("clear_time_ms") val clearTimeMs: Long,
  @SerialName("par_time_ms") val parTimeMs: Long,
  val affixes: List<AffixWebEntity>,
)

@Serializable
data class AffixWebEntity(
  val id: Int,
  val icon: String
)

@Serializable
data class SeasonWebEntity(
  val scores: SeasonScoreWebEntity
)

@Serializable
data class SeasonScoreWebEntity(
  val all: Double
)

