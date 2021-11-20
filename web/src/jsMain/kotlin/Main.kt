import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import de.nilsdruyen.mythicplus.character.RaiderIoRepository
import de.nilsdruyen.mythicplus.character.RaiderIoRepositoryImpl
import de.nilsdruyen.mythicplus.pages.MythicPlusWebPage
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

val LocalDataRepository = compositionLocalOf<RaiderIoRepository> { RaiderIoRepositoryImpl() }
val currentLocation = "${window.location.protocol}//${window.location.host}"

fun main() {
  val raiderIoRepository: RaiderIoRepository = RaiderIoRepositoryImpl()

  renderComposable(rootElementId = "root") {
    Style(AppStylesheet)
    CompositionLocalProvider(LocalDataRepository provides raiderIoRepository) {
      MythicPlusWebPage()
    }
  }
}