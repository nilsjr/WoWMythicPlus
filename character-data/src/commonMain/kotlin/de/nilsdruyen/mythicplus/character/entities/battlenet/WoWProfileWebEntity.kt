package de.nilsdruyen.mythicplus.character.entities.battlenet

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WoWProfileWebEntity(
  val id: Long,
  @SerialName("wow_accounts") val accounts: List<AccountWebEntity>,
)

@Serializable
data class AccountWebEntity(
  val id: Long,
  val characters: List<CharacterWebEntity>,
)

@Serializable
data class CharacterWebEntity(
  val id: Long,
  val name: String,
  val level: Int,
)
