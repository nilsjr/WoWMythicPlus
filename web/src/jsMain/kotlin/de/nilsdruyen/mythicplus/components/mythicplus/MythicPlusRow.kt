package de.nilsdruyen.mythicplus.components.mythicplus

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.CharacterSummary
import de.nilsdruyen.mythicplus.character.models.DungeonScore
import de.nilsdruyen.mythicplus.character.models.Score
import de.nilsdruyen.mythicplus.styles.ColorConst
import de.nilsdruyen.mythicplus.styles.StyleConst
import de.nilsdruyen.mythicplus.styles.TableStyle
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Tr

@Composable
fun CharacterMythicPlusRow(character: Character, currentAffixes: List<Int>) {
  Tr {
    Td({
      classes(TextStyle.title)
    }) { Text(character.name) }
    Td({
      classes(TextStyle.score)
      style {
        background(character.hexColor)
      }
    }) {
      Text(character.score.toString())
    }
    character.dungeons.forEach { DungeonScores(it, currentAffixes) }
  }
}

@Composable
fun CharacterMythicPlusSummaryRow(character: CharacterSummary, currentAffixes: List<Int>) {
  Tr {
    Td({
      classes(TextStyle.title)
    }) { Text("Best of all") }
    Td({
      classes(TextStyle.score)
      style {
        background(character.hexColor)
      }
    }) {
      Text(character.score.toString())
    }
    character.dungeons.forEach { DungeonScores(it, currentAffixes) }
  }
}

@Composable
fun DungeonScores(dungeon: DungeonScore, currentAffixes: List<Int>) {
  Score(dungeon.fortifiedScore, currentAffixes)
  Score(dungeon.tyrannicalScore, currentAffixes)
}

@Composable
fun Score(score: Score, currentAffixes: List<Int>) {
  Td({
    classes(TableStyle.cellLevel)
    style {
      if (score.played) {
        if (score.upgrade == 0) {
          background(ColorConst.GRAY)
        } else {
          background(ColorConst.GREEN)
        }
      } else {
        background(ColorConst.RED)
      }
      if (currentAffixes.none { it == score.id }) opacity(StyleConst.OPACITY)
    }
  }) {
    Div({
      classes(TextStyle.level)
      style {
        property("margin", "0 auto")
        backgroundColor(Color.transparent)
      }
    }) {
      Text(score.formattedLevel)
    }
    if (score.played) {
      Div({
        classes(TextStyle.levelHint)
        style {
          property("margin", "0 auto")
          backgroundColor(Color.transparent)
        }
      }) {
        Text(score.formattedCleanTime)
      }
    }
  }
}