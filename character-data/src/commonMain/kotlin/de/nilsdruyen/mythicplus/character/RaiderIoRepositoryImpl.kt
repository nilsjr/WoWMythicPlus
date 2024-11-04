package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.annotations.Inject
import de.nilsdruyen.mythicplus.character.apis.RaiderIoApi
import de.nilsdruyen.mythicplus.character.entities.MythicPlusDungeonWebEntity
import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.enums.toSlot
import de.nilsdruyen.mythicplus.character.extensions.getColorForScore
import de.nilsdruyen.mythicplus.character.extensions.getSpec
import de.nilsdruyen.mythicplus.character.extensions.map
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.DominationShard
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.DungeonScore
import de.nilsdruyen.mythicplus.character.models.Encounter
import de.nilsdruyen.mythicplus.character.models.Gear
import de.nilsdruyen.mythicplus.character.models.InputCharacter
import de.nilsdruyen.mythicplus.character.models.Item
import de.nilsdruyen.mythicplus.character.models.Raid
import de.nilsdruyen.mythicplus.character.models.Score
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import de.nilsdruyen.mythicplus.character.utils.Constants
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

class RaiderIoRepositoryImpl @Inject constructor(
  private val client: RaiderIoApi
) : RaiderIoRepository {

  override suspend fun getCharacterList(charList: List<InputCharacter>, dungeons: List<Dungeon>): List<Character> {
    val scoreTiers = getScoreTiers()
    val currentPeriod = getCurrentPeriod()
    return charList.map { getCharacter(it, scoreTiers, currentPeriod, dungeons) }
  }

  override suspend fun getDungeons(): List<Dungeon> {
    return client.getStaticData().seasons
      .first { it.slug == Constants.SEASON_SLUG }.dungeons
      .map { Dungeon(it.id, it.shortName, it.slug) }
      .sortedBy { it.slug }
  }

  override suspend fun getScoreTiers(): List<ScoreTier> = client.getScoreTiers()

  private suspend fun getCurrentPeriod(): LocalDateTime {
    val period = client.getCurrentPeriod().periods.firstOrNull { it.region == "eu" }
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return period?.let {
      listOf(it.previous, it.current, it.next).first { item -> item.isCurrentWeek(now) }.startDate
    } ?: now
  }

  private suspend fun getCharacter(
    char: InputCharacter,
    tiers: List<ScoreTier>,
    currentPeriod: LocalDateTime,
    dungeons: List<Dungeon>,
  ): Character {
    val entity = client.getCharacter(char.realm, char.name)
    val charScore = entity.scoreBySeason.firstOrNull()?.scores?.all ?: -1.0
    val allRuns = entity.bestRuns
    val list = dungeons.map { dungeon ->
      val filteredDungeons = allRuns.filter { it.shortName == dungeon.shortName }
      val bestScore = filteredDungeons.getBestScore()
      DungeonScore(dungeon.shortName, dungeon.slug, bestScore)
    }.sortedBy { it.slug }

    val items = entity.gear.items.map {
      val (key, data) = it
      Item(
        id = data.id,
        slot = key.toSlot() ?: ItemSlot.Neck,
        name = data.name,
        level = data.itemLevel,
        icon = data.icon,
        isLegendary = data.isLegendary,
        dominationShards = data.dominationShards.map { shard ->
          DominationShard(shard.quality, shard.name, shard.icon, shard.itemId)
        },
        gems = data.gems,
        bonuses = data.bonuses,
      )
    }
    val gear = Gear(entity.gear.iLvlEquipped.roundToInt(), items)

    val keysThisWeek = entity.recentRuns.filter {
      val dateTime = Instant.parse(it.completedAt).toLocalDateTime(TimeZone.currentSystemDefault())
      dateTime > currentPeriod
    }.size

    return Character(
      name = entity.name,
      realm = entity.realm,
      specialization = entity.clazz.map().getSpec(entity.spec),
      profileUrl = entity.profileUrl,
      score = charScore,
      scoreColorHex = tiers.getColorForScore(charScore),
      dungeons = list,
      gear = gear,
      completedKeysThisWeek = keysThisWeek
    )
  }

  private fun List<MythicPlusDungeonWebEntity>.getBestScore(): Score {
    val dungeon = maxByOrNull { it.score }
    return if (dungeon == null) {
      println("no run found")
      Score.empty()
    } else {
      Score(dungeon.score, dungeon.level, dungeon.upgrades, dungeon.clearTimeMs)
    }
  }

  override suspend fun getCurrentRaid(): Raid {
    val raidEntity = client.getRaidStaticData()
    return with(raidEntity.raids.last()) {
      Raid(
        id = id,
        slug = slug,
        name = name,
        shortName = shortName,
        icon = icon,
        encounters = encounters.map {
          Encounter(id, slug, name)
        }
      )
    }
  }
}