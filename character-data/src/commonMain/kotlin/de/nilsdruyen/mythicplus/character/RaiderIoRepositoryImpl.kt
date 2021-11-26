package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.apis.RaiderIoApi
import de.nilsdruyen.mythicplus.character.entities.MythicPlusDungeonWebEntity
import de.nilsdruyen.mythicplus.character.enums.Affliction
import de.nilsdruyen.mythicplus.character.enums.Arcane
import de.nilsdruyen.mythicplus.character.enums.Arms
import de.nilsdruyen.mythicplus.character.enums.Assassination
import de.nilsdruyen.mythicplus.character.enums.Balance
import de.nilsdruyen.mythicplus.character.enums.BeastMastery
import de.nilsdruyen.mythicplus.character.enums.Blood
import de.nilsdruyen.mythicplus.character.enums.Brewmaster
import de.nilsdruyen.mythicplus.character.enums.DeathKnightSpec
import de.nilsdruyen.mythicplus.character.enums.Demonology
import de.nilsdruyen.mythicplus.character.enums.Destruction
import de.nilsdruyen.mythicplus.character.enums.Discipline
import de.nilsdruyen.mythicplus.character.enums.DruidSpec
import de.nilsdruyen.mythicplus.character.enums.Elemental
import de.nilsdruyen.mythicplus.character.enums.Enhancement
import de.nilsdruyen.mythicplus.character.enums.Feral
import de.nilsdruyen.mythicplus.character.enums.Fire
import de.nilsdruyen.mythicplus.character.enums.Fury
import de.nilsdruyen.mythicplus.character.enums.Guardian
import de.nilsdruyen.mythicplus.character.enums.Havoc
import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.enums.MageSpec
import de.nilsdruyen.mythicplus.character.enums.Marksmanship
import de.nilsdruyen.mythicplus.character.enums.Mistweaver
import de.nilsdruyen.mythicplus.character.enums.Outlaw
import de.nilsdruyen.mythicplus.character.enums.PaladinSpec
import de.nilsdruyen.mythicplus.character.enums.PriestSpec
import de.nilsdruyen.mythicplus.character.enums.Retribution
import de.nilsdruyen.mythicplus.character.enums.Shadow
import de.nilsdruyen.mythicplus.character.enums.ShamanSpec
import de.nilsdruyen.mythicplus.character.enums.Specialization
import de.nilsdruyen.mythicplus.character.enums.Subtlety
import de.nilsdruyen.mythicplus.character.enums.Survival
import de.nilsdruyen.mythicplus.character.enums.Unholy
import de.nilsdruyen.mythicplus.character.enums.Vengeance
import de.nilsdruyen.mythicplus.character.enums.WarriorSpec
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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class RaiderIoRepositoryImpl : RaiderIoRepository {

  override suspend fun getCharacterList(charList: List<InputCharacter>, scoreTiers: List<ScoreTier>): List<Character> {
    return charList.map { getCharacter(it, scoreTiers, getCurrentPeriod()) }
  }

  override suspend fun getCurrentAffixeIds(): List<Int> = RaiderIoApi.getCurrentAffixIds()

  override suspend fun getDungeons(): List<Dungeon> {
    return RaiderIoApi.getStaticData().dungeons.map { Dungeon(it.id, it.shortName) }.sortedBy {
      Constants.Dungeons.indexOf(it.shortName)
    }
  }

  override suspend fun getScoreTiers(): List<ScoreTier> = RaiderIoApi.getScoreTiers()

  private suspend fun getCurrentPeriod(): LocalDateTime {
    val period = RaiderIoApi.getCurrentPeriod().periods.firstOrNull { it.region == "eu" }
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return period?.let {
      listOf(it.previous, it.current, it.next).first { item -> item.isCurrentWeek(now) }.startDate
    } ?: now
  }

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
    val specList: List<Specialization> = when (clazz) {
      WoWClass.DEATH_KNIGHT -> listOf(DeathKnightSpec.Frost, Blood, Unholy)
      WoWClass.DEMON_HUNTER -> listOf(Havoc, Vengeance)
      WoWClass.DRUID -> listOf(DruidSpec.Restoration, Balance, Feral, Guardian)
      WoWClass.HUNTER -> listOf(BeastMastery, Marksmanship, Survival)
      WoWClass.MAGE -> listOf(MageSpec.Frost, Fire, Arcane)
      WoWClass.MONK -> listOf(Brewmaster, Mistweaver, Windwalker)
      WoWClass.PALADIN -> listOf(PaladinSpec.Holy, Retribution, PaladinSpec.Protection)
      WoWClass.PRIEST -> listOf(PriestSpec.Holy, Discipline, Shadow)
      WoWClass.ROGUE -> listOf(Assassination, Outlaw, Subtlety)
      WoWClass.SHAMAN -> listOf(ShamanSpec.Restoration, Elemental, Enhancement)
      WoWClass.WARLOCK -> listOf(Affliction, Demonology, Destruction)
      WoWClass.WARRIOR -> listOf(WarriorSpec.Protection, Fury, Arms)
    }
    return specList.firstOrNull { it.name == specializationRaw.lowercase() } ?: Blood
  }
}