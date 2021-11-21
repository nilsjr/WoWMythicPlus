package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaticDataEntity(
  val dungeons: List<DungeonInfoEntity>
)

@Serializable
data class DungeonInfoEntity(
  val id: Int,
  @SerialName("short_name") val shortName: String,
  val slug: String
)
