package de.nilsdruyen.mythicplus.character.extensions

import de.nilsdruyen.mythicplus.character.models.ScoreTier
import kotlin.math.absoluteValue

fun List<ScoreTier>.getColorForScore(score: Double): String {
  val colorForScore = this.map {
    it.hexColor to (it.score - score).absoluteValue
  }.minByOrNull { it.second }
  return colorForScore?.first ?: ""
}