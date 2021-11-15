package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AffixesWebEntity(
  @SerialName("affix_details") val details: List<AffixDetailsWebEntity>,
)

@Serializable
data class AffixDetailsWebEntity(
  val id: Int,
)