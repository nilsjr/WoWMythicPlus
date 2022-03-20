package de.nilsdruyen.mythicplus.pages.mythicplus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import de.nilsdruyen.mythicplus.character.models.CharacterOverview
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.extensions.generateSummary
import de.nilsdruyen.mythicplus.styles.ButtonStyle
import de.nilsdruyen.mythicplus.styles.ColorConst
import de.nilsdruyen.mythicplus.styles.ImageStyle
import de.nilsdruyen.mythicplus.styles.TableStyle
import de.nilsdruyen.mythicplus.styles.TextStyle
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Tr

@Composable
fun MythicPlusPage(overview: CharacterOverview) {
  val coroutineScope = rememberCoroutineScope()
  val viewModel = remember { MythicPlusViewModel(overview, coroutineScope) }
  val state = viewModel.state.collectAsState()

  MythicPlusContent(state.value) {
    coroutineScope.launch {
      viewModel.intent.send(MythicPlusIntent.Reorder(it))
    }
  }
}

@Composable
fun MythicPlusContent(state: MythicPlusState, changeOrder: (ListOrder) -> Unit) {
  Table({ classes(TableStyle.parent) }) {
    MythicPlusTableHeader(state.order, changeOrder, state.dungeons)
    if (state.characterList.isNotEmpty()) {
      if (state.characterList.size > 1) {
        CharacterMythicPlusSummaryRow(state.characterList.generateSummary(state.scoreTiers, state.dungeons))
      }
      EmptyRow()
      state.characterList.forEach {
        CharacterMythicPlusRow(it, state.currentAffixIds)
      }
    }
  }
}

const val ROWS = 20
const val NAME_SPAN = 3

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
    repeat(dungeons.size) {
      AffixIcon(Constants.Icons.FORTIFIED_URL, "fortified")
      AffixIcon(Constants.Icons.TYRANNICAL_URL, "tyrannical")
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
fun OrderButton(order: ListOrder, activeOrderButton: ListOrder, changeOrder: (ListOrder) -> Unit) {
  Th {
    Button(
      attrs = {
        classes(if (order==activeOrderButton) ButtonStyle.orderActive else ButtonStyle.order)
        onClick { changeOrder(order) }
      }
    ) {
      Text(order.textValue)
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