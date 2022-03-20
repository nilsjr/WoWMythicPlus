package de.nilsdruyen.mythicplus.pages.common

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.components.BattleNetLogo
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

@Composable
fun LoadingPage() {
  Div({
    style {
      height(100.percent)
      display(DisplayStyle.Flex)
      alignItems("center")
    }
  }) {
    BattleNetLogo()
  }
}