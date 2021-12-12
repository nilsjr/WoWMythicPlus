package de.nilsdruyen.mythicplus.character.entities.battlenet

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoWebEntity(
  val id: Long,
  val battletag: String,
)
