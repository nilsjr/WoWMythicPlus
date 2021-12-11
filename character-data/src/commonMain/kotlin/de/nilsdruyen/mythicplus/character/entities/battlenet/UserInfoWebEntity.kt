package de.nilsdruyen.mythicplus.character.entities.battlenet

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoWebEntity(
  val id: String,
  val battletag: String,
)
