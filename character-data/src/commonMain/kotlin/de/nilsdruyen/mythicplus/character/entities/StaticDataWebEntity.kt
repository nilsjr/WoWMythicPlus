package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaticDataWebEntity(
  val seasons: List<SeasonInfoWebEntity>,
  val dungeons: List<DungeonInfoWebEntity>,
)

@Serializable
data class SeasonInfoWebEntity(
  val slug: String,
  val name: String,
  val dungeons: List<DungeonInfoWebEntity>,
)

@Serializable
data class DungeonInfoWebEntity(
  val id: Int,
  @SerialName("challenge_mode_id")
  val modeId: Int,
  @SerialName("short_name")
  val shortName: String,
  @SerialName("slug")
  val slug: String,
)