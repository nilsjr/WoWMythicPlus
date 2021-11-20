package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon

interface RaiderIoRepository {

  suspend fun getCharacterList(realm: String, names: List<String>): List<Character>

  suspend fun getCurrentAffixeIds(): List<Int>

  suspend fun getDungeons(): List<Dungeon>
}