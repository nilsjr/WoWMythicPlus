package de.nilsdruyen.mythicplus.pages

import LocalCharacterUsecase
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.exceptions.InvalidArgumentsException
import de.nilsdruyen.mythicplus.character.utils.convertToCharacterList
import de.nilsdruyen.mythicplus.components.Version
import de.nilsdruyen.mythicplus.pages.common.InvalidArgumentsPage
import de.nilsdruyen.mythicplus.pages.common.LoadingPage
import de.nilsdruyen.mythicplus.pages.common.NoArgumentsPage
import de.nilsdruyen.mythicplus.pages.gear.GearTable
import de.nilsdruyen.mythicplus.pages.mythicplus.MythicPlusPage
import de.nilsdruyen.mythicplus.pages.raid.RaidPage
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import de.nilsdruyen.mythicplus.styles.ButtonStyle
import de.nilsdruyen.mythicplus.utils.PageConst
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Footer
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.url.URLSearchParams

@Composable
fun WebPage() {
  var state by remember { mutableStateOf<PageState>(PageState.NoArguments) }
  val currentPage = remember { mutableStateOf(Page.MythicPlus) }

  LaunchedEffect(Unit) {
    val urlParams = URLSearchParams(window.location.search)
    currentPage.value = urlParams.get("page").jumpTo()

    if (urlParams.has(PageConst.CHARACTERS)) {
      try {
        val characters = (urlParams.get(PageConst.CHARACTERS) ?: "").convertToCharacterList()
        if (characters.isNotEmpty()) {
          state = PageState.PageArguments(characters)
        }
      } catch (e: InvalidArgumentsException) {
        state = PageState.InvalidArguments
      }
    } else if (urlParams.has(PageConst.REALM) && urlParams.has(PageConst.NAMES)) {
      try {
        val realm = urlParams.get(PageConst.REALM) ?: throw InvalidArgumentsException
        val characters =
          (urlParams.get(PageConst.NAMES) ?: throw InvalidArgumentsException).convertToCharacterList(realm)
        if (characters.isNotEmpty()) {
          state = PageState.PageArguments(characters)
        }
      } catch (e: InvalidArgumentsException) {
        state = PageState.InvalidArguments
      }
    }
  }

  PageHeader()
  Menu(isVisible = state is PageState.PageArguments, currentPage.value) {
    currentPage.value = it
  }
  Content {
    when (state) {
      PageState.NoArguments -> NoArgumentsPage()
      PageState.InvalidArguments -> InvalidArgumentsPage()
      is PageState.PageArguments -> PageWithArguments(state as PageState.PageArguments, currentPage)
    }
  }
  PageFooter()
}

@Composable
fun PageWithArguments(arguments: PageState.PageArguments, page: MutableState<Page>) {
  val usecase = LocalCharacterUsecase.current
  val scope = rememberCoroutineScope()
  val viewModel = remember { WebPageViewModel(scope, usecase) }
  val state = viewModel.state.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.intent.send(WebPageIntent.LoadData(arguments.characterList))
  }

  println("webpage ${state.value.isLoading}")
  if (state.value.isLoading) {
    LoadingPage()
  } else {
    val overview = state.value.characterOverview
    when (page.value) {
      Page.MythicPlus -> MythicPlusPage(overview)
      Page.Raid -> RaidPage(overview)
      Page.Gear -> GearTable(overview.characterList)
    }
  }
}

@Composable
fun PageHeader() {
  Header({
    classes(AppStylesheet.pageHeader)
  }) { Text("Mythic+ Overview") }
}

@Composable
fun Menu(isVisible: Boolean, currentPage: Page, onMenuItemClick: (Page) -> Unit) {
  Aside({
    classes(AppStylesheet.pageMenu)
  }) {
    if (isVisible) {
      MenuButton(Page.MythicPlus, currentPage == Page.MythicPlus) {
        onMenuItemClick(Page.MythicPlus)
      }
      MenuButton(Page.Raid, currentPage == Page.Raid) {
        onMenuItemClick(Page.Raid)
      }
      MenuButton(Page.Gear, currentPage == Page.Gear) {
        onMenuItemClick(Page.Gear)
      }
    }
  }
}

@Composable
fun MenuButton(page: Page, isActive: Boolean, onMenuItemClick: () -> Unit) {
  Button(
    attrs = {
      classes(if (isActive) ButtonStyle.buttonActive else ButtonStyle.button)
      onClick { onMenuItemClick() }
    }
  ) { Text(page.title()) }
}

@Composable
fun Content(content: @Composable () -> Unit) {
  Div({
    classes(AppStylesheet.pageContent)
  }) { content() }
}

@Composable
fun PageFooter() {
  Footer({
    classes(AppStylesheet.pageFooter)
  }) {
    Version()
  }
}