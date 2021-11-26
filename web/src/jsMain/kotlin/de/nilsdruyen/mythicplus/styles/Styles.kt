package de.nilsdruyen.mythicplus.styles

import org.jetbrains.compose.web.css.CSSBuilder
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.gridTemplateColumns
import org.jetbrains.compose.web.css.gridTemplateRows
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.lineHeight
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.selectors.hover
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
      property("font-family", FontConst.DEFAULT)
    }

    "#root" style {
      width(100.percent)
      height(100.percent)
      display(DisplayStyle.Grid)
      gap(4.px)
      gridTemplateRows("auto auto 1fr auto")
      gridTemplateColumns("100%")
    }
  }

  val pageHeader by style {
    padding(22.px)
    textAlign("center")
    fontSize(28.px)
  }

  val pageMenu by style {
    padding(12.px)
    textAlign("center")
  }

  val pageContent by style {

  }

  val pageFooter by style {
    padding(4.px)
  }
}

object TextStyle : StyleSheet(AppStylesheet) {

  val default by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(600)
    textAlign("center")
  }

  val defaultTitle by style {
    color(Color.white)
    fontSize(24.px)
    fontWeight(600)
    textAlign("center")
  }

  val level by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(600)
    textAlign("center")
  }

  val levelHint by style {
    color(Color.white)
    fontSize(12.px)
    fontWeight(400)
    textAlign("center")
  }

  val title by style {
    color(Color.white)
    fontSize(24.px)
    fontWeight(600)
  }

  val runs by style {
    color(Color.white)
    fontSize(24.px)
    textAlign("center")
    fontWeight(600)
  }

  val score by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(400)
    textAlign("center")
    borderRadius(4.px)
    padding(4.px)
    property("text-shadow", "1px 1px #000")
  }

  val headText by style {
    color(Color.white)
    fontSize(20.px)
    fontWeight(600)
    textAlign("center")
  }

  val link by style {
    color(Color("white"))
    backgroundColor(Color("#1565c0"))
    fontSize(15.px)
    textDecoration("none")
    borderRadius(8.px)
    padding(8.px, 12.px)
    lineHeight(24.px)
    marginLeft(8.px)
    property("width", "fit-content")
    property("transition", "0.3s")
    opacity(.7)
    hover(self) style {
      opacity(1)
    }

  }

  val itemLevel by style {
    width(100.percent)
    display(DisplayStyle.Flex)
    justifyContent(JustifyContent.Center)
    alignItems("center")
  }

  val hint by style {
    margin(8.px)
    fontSize(14.px)
    color(Color.white)
  }
}

object ButtonStyle : StyleSheet(AppStylesheet) {

  val button by style {
    basicButton()
    opacity(.7)
  }

  val buttonActive by style {
    basicButton()
    opacity(1)
  }

  private fun CSSBuilder.basicButton() {
    width(160.px)
    padding(8.px, 12.px)
    margin(8.px)
    color(Color("white"))
    backgroundColor(Color("#1565c0"))
    fontSize(16.px)
    textDecoration("none")
    property("border", "none")
    borderRadius(8.px)
    lineHeight(24.px)
    fontWeight(400)
    property("transition", "0.3s")
    hover(self) style {
      opacity(1)
      textDecoration("underline")
    }
  }

  val order by style {
    orderButton()
    opacity(.6)
  }

  val orderActive by style {
    orderButton()
    opacity(1)
  }

  private fun CSSBuilder.orderButton() {
    width(100.percent)
    height(30.px)
    fontSize(12.px)
    color(Color("white"))
    backgroundColor(Color(ColorConst.GRAY))
    borderRadius(4.px)
    textDecoration("none")
    property("border", "none")
    property("transition", "0.3s")
    hover(self) style {
      opacity(1)
      textDecoration("underline")
    }
  }
}

object ImageStyle : StyleSheet(AppStylesheet) {

  val dungeon by style {
    width(30.px)
    height(30.px)
    borderRadius(50.percent)
    marginRight(8.px)
    property("vertical-align", "middle")
  }

  val affix by style {
    width(25.px)
    height(25.px)
    borderRadius(50.percent)
  }

  val item by style {
    width(30.px)
    height(30.px)
    borderRadius(4.px)
    property("vertical-align", "middle")
  }

  val icon by style {
    width(32.px)
    margin(2.px)
    height(32.px)
    borderRadius(50.percent)
  }
}

object TableStyle : StyleSheet(AppStylesheet) {

  val root by style {
    width(100.percent)
    property("border-spacing", "8px")
  }

  val cellLevel by style {
    borderRadius(4.px)
  }

  val cellImage by style {
    textAlign("center")
  }

  val cellItem by style {
    textAlign("center")
    borderRadius(4.px)
    padding(2.px)
  }
}