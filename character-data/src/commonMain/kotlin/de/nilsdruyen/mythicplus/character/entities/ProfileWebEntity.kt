package de.nilsdruyen.mythicplus.character.entities

import de.nilsdruyen.mythicplus.character.enums.Role
import io.ktor.resources.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Resource("characters/profile")
data class ProfileRequest(
  val realm: String,
  val name: String,
  val fields: String,
  val region: String = "eu",
)

@Serializable
data class ProfileWebEntity(
  @SerialName("name")
  val name: String,
  @SerialName("class")
  val clazz: String,
  @SerialName("race")
  val race: String,
  @SerialName("realm")
  val realm: String,
  @SerialName("region")
  val region: String,
  @SerialName("active_spec_name")
  val spec: String,
  @SerialName("active_spec_role")
  val role: Role,
  @SerialName("thumbnail_url")
  val thumbnailUrl: String,
  @SerialName("profile_url")
  val profileUrl: String,
  @SerialName("mythic_plus_best_runs")
  val bestRuns: List<MythicPlusDungeonWebEntity>,
  @SerialName("mythic_plus_alternate_runs")
  val altRuns: List<MythicPlusDungeonWebEntity>,
  @SerialName("mythic_plus_scores_by_season")
  val scoreBySeason: List<SeasonWebEntity>,
  @SerialName("mythic_plus_recent_runs")
  val recentRuns: List<MythicPlusDungeonWebEntity>,
  val gear: GearWebEntity,
)

@Serializable
data class MythicPlusDungeonWebEntity(
  @SerialName("dungeon")
  val dungeon: String,
  @SerialName("short_name")
  val shortName: String,
  @SerialName("mythic_level")
  val level: Int,
  @SerialName("num_keystone_upgrades")
  val upgrades: Int,
  @SerialName("zone_id")
  val id: Int,
  @SerialName("score")
  val score: Double,
  @SerialName("clear_time_ms")
  val clearTimeMs: Long,
  @SerialName("par_time_ms")
  val parTimeMs: Long,
  @SerialName("affixes")
  val affixes: List<AffixWebEntity>,
  @SerialName("completed_at")
  val completedAt: String,
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