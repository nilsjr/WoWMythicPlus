package de.nilsdruyen.mythicplus.character.models

import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.utils.format
import kotlin.math.max
import kotlin.math.min

data class Character(
  val name: String,
  val score: Double,
  val hexColor: String,
  val dungeons: List<DungeonScore>,
  val gear: Gear,
)

// TODO: 23.11.21 sealed class for list
data class CharacterSummary(
  val score: Double,
  val hexColor: String,
  val dungeons: List<DungeonScore>,
)

data class Gear(
  val itemLevel: Int,
  val items: List<Item>,
)

data class Item(
  val id: Int,
  val slot: ItemSlot,
  val name: String,
  val level: Int,
  val icon: String,
  val isLegendary: Boolean,
  val dominationShards: List<DominationShard>,
  val gems: List<Int>,
  val bonuses: List<Int>,
) {

  val formattedGems = gems.joinToString(",")
  val formattedBonus = bonuses.joinToString(":")
}

data class DominationShard(
  val quality: Int,
  val name: String,
  val icon: String,
  val id: Int,
)

data class DungeonScore(
  val shortName: String,
  val tyrannicalScore: Score,
  val fortifiedScore: Score,
) {

  val score =
    max(tyrannicalScore.score, fortifiedScore.score) * 1.5 + min(tyrannicalScore.score, fortifiedScore.score) * 0.5
}

data class Score(
  val id: Int,
  val score: Double,
  val level: Int,
  val upgrade: Int,
  val cleanTimeMs: Long,
) {

  val formattedLevel = StringBuilder().apply {
    append(level)
    repeat(upgrade) { append("*") }
  }.toString()

  val formattedCleanTime = cleanTimeMs.format()
  val played = score > 0

  companion object {

    fun empty(type: Int): Score = Score(type, 0.0, 0, 0, 0)
  }
}