package de.nilsdruyen.mythicplus.pages.raid

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.models.CharacterOverview
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun RaidPage(overview: CharacterOverview) {
  Div({
    style {
      width(100.percent)
      height(100.percent)
      display(DisplayStyle.Flex)
      alignItems("center")
    }
  }) {
    Text("coming soon...")
    Br { }
    Text("${overview.raid?.name} coming soon...")
  }
}