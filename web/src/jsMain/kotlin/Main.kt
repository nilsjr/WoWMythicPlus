import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import de.nilsdruyen.mythicplus.character.CharacterUsecase
import de.nilsdruyen.mythicplus.character.CharacterUsecaseImpl
import de.nilsdruyen.mythicplus.character.RaiderIoRepositoryImpl
import de.nilsdruyen.mythicplus.character.apis.RaiderIoApiImpl
import de.nilsdruyen.mythicplus.character.apis.RaiderIoClient
import de.nilsdruyen.mythicplus.pages.WebPage
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

val LocalCharacterUsecase = compositionLocalOf<CharacterUsecase> { error("no usecase provided") }
val currentLocation = "${window.location.protocol}//${window.location.host}"

fun main() {
  val api = RaiderIoApiImpl(RaiderIoClient.build())
  val characterUseCase: CharacterUsecase = CharacterUsecaseImpl(RaiderIoRepositoryImpl(api))
  renderComposable(rootElementId = "root") {
    Style(AppStylesheet)
    CompositionLocalProvider(LocalCharacterUsecase provides characterUseCase) {
      WebPage()
    }
  }
}