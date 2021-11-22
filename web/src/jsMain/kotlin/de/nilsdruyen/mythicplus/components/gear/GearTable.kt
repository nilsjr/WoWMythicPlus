package de.nilsdruyen.mythicplus.components.gear

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.CharacterViewModel
import de.nilsdruyen.mythicplus.character.models.Item
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.extensions.colorForItemLvl
import de.nilsdruyen.mythicplus.styles.ColorConst
import de.nilsdruyen.mythicplus.styles.ImageStyle
import de.nilsdruyen.mythicplus.styles.TableStyle
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Tr

@Composable
fun GearTable(viewModel: CharacterViewModel.GearOverview) {
  Table({
    classes(TableStyle.root)
  }) {
//    GearTableHeader()
    if (viewModel.characterList.isNotEmpty()) {
      viewModel.characterList.forEach {
        CharacterGearRow(it)
      }
    }
  }
}

@Composable
fun GearTableHeader() {
  Tr {
    Th({ colspan(2) })
    ItemSlot.values().forEach {
      Th({
        classes(TextStyle.headText)
      }) {
        Text(it.name)
      }
    }
  }
}

@Composable
fun CharacterGearRow(character: Character) {
  Tr {
    Td({
      classes(TextStyle.title)
    }) { Text(character.name) }
    Td({
      classes(TextStyle.score)
      style {
        background(ColorConst.GRAY)
      }
    }) {
      Text(character.gear.itemLevel.toString())
    }
    character.gear.items.forEach { ItemCell(it) }
  }
}

@Composable
fun ItemCell(item: Item) {
  Td({
    classes(TableStyle.cellItem)
    style {
      background(item.colorForItemLvl())
    }
  }) {
    A(href = "https://www.wowhead.com/item=${item.id}", {
//      attr("data-wh-rename-link", "false")
      attr("rel", "gems=${item.formattedGems}&bonus=${item.formattedBonus}&ilvl=${item.level}")
      target(ATarget.Blank)
    }) {
      Img(Constants.Icons.gearIcon(item.icon), item.name) {
        classes(ImageStyle.item)
      }
    }
    Div({
      classes(TextStyle.itemLevel)
    }) {
      Text("${item.level}")
    }
  }
}