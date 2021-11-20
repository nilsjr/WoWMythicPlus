package de.nilsdruyen.mythicplus.pages

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.styles.TextStyle
import de.nilsdruyen.mythicplus.utils.PageConst
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun NoArgumentsPage() {
  ArgumentStatusPage("Please provide realm & character names via url parameters")
}

@Composable
fun InvalidArgumentsPage() {
  ArgumentStatusPage("Url parameter format is invalid. Please provide correct arguments")
}

@Composable
fun ArgumentStatusPage(title: String) {
  Div({
    style {
      property("margin", "0 auto")
      padding(30.px)
    }
  }) {
    P({
      classes(TextStyle.defaultTitle)
    }) {
      Text(title)
    }
    Br { }
    P({
      classes(TextStyle.default)
    }) {
      Text("examples")
      Br { }
      Br { }
      Text("multiple realms: ${PageConst.PARAM_EXAMPLE_MULTIPLE}")
      Br { }
      Br { }
      Text("single realm: ${PageConst.PARAM_EXAMPLE_SINGLE}")
    }
  }
}