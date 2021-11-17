package de.nilsdruyen.mythicplus.screens

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.styles.TextStyle
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun NoParameters() {
  Div({
    style {
      property("margin", "0 auto")
      padding(30.px)
    }
  }) {
    P({
      classes(TextStyle.defaultTitle)
    }) {
      Text("Please provide realm & character names via url parameters")
    }
    Br { }
    P({
      classes(TextStyle.default)
    }) {
      Text("example: /?realm=thrall&chars=Twilliam,Harazz")
    }
  }
}