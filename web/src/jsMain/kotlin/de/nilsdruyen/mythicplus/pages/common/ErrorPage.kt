package de.nilsdruyen.mythicplus.pages.common

import androidx.compose.runtime.Composable
import currentLocation
import de.nilsdruyen.mythicplus.styles.TextStyle
import de.nilsdruyen.mythicplus.utils.PageConst
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
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
      classes(TextStyle.h1)
    }) {
      Text(title)
    }
    Br { }
    P({
      classes(TextStyle.body)
    }) {
      Text("examples")
      Br { }
      Br { }
      Text("multiple realms: ${PageConst.PARAM_EXAMPLE_MULTIPLE}")
      A(
        attrs = {
          classes(TextStyle.linkText)
          target(ATarget.Self)
        },
        href = "$currentLocation${PageConst.PARAM_EXAMPLE_MULTIPLE}"
      ) {
        Text("test it")
      }
      Br { }
      Br { }
      Text("single realm: ${PageConst.PARAM_EXAMPLE_SINGLE}")
      A(
        attrs = {
          classes(TextStyle.linkText)
          target(ATarget.Self)
        },
        href = "$currentLocation${PageConst.PARAM_EXAMPLE_SINGLE}"
      ) {
        Text("test it")
      }
      Br { }
      Br { }
      Text("single realm: ${PageConst.PARAM_EXAMPLE_SINGLE_2}")
      A(
        attrs = {
          classes(TextStyle.linkText)
          target(ATarget.Self)
        },
        href = "$currentLocation${PageConst.PARAM_EXAMPLE_SINGLE_2}"
      ) {
        Text("test it")
      }
    }
  }
}