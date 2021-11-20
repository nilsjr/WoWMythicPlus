package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants

class RaiderIoRepositoryImpl : RaiderIoRepository {

  override suspend fun getCharacterList(realm: String, names: List<String>): List<Character> =
    names.map { RaiderIoApi.getCharacter(realm, it) }.sortedByDescending { it.score }

  override suspend fun getCurrentAffixeIds(): List<Int> = RaiderIoApi.getCurrentAffixIds()

  override suspend fun getDungeons(): List<Dungeon> {
    return RaiderIoApi.getStaticData().dungeons.map { Dungeon(it.id, it.shortName) }.sortedBy {
      Constants.Dungeons.indexOf(it.shortName)
    }
  }
}