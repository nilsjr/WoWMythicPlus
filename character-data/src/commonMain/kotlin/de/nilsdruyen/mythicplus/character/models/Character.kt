package de.nilsdruyen.mythicplus.character.models

import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.utils.format

data class Character(
  val name: String,
  val score: Double,
  val hexColor: String,
  val dungeons: List<DungeonScore>,
  val gear: Gear,
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
  val formattedBonus = gems.joinToString(",")
}

data class DominationShard(
  val quality: Int,
  val name: String,
  val icon: String,
  val id: Int,
)

data class DungeonScore(
  val shortName: String,
  val tyrannScore: Score,
  val fortScore: Score,
)

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