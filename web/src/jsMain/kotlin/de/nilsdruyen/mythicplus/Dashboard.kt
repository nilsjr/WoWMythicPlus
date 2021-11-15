package de.nilsdruyen.mythicplus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.RaiderIoApi
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.styles.ColorConst
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Tr

@Composable
fun Dashboard() {
  var characters by remember { mutableStateOf(emptyList<Character>()) }
  var currentAffixes by remember { mutableStateOf(emptyList<Int>()) }

  LaunchedEffect(Unit) {
    val chars = listOf("Twilliam", "Harazz", "Landolph", "Landolf", "Bumsbüffel", "Docsnyder").map {
      RaiderIoApi.getCharacter(it)
    }
    characters = chars.sortedByDescending { it.score }
    currentAffixes = RaiderIoApi.getCurrentAffixIds()
  }

  Table({
    style {
      property("border-spacing", "8px")
    }
  }) {
    TableHeader()
    characters.forEach {
      CharacterRow(it, currentAffixes)
    }
  }
}

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
      style {
        textAlign("end")
      }
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
      classes(TextStyle.score)
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
      classes(TextStyle.score)
    }
  }) {
    Text(dungeon.tyrannScore.formattedLevel)
  }
}