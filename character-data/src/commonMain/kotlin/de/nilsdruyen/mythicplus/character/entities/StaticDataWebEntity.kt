package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaticDataWebEntity(
  val dungeons: List<DungeonInfoWebEntity>
)

@Serializable
data class DungeonInfoWebEntity(
  val id: Int,
  @SerialName("short_name") val shortName: String,
  val slug: String
)
