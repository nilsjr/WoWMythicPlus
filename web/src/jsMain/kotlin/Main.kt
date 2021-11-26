import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import de.nilsdruyen.mythicplus.character.CharacterUsecase
import de.nilsdruyen.mythicplus.character.CharacterUsecaseImpl
import de.nilsdruyen.mythicplus.character.RaiderIoRepositoryImpl
import de.nilsdruyen.mythicplus.pages.MythicPlusWebPage
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

val LocalCharacterUsecase = compositionLocalOf<CharacterUsecase> { CharacterUsecaseImpl(RaiderIoRepositoryImpl()) }
val currentLocation = "${window.location.protocol}//${window.location.host}"

fun main() {
  val characterUsecase: CharacterUsecase = CharacterUsecaseImpl(RaiderIoRepositoryImpl())
  renderComposable(rootElementId = "root") {
    Style(AppStylesheet)
    CompositionLocalProvider(LocalCharacterUsecase provides characterUsecase) {
      MythicPlusWebPage()
    }
  }
}