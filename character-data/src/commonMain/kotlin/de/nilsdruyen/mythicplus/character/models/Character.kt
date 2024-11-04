package de.nilsdruyen.mythicplus.character.models

import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.enums.Specialization
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.character.utils.format

data class Character(
  val name: String,
  val realm: String,
  val specialization: Specialization,
  val profileUrl: String,
  val score: Double,
  val scoreColorHex: String,
  val dungeons: List<DungeonScore>,
  val gear: Gear,
  val completedKeysThisWeek: Int
) {

  fun iconForSpec() = Constants.Icons.clazzSpecIcon(specialization)
  fun iconForClazz() = Constants.Icons.clazzIcon(specialization.wowClass)

  val invalid = score == -1.0
}

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
  val slug: String,
  val bestScore: Score,
)

data class Score(
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

    fun empty(): Score = Score(0.0, 0, 0, 0)
  }
}