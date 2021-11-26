package de.nilsdruyen.mythicplus.pages

import LocalCharacterUsecase
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.models.CharacterViewModel
import de.nilsdruyen.mythicplus.components.gear.GearTable
import de.nilsdruyen.mythicplus.components.mythicplus.MythicPlusTable
import de.nilsdruyen.mythicplus.states.ArgumentState

@Composable
fun CharacterPage(arguments: ArgumentState.PageArguments, page: MutableState<Page>) {
  val usecase = LocalCharacterUsecase.current
  var currentState by remember { mutableStateOf<CharacterViewModel>(CharacterViewModel.Loading) }

  LaunchedEffect(Unit) {
    currentState = usecase.loadCharacterViewModel(arguments.characterList)
  }

  when (currentState) {
    CharacterViewModel.Loading -> LoadingPage()
    is CharacterViewModel.CharacterOverview -> {
      val overview = currentState as CharacterViewModel.CharacterOverview
      when (page.value) {
        Page.MythicPlus -> MythicPlusTable(overview)
        Page.Gear -> GearTable(CharacterViewModel.GearOverview(overview.characterList))
      }
    }
    else -> {}
  }
}