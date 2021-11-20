package de.nilsdruyen.mythicplus.styles

import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.lineHeight
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.textDecoration
import org.jetbrains.compose.web.css.width

object AppStylesheet : StyleSheet() {

  init {
    "html, body" style {
      height(100.percent)
      margin(0.px)
      background("#212121")
      color(Color.white)
    }
  }
}

object TextStyle : StyleSheet(AppStylesheet) {

  val default by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(600)
    textAlign("center")
    property(
      "font-family",
      "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }

  val defaultTitle by style {
    color(Color.white)
    fontSize(24.px)
    fontWeight(600)
    textAlign("center")
    property(
      "font-family",
      "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }

  val level by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(600)
    textAlign("center")

    property(
      "font-family",
      "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }

  val levelHint by style {
    color(Color.white)
    fontSize(12.px)
    fontWeight(400)
    textAlign("center")

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
    textAlign("center")
    borderRadius(4.px)
    padding(4.px)
    property("text-shadow", "1px 1px #000")

    property(
      "font-family",
      "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }

  val headText by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(600)
    textAlign("start")

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
    textDecoration("none")
    borderRadius(12.px)
    padding(12.px, 32.px)
    lineHeight(24.px)
    fontWeight(400)
    property("width", "fit-content")
  }
}

object CellStyle : StyleSheet(AppStylesheet) {

  val level by style {
    borderRadius(4.px)
  }
}