package de.nilsdruyen.mythicplus.extensions

import de.nilsdruyen.mythicplus.character.extensions.getColorForScore
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.CharacterSummary
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.DungeonScore
import de.nilsdruyen.mythicplus.character.models.Item
import de.nilsdruyen.mythicplus.character.models.Score
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.styles.ColorConst
import kotlin.math.roundToInt

fun Item.colorForItemLvl(): String = when {
  isLegendary -> ColorConst.Gear.ORANGE
  level > 184 -> if (level==230) ColorConst.Gear.BLUE else ColorConst.Gear.PURPLE
  level > 130 -> ColorConst.Gear.BLUE
  else -> ColorConst.Gear.GREEN
}

fun List<Character>.generateSummary(scoreTiers: List<ScoreTier>, dungeons: List<Dungeon>): CharacterSummary {
  val dungeonSummary = dungeons.map { dungeon ->
    this.map { char ->
      char.dungeons.first { it.shortName==dungeon.shortName }
    } to dungeon
  }.map { scorePair ->
    val (scores, dungeon) = scorePair
    val bestTyrannical = scores.maxByOrNull { it.tyrannicalScore.score }?.tyrannicalScore
    val bestFortified = scores.maxByOrNull { it.fortifiedScore.score }?.fortifiedScore
    DungeonScore(
      shortName = dungeon.shortName,
      slug = dungeon.slug,
      tyrannicalScore = bestTyrannical ?: Score.empty(Constants.TYRANNICAL),
      fortifiedScore = bestFortified ?: Score.empty(Constants.FORTIFIED)
    )
  }.sortedBy { it.slug }

  val score = dungeonSummary.sumOf { it.score }
  val colorForScore = scoreTiers.getColorForScore(score)

  return CharacterSummary(
    score.roundToInt().toDouble(),
    colorForScore,
    dungeonSummary
  )
}