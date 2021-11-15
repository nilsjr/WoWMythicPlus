import de.nilsdruyen.mythicplus.Dashboard
import de.nilsdruyen.mythicplus.components.Layout
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

fun main() {
  renderComposable(rootElementId = "root") {
    Style(AppStylesheet)
    Layout {
      Dashboard()
    }
  }
}

