package de.nilsdruyen.mythicplus.components

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.DungeonScore
import de.nilsdruyen.mythicplus.character.models.Score
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.styles.CellStyle
import de.nilsdruyen.mythicplus.styles.ColorConst
import de.nilsdruyen.mythicplus.styles.StyleConst
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Tr

@Composable
fun TableHeader(dungeons: List<Dungeon>) {
  Tr {
    Th({
      colspan(2)
      classes(TextStyle.headText)
    }) {
      Text("Char")
    }
    dungeons.forEach {
      Th({
        colspan(2)
        classes(TextStyle.headText)
        style {
          textAlign("center")
        }
      }) {
        Img(Constants.Icons.dungeonIcon(it.id), it.shortName) {
          style {
            width(30.px)
            height(30.px)
            borderRadius(50.percent)
            marginRight(8.px)
            property("vertical-align", "middle")
          }
        }
        Text(it.shortName)
      }
    }
  }
  Tr {
    Td({ colspan(2) }) {}
    repeat(Constants.Dungeons.size) {
      AffixIcon(Constants.Icons.FORTIFIED_URL, "fortified")
      AffixIcon(Constants.Icons.TYRANNICAL_URL, "tyrannical")
    }
  }
}

@Composable
fun AffixIcon(url: String, alt: String) {
  Td({
    style {
      textAlign("center")
    }
  }) {
    Img(url, alt) {
      style {
        width(25.px)
        height(25.px)
        borderRadius(50.percent)
      }
    }
  }
}

@Composable
fun CharacterRow(character: Character, currentAffixes: List<Int>) {
  Tr {
    Td({
      classes(TextStyle.title)
    }) {
      Text(character.name)
    }
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
  Score(dungeon.fortScore, currentAffixes)
  Score(dungeon.tyrannScore, currentAffixes)
}

@Composable
fun Score(score: Score, currentAffixes: List<Int>) {
  Td({
    classes(CellStyle.level)
    style {
      if (score.played) {
        background(ColorConst.GREEN)
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