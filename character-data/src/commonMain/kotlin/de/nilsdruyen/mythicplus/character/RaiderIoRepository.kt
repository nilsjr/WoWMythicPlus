package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.InputCharacter
import de.nilsdruyen.mythicplus.character.models.ScoreTier

interface RaiderIoRepository {

  suspend fun getCharacterList(charList: List<InputCharacter>, dungeons: List<Dungeon>): List<Character>

  suspend fun getCurrentAffixeIds(): List<Int>

  suspend fun getDungeons(): List<Dungeon>

  suspend fun getScoreTiers(): List<ScoreTier>
}