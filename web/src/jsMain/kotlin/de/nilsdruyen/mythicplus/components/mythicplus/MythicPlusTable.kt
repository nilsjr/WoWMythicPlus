package de.nilsdruyen.mythicplus.components.mythicplus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.CharacterViewModel
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.extensions.generateSummary
import de.nilsdruyen.mythicplus.styles.ButtonStyle
import de.nilsdruyen.mythicplus.styles.ColorConst
import de.nilsdruyen.mythicplus.styles.ImageStyle
import de.nilsdruyen.mythicplus.styles.TableStyle
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Tr

const val ROWS = 20
const val NAME_SPAN = 3

enum class ListOrder(val textValue: String) {
  Name("name"),
  Runs("runs this week"),
  Class("class/spec"),
  Score("score")
}

@Composable
fun MythicPlusTable(viewModel: CharacterViewModel.CharacterOverview) {
  var sortBy by remember { mutableStateOf(ListOrder.Score) }
  val sortedCharachterList = remember { mutableStateOf(viewModel.characterList) }

  sortedCharachterList.value = when (sortBy) {
    ListOrder.Name -> viewModel.characterList.sortedBy(Character::name)
    ListOrder.Runs -> viewModel.characterList.sortedByDescending(Character::completedKeysThisWeek)
    ListOrder.Class -> viewModel.characterList.sortedBy { it.specialization.wowClass.name }
    ListOrder.Score -> viewModel.characterList.sortedByDescending(Character::score)
  }

  Table({
    classes(TableStyle.root)
  }) {
    MythicPlusTableHeader(sortBy, { sortBy = it }, viewModel.dungeons)
    if (sortedCharachterList.value.isNotEmpty()) {
      if (sortedCharachterList.value.size > 1) {
        CharacterMythicPlusSummaryRow(sortedCharachterList.value.generateSummary(viewModel.scoreTiers))
      }
      EmptyRow()
      sortedCharachterList.value.forEach {
        CharacterMythicPlusRow(it, viewModel.currentAffixIds)
      }
    }
  }
  P({ classes(TextStyle.hint) }) {
    Text("Number behind names = Dungeons played this week")
  }
}

@Composable
fun OrderButton(order: ListOrder, activeOrderButton: ListOrder, changeOrder: (ListOrder) -> Unit) {
  Th {
    Button(
      attrs = {
        classes(if (order == activeOrderButton) ButtonStyle.orderActive else ButtonStyle.order)
        onClick { changeOrder(order) }
      }
    ) {
      Text(order.textValue)
    }
  }
}

@Composable
fun EmptyRow() {
  Tr({
    style {
      background(ColorConst.GRAY)
    }
  }) { Td({ colspan(ROWS) }) {} }
}

@Composable
fun MythicPlusTableHeader(activeOrder: ListOrder, changeOrder: (ListOrder) -> Unit, dungeons: List<Dungeon>) {
  Tr {
    Th({ colspan(NAME_SPAN + 1) })
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
    OrderButton(ListOrder.Name, activeOrder, changeOrder)
    OrderButton(ListOrder.Runs, activeOrder, changeOrder)
    OrderButton(ListOrder.Class, activeOrder, changeOrder)
    OrderButton(ListOrder.Score, activeOrder, changeOrder)
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