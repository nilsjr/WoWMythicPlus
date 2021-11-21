package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.Serializable

@Serializable
data class ScoreTierWebEntity(
  val score: Int,
  val rgbHex: String,
)