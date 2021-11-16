import de.nilsdruyen.mythicplus.Dashboard
import de.nilsdruyen.mythicplus.character.CharacterRepository
import de.nilsdruyen.mythicplus.character.CharacterRepositoryImpl
import de.nilsdruyen.mythicplus.components.Layout
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

fun main() {
  val characterRepository: CharacterRepository = CharacterRepositoryImpl()

  renderComposable(rootElementId = "root") {
    Style(AppStylesheet)
    Layout {
      Dashboard(characterRepository)
    }
  }
}

