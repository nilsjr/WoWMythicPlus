package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.Serializable

@Serializable
data class ScoreTierEntity(
  val score: Int,
  val rgbHex: String,
)