package de.nilsdruyen.mythicplus.components

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.styles.ColorConst
import de.nilsdruyen.mythicplus.styles.StyleConst
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Tr

@Composable
fun TableHeader() {
  Tr {
    Th({
      colspan(2)
      classes(TextStyle.headText)
    }) {
      Text("Char")
    }
    Constants.Dungeons.forEach {
      Th({
        colspan(2)
        classes(TextStyle.headText)
      }) {
        Text(it)
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
fun DungeonScores(dungeon: Dungeon, currentAffixes: List<Int>) {
  Score(
    dungeon.fortScore.level > 0,
    dungeon.fortScore.formattedLevel,
    currentAffixes.none { it == dungeon.fortScore.id })
  Score(
    dungeon.tyrannScore.level > 0,
    dungeon.tyrannScore.formattedLevel,
    currentAffixes.none { it == dungeon.tyrannScore.id })
}

@Composable
fun Score(everPlayed: Boolean, formattedLevel: String, currentAffix: Boolean) {
  Td({
    classes(TextStyle.level)
    style {
      if (everPlayed) {
        background(ColorConst.GREEN)
      } else {
        background(ColorConst.RED)
      }
      if (currentAffix) opacity(StyleConst.OPACITY)
    }
  }) {
    Text(formattedLevel)
  }
}