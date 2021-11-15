package de.nilsdruyen.mythicplus.character.models

data class Character(
  val name: String,
  val score: Int,
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
) {

  val formattedLevel = StringBuilder().apply {
    append(level)
    repeat(upgrade) { append("*") }
  }.toString()
}
