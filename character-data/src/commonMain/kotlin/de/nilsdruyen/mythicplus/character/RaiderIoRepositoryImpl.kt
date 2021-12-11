package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.annotations.Inject
import de.nilsdruyen.mythicplus.character.apis.WebApi
import de.nilsdruyen.mythicplus.character.entities.MythicPlusDungeonWebEntity
import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.enums.toSlot
import de.nilsdruyen.mythicplus.character.extensions.getColorForScore
import de.nilsdruyen.mythicplus.character.extensions.getSpec
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

class RaiderIoRepositoryImpl @Inject constructor(
  val api: WebApi
) : RaiderIoRepository {

  override suspend fun getCharacterList(charList: List<InputCharacter>): List<Character> {
    val scoreTiers = getScoreTiers()
    val currentPeriod = getCurrentPeriod()
    return charList.map { getCharacter(it, scoreTiers, currentPeriod) }
  }

  override suspend fun getCharacter(char: InputCharacter): Character {
    val scoreTiers = getScoreTiers()
    val currentPeriod = getCurrentPeriod()
    return getCharacter(char, scoreTiers, currentPeriod)
  }

  override suspend fun getCurrentAffixeIds(): List<Int> = api.getCurrentAffixIds()

  override suspend fun getDungeons(): List<Dungeon> {
    return api.getStaticData().dungeons.map { Dungeon(it.id, it.shortName) }.sortedBy {
      Constants.Dungeons.indexOf(it.shortName)
    }
  }

  override suspend fun getScoreTiers(): List<ScoreTier> = api.getScoreTiers()

  private suspend fun getCurrentPeriod(): LocalDateTime {
    val period = api.getCurrentPeriod().periods.firstOrNull { it.region=="eu" }
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
    val entity = api.getCharacter(char.realm, char.name)
    val charScore = entity.scoreBySeason.first().scores.all
    val list = Constants.Dungeons.map { dungeon ->
      val filteredDungeons = (entity.bestRuns + entity.altRuns).filter { it.shortName==dungeon }
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
      specialization = entity.clazz.getSpec(entity.spec),
      profileUrl = entity.profileUrl,
      score = charScore,
      scoreColorHex = tiers.getColorForScore(charScore),
      dungeons = list,
      gear = gear,
      completedKeysThisWeek = keysThisWeek
    )
  }

  private fun List<MythicPlusDungeonWebEntity>.mapToScore(type: Int): Score {
    val dungeon = this.firstOrNull { it.affixes.any { affix -> affix.id==type } }
    return if (dungeon==null) {
      Score.empty(type)
    } else {
      Score(type, dungeon.score, dungeon.level, dungeon.upgrades, dungeon.clearTimeMs)
    }
  }
}