package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character

class CharacterRepositoryImpl : CharacterRepository {

  override suspend fun getCharacterList(realm: String, names: List<String>): List<Character> =
    names.map { RaiderIoApi.getCharacter(realm, it) }.sortedByDescending { it.score }

  override suspend fun getCurrentAffixeIds(): List<Int> = RaiderIoApi.getCurrentAffixIds()
}