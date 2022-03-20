package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaidStaticDataWebEntity(
  @SerialName("raids")
  val raids: List<RaidWebEntity>
)

@Serializable
data class RaidWebEntity(
  val id: Int,
  val slug: String,
  val name: String,
  @SerialName("short_name")
  val shortName: String,
  val icon: String,
  val encounters: List<EncounterWebEntity>
)

@Serializable
data class EncounterWebEntity(
  val id: Int,
  val slug: String,
  val name: String,
)
