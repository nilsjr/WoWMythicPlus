package de.nilsdruyen.mythicplus.components.mythicplus

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.models.CharacterViewModel
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.styles.ImageStyle
import de.nilsdruyen.mythicplus.styles.TableStyle
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Tr

@Composable
fun MythicPlusTable(viewModel: CharacterViewModel.MythicPlusOverview) {
  Table({
    classes(TableStyle.root)
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
    Th({ colspan(2) })
    dungeons.forEach {
      Th({
        colspan(2)
        classes(TextStyle.headText)
      }) {
        Img(Constants.Icons.dungeonIcon(it.id), it.shortName) {
          classes(ImageStyle.dungeon)
        }
        Text(it.shortName)
      }
    }
  }
  Tr {
    Td({ colspan(2) })
    repeat(Constants.Dungeons.size) {
      AffixIcon(Constants.Icons.FORTIFIED_URL, "fortified")
      AffixIcon(Constants.Icons.TYRANNICAL_URL, "tyrannical")
    }
  }
}

@Composable
fun AffixIcon(url: String, alt: String) {
  Td({
    classes(TableStyle.cellImage)
  }) {
    Img(url, alt) { classes(ImageStyle.affix) }
  }
}