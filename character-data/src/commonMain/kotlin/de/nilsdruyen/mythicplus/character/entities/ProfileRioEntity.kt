package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRioEntity(
  @SerialName("name") val name: String,
  @SerialName("class") val clazz: String,
  @SerialName("mythic_plus_best_runs") val bestRuns: List<MythicPlusDungeonEntity>,
  @SerialName("mythic_plus_alternate_runs") val altRuns: List<MythicPlusDungeonEntity>,
)

@Serializable
data class MythicPlusDungeonEntity(
  @SerialName("dungeon") val dungeon: String,
  @SerialName("short_name") val shortName: String,
  @SerialName("mythic_level") val level: Int,
  @SerialName("num_keystone_upgrades") val upgrades: Int,
  @SerialName("zone_id") val id: Int,
  @SerialName("score") val score: Double,
  @SerialName("affixes") val affixes: List<AffixEntity>
)

@Serializable
data class AffixEntity(
  val id: Int
)


