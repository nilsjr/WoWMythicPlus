package de.nilsdruyen.mythicplus.components

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.models.CharacterViewModel
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Tr

@Composable
fun CharacterTable(viewModel: CharacterViewModel.MythicPlusOverview) {
  Table({
    style {
      property("border-spacing", "8px")
    }
  }) {
    TableHeader(viewModel.dungeons)
    if (viewModel.characterList.isNotEmpty()) {
      viewModel.characterList.forEach {
        CharacterRow(it, viewModel.currentAffixIds)
      }
    }
  }
}

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