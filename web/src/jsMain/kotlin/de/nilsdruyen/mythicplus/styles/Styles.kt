package de.nilsdruyen.mythicplus.styles

import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.lineHeight
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.selectors.CSSSelector
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.textDecoration
import org.jetbrains.compose.web.css.width

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

  val level by style {
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

  val score by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(400)
    textAlign("end")

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

object InputStyle : StyleSheet(AppStylesheet) {

  val input by style {
    width(200.px)
    margin(8.px)
    padding(4.px)
    backgroundColor(Color.white)
    color(Color.black)
  }
}

object ButtonStyle : StyleSheet(AppStylesheet) {

  val button by style {
    color(Color("white"))
    backgroundColor(Color("#167dff"))
    fontSize(15.px)
//    display(DisplayStyle.InlineBlock)
    textDecoration("none")
    borderRadius(12.px)
    padding(12.px, 32.px)
    lineHeight(24.px)
    fontWeight(400)
    property("width", "fit-content")
  }
}

object ColorConst {

  const val RED = "#c62828"
  const val GREEN = "#2e7d32"
}