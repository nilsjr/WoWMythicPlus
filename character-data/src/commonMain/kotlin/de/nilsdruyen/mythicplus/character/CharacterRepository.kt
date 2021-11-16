package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character

interface CharacterRepository {

  suspend fun getCharacterList(realm: String, names: List<String>): List<Character>

  suspend fun getCurrentAffixeIds(): List<Int>
}