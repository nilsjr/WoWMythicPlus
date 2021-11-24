package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.apis.RaiderIoApi
import de.nilsdruyen.mythicplus.character.entities.MythicPlusDungeonWebEntity
import de.nilsdruyen.mythicplus.character.enums.Blood
import de.nilsdruyen.mythicplus.character.enums.Brewmaster
import de.nilsdruyen.mythicplus.character.enums.DeathKnightSpec
import de.nilsdruyen.mythicplus.character.enums.Elemental
import de.nilsdruyen.mythicplus.character.enums.Enhancement
import de.nilsdruyen.mythicplus.character.enums.Havoc
import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.enums.Mistweaver
import de.nilsdruyen.mythicplus.character.enums.PaladinSpec
import de.nilsdruyen.mythicplus.character.enums.Retribution
import de.nilsdruyen.mythicplus.character.enums.Role
import de.nilsdruyen.mythicplus.character.enums.ShamanSpec
import de.nilsdruyen.mythicplus.character.enums.Specialization
import de.nilsdruyen.mythicplus.character.enums.Unholy
import de.nilsdruyen.mythicplus.character.enums.Windwalker
import de.nilsdruyen.mythicplus.character.enums.WoWClass
import de.nilsdruyen.mythicplus.character.enums.toSlot
import de.nilsdruyen.mythicplus.character.extensions.getColorForScore
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.DominationShard
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.DungeonScore
import de.nilsdruyen.mythicplus.character.models.Gear
import de.nilsdruyen.mythicplus.character.models.InputCharacter
import de.nilsdruyen.mythicplus.character.models.Item
import de.nilsdruyen.mythicplus.character.models.Score
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import de.nilsdruyen.mythicplus.character.utils.Constants
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class RaiderIoRepositoryImpl : RaiderIoRepository {

  override suspend fun getCharacterList(charList: List<InputCharacter>, scoreTiers: List<ScoreTier>): List<Character> {
    val currentPeriod = RaiderIoApi.getCurrentPeriod()
    return charList.map { getCharacter(it, scoreTiers, currentPeriod) }.sortedByDescending { it.score }
  }

  override suspend fun getCurrentAffixeIds(): List<Int> = RaiderIoApi.getCurrentAffixIds()

  override suspend fun getDungeons(): List<Dungeon> {
    return RaiderIoApi.getStaticData().dungeons.map { Dungeon(it.id, it.shortName) }.sortedBy {
      Constants.Dungeons.indexOf(it.shortName)
    }
  }

  override suspend fun getScoreTiers(): List<ScoreTier> = RaiderIoApi.getScoreTiers()

  private suspend fun getCharacter(
    char: InputCharacter,
    tiers: List<ScoreTier>,
    currentPeriod: LocalDateTime,
  ): Character {
    val entity = RaiderIoApi.getCharacter(char.realm, char.name)
    val charScore = entity.scoreBySeason.first().scores.all
    val list = Constants.Dungeons.map { dungeon ->
      val filteredDungeons = (entity.bestRuns + entity.altRuns).filter { it.shortName == dungeon }
      val tyrannical = filteredDungeons.mapToScore(Constants.TYRANNICAL)
      val fortified = filteredDungeons.mapToScore(Constants.FORTIFIED)
      DungeonScore(dungeon, tyrannical, fortified)
    }

    val items = entity.gear.items.map {
      val (key, data) = it
      Item(
        data.id,
        key.toSlot() ?: ItemSlot.Neck,
        data.name,
        data.itemLevel,
        data.icon,
        data.isLegendary,
        data.dominationShards.map { shard ->
          DominationShard(shard.quality, shard.name, shard.icon, shard.itemId)
        },
        data.gems,
        data.bonuses,
      )
    }
    val gear = Gear(entity.gear.iLvlEquipped, items)

    val keysThisWeek = entity.recentRuns.filter {
      val dateTime = it.completedAt.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())
      dateTime > currentPeriod
    }.size

    return Character(
      name = entity.name,
      realm = entity.realm,
      specialization = getSpecForClass(entity.clazz, entity.spec),
      profileUrl = entity.profileUrl,
      score = charScore,
      scoreColorHex = tiers.getColorForScore(charScore),
      dungeons = list,
      gear = gear,
      completedKeysThisWeek = keysThisWeek
    )
  }

  private fun List<MythicPlusDungeonWebEntity>.mapToScore(type: Int): Score {
    val dungeon = this.firstOrNull { it.affixes.any { affix -> affix.id == type } }
    return if (dungeon == null) {
      Score.empty(type)
    } else {
      Score(type, dungeon.score, dungeon.level, dungeon.upgrades, dungeon.clearTimeMs)
    }
  }

  private fun getSpecForClass(clazz: WoWClass, specializationRaw: String): Specialization {
    println("check ${clazz.name}")
    println("with $specializationRaw")
    val specList: List<Specialization> = when (clazz) {
      WoWClass.DEATH_KNIGHT -> listOf(DeathKnightSpec.Frost, Blood, Unholy)
      WoWClass.PALADIN -> listOf(PaladinSpec.Holy, Retribution, PaladinSpec.Protection)
//      WoWClass.PALADIN -> listOf(PaladinSpec.Holy, Retribution, PaladinSpec.Protection)
      WoWClass.MONK -> listOf(Brewmaster, Mistweaver, Windwalker)
//      WoWClass.PALADIN -> listOf(PaladinSpec.Holy, Retribution, PaladinSpec.Protection)
//      WoWClass.PALADIN -> listOf(PaladinSpec.Holy, Retribution, PaladinSpec.Protection)
//      WoWClass.PALADIN -> listOf(PaladinSpec.Holy, Retribution, PaladinSpec.Protection)
//      WoWClass.PALADIN -> listOf(PaladinSpec.Holy, Retribution, PaladinSpec.Protection)
      WoWClass.SHAMAN -> listOf(ShamanSpec.Restoration, Elemental, Enhancement)
      else -> listOf(Havoc)
    }
    println("found: $specList")
    return specList.firstOrNull { it.name == specializationRaw.lowercase() } ?: Blood
  }
}