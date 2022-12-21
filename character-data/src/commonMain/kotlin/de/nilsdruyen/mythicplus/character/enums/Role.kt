package de.nilsdruyen.mythicplus.character.enums

import de.nilsdruyen.mythicplus.character.utils.Constants

enum class Role {
  TANK,
  HEALER,
  DPS
}

fun Role.icon() = when (this) {
  Role.TANK -> Constants.Icons.TANK
  Role.HEALER -> Constants.Icons.HEALER
  Role.DPS -> Constants.Icons.DPS
}