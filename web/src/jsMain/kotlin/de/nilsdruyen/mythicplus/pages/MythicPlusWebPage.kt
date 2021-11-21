package de.nilsdruyen.mythicplus.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.exceptions.InvalidArgumentsException
import de.nilsdruyen.mythicplus.character.utils.convertToCharacterList
import de.nilsdruyen.mythicplus.components.Version
import de.nilsdruyen.mythicplus.states.ArgumentState
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import de.nilsdruyen.mythicplus.styles.ButtonStyle
import de.nilsdruyen.mythicplus.utils.PageConst
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.url.URLSearchParams

@Composable
fun MythicPlusWebPage() {
  var state by remember { mutableStateOf<ArgumentState>(ArgumentState.NoArguments) }
  val page = remember { mutableStateOf<Page>(Page.MythicPlus) }

  LaunchedEffect(Unit) {
    val urlParams = URLSearchParams(window.location.search)

    if (urlParams.has(PageConst.CHARACTERS)) {
      try {
        val characters = (urlParams.get(PageConst.CHARACTERS) ?: "").convertToCharacterList()
        if (characters.isNotEmpty()) {
          state = ArgumentState.PageArguments(characters)
        }
      } catch (e: InvalidArgumentsException) {
        state = ArgumentState.InvalidArguments
      }
    } else if (urlParams.has(PageConst.REALM) && urlParams.has(PageConst.NAMES)) {
      try {
        val realm = urlParams.get(PageConst.REALM) ?: throw InvalidArgumentsException
        val characters =
          (urlParams.get(PageConst.NAMES) ?: throw InvalidArgumentsException).convertToCharacterList(realm)
        if (characters.isNotEmpty()) {
          state = ArgumentState.PageArguments(characters)
        }
      } catch (e: InvalidArgumentsException) {
        state = ArgumentState.InvalidArguments
      }
    }
  }

  Header()
  Menu(isVisible = state is ArgumentState.PageArguments, page.value) {
    page.value = it
  }
  Content {
    when (state) {
      ArgumentState.NoArguments -> NoArgumentsPage()
      ArgumentState.InvalidArguments -> InvalidArgumentsPage()
      is ArgumentState.PageArguments -> CharacterPage(state as ArgumentState.PageArguments, page)
    }
  }
  Footer()
}

@Composable
fun Header() {
  org.jetbrains.compose.web.dom.Header({
    classes(AppStylesheet.pageHeader)
  }) { Text("Mythic+ Overview") }
}

@Composable
fun Menu(isVisible: Boolean, currentPage: Page, onMenuItemClick: (Page) -> Unit) {
  org.jetbrains.compose.web.dom.Aside({
    classes(AppStylesheet.pageMenu)
  }) {
    if (isVisible) {
      Button(
        attrs = {
          classes(if (currentPage == Page.MythicPlus) ButtonStyle.buttonActive else ButtonStyle.button)
          onClick {
            onMenuItemClick(Page.MythicPlus)
          }
        }
      ) {
        Text("Mythic+ Scores")
      }
      Button(
        attrs = {
          classes(if (currentPage == Page.Gear) ButtonStyle.buttonActive else ButtonStyle.button)
          onClick {
            onMenuItemClick(Page.Gear)
          }
        }
      ) {
        Text("Character gear")
      }
    }
  }
}

@Composable
fun Content(content: @Composable () -> Unit) {
  Div({
    classes(AppStylesheet.pageContent)
  }) { content() }
}

@Composable
fun Footer() {
  org.jetbrains.compose.web.dom.Footer({
    classes(AppStylesheet.pageFooter)
  }) {
    Version()
  }
}