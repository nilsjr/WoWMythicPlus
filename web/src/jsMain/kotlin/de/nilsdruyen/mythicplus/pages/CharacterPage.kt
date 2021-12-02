package de.nilsdruyen.mythicplus.pages

import LocalCharacterUsecase
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import de.nilsdruyen.mythicplus.character.models.WebViewModel
import de.nilsdruyen.mythicplus.components.gear.GearTable
import de.nilsdruyen.mythicplus.components.mythicplus.MythicPlusTable
import de.nilsdruyen.mythicplus.states.ArgumentState

@Composable
fun CharacterPage(arguments: ArgumentState.PageArguments, page: MutableState<Page>) {
  val usecase = LocalCharacterUsecase.current
  val currentState by usecase.viewModel().collectAsState(WebViewModel.Loading)

  LaunchedEffect(Unit) {
    usecase.setInput(arguments.characterList)
  }

  when (currentState) {
    WebViewModel.Loading -> LoadingPage()
    is WebViewModel.CharacterOverview -> {
      val overview = currentState as WebViewModel.CharacterOverview
      when (page.value) {
        Page.MythicPlus -> MythicPlusTable(overview)
        Page.Gear -> GearTable(WebViewModel.GearOverview(overview.characterList))
      }
    }
    else -> {}
  }
}