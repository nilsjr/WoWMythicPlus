package de.nilsdruyen.mythicplus.styles

import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.selectors.CSSSelector

object AppStylesheet : StyleSheet() {

  init {
    CSSSelector.Universal style {
      margin(0.px)
      background("#212121")
      color(Color.white)
    }
  }
}

object TextStyle : StyleSheet(AppStylesheet) {

  val score by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(600)

    property(
      "font-family",
      "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }

  val title by style {
    color(Color.white)
    fontSize(24.px)
    fontWeight(600)

    property(
      "font-family",
      "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }

  val headText by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(600)

    property(
      "font-family",
      "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }
}

object ColorConst {

  const val RED = "#c62828"
  const val GREEN = "#2e7d32"
}