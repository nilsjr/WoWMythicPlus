package de.nilsdruyen.mythicplus.character.models

import de.nilsdruyen.mythicplus.character.utils.format

data class Character(
  val name: String,
  val score: Double,
  val hexColor: String,
  val dungeons: List<Dungeon>
)

data class Dungeon(
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