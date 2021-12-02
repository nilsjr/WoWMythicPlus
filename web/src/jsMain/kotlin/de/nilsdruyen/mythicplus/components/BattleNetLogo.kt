package de.nilsdruyen.mythicplus.components

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.external.AnimationConfig
import de.nilsdruyen.mythicplus.external.lottie
import io.ktor.http.encodeURLPath
import kotlinx.browser.window
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Composable
fun BattleNetLogo() {
  Div({
    style {
      property("margin", "0 auto")
      width(80.px)
      height(80.px)
    }
  }) {
    DomSideEffect {
      val logoPath = "${window.origin.encodeURLPath()}/assets/loading-bnet.json"
      val data = jsObject<AnimationConfig> {
        container = it
        renderer = "svg"
        loop = true
        autoplay = true
        path = logoPath
      }
      lottie.loadAnimation(data)
    }
  }
}

// Workaround until I found another solution due jsObject internal visibility
internal fun <T : Any> jsObject(): T = js("({})") as T

internal inline fun <T : Any> jsObject(builder: T.() -> Unit): T =
  jsObject<T>().apply(builder)