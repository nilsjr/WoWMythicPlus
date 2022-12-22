package de.nilsdruyen.mythicplus.character.enums

import de.nilsdruyen.mythicplus.character.utils.Constants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Role {
  TANK,

  @SerialName("HEALING")
  HEALER,
  DPS
}

fun Role.icon() = when (this) {
  Role.TANK -> Constants.Icons.TANK
  Role.HEALER -> Constants.Icons.HEALER
  Role.DPS -> Constants.Icons.DPS
}