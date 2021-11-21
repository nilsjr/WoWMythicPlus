package de.nilsdruyen.mythicplus.components

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.BuildConfig
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun Version() {
  Div({
    style {
      margin(12.px)
      textAlign("center")
    }
  }) {
    Text(BuildConfig.VERSION)
  }
}