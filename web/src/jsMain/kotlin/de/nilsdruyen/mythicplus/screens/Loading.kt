package de.nilsdruyen.mythicplus.screens

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.components.BattleNetLogo
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

@Composable
fun Loading() {
  Div({
    style {
      property("margin", "0 auto")
      padding(30.px)
    }
  }) {
    BattleNetLogo()
  }
}