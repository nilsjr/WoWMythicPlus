package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.InputCharacter

interface RaiderIoRepository {

  suspend fun getCharacterList(charList: List<InputCharacter>): List<Character>

  suspend fun getCurrentAffixeIds(): List<Int>

  suspend fun getDungeons(): List<Dungeon>
}