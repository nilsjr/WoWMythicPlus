package de.nilsdruyen.mythicplus.components

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.styles.ColorConst
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
      style {
        textAlign("start")
      }
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
    }) {
      Text(character.score.toString())
    }
    character.dungeons.forEach { DungeonScore(it, currentAffixes) }
  }
}

@Composable
fun DungeonScore(dungeon: Dungeon, currentAffixes: List<Int>) {
  Td({
    style {
      textAlign("center")
      if (dungeon.fortScore.level > 0) {
        background(ColorConst.GREEN)
      } else {
        background(ColorConst.RED)
      }
      if (currentAffixes.none { it == dungeon.fortScore.id }) {
        opacity(0.5)
      }
      classes(TextStyle.level)
      property("color", dungeon.fortScore.hexColor)
    }
  }) {
    Text(dungeon.fortScore.formattedLevel)
  }
  Td({
    style {
      textAlign("center")
      if (dungeon.tyrannScore.level > 0) {
        background(ColorConst.GREEN)
      } else {
        background(ColorConst.RED)
      }
      if (currentAffixes.none { it == dungeon.tyrannScore.id }) {
        opacity(0.5)
      }
      classes(TextStyle.level)
      property("color", dungeon.tyrannScore.hexColor)
    }
  }) {
    Text(dungeon.tyrannScore.formattedLevel)
  }
}