package de.nilsdruyen.mythicplus.character.extensions

import de.nilsdruyen.mythicplus.character.enums.WoWClass

fun WoWClass.toIdentifier(): String = this.name.lowercase().replace("_", "-")