package de.nilsdruyen.mythicplus.character.utils

import de.nilsdruyen.mythicplus.character.exceptions.InvalidArgumentsException
import de.nilsdruyen.mythicplus.character.models.InputCharacter

fun String.convertToCharacterList(realm: String): List<InputCharacter> {
  return if (this.isNotEmpty()) {
    if (this.contains(",")) {
      this.split(",")
    } else {
      listOf(this)
    }.map {
      InputCharacter(it, realm)
    }
  } else {
      throw InvalidArgumentsException
  }
}

fun String.convertToCharacterList(): List<InputCharacter> {
  return if (this.isNotEmpty()) {
    try {
      if (this.contains(",")) {
        this.split(",").map {
          convertToCharacter(it)
        }
      } else {
        listOf(convertToCharacter(this))
      }
    } catch (e: Throwable) {
      throw InvalidArgumentsException
    }
  } else {
      emptyList()
  }
}

fun convertToCharacter(charRaw: String): InputCharacter {
  return if (charRaw.contains(":")) {
    val splittedChar = charRaw.split(":", limit = 2)
    InputCharacter(splittedChar[0], splittedChar[1])
  } else {
      throw InvalidArgumentsException
  }
}