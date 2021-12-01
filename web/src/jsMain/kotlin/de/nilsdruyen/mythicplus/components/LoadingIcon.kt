package de.nilsdruyen.mythicplus.components

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.animation
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.duration
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Composable
fun LoadingIcon() {
  Div({
    classes(LoadingStyle.icon)
  }) {
    Div {}
    Div {}
  }
}

object LoadingStyle : StyleSheet(AppStylesheet) {

  val iconAnimation by keyframes {
    from {
      top(36.px)
      left(36.px)
      width(0.px)
      height(0.px)
      opacity(1)
    }
    to {
      top(0.px)
      left(0.px)
      width(72.px)
      height(72.px)
      opacity(0)
    }
  }

  val icon by style {
    display(DisplayStyle.InlineBlock)
    position(Position.Relative)
    width(80.px)
    height(80.px)

    self + " div" style {
      position(Position.Absolute)
      border {
        width = 4.px
        style = LineStyle.Solid
        color = Color("#fff")
      }
      opacity(1)
      borderRadius(50.percent)
      animation(iconAnimation) {
        duration(1.s)
        timingFunction = listOf(AnimationTimingFunction("cubic-bezier(0, 0.2, 0.8, 1) infinite"))
      }
    }

    self + " div:nth-child(2)" style {
      property("animation-delay", "-0.5s")
    }
  }
}