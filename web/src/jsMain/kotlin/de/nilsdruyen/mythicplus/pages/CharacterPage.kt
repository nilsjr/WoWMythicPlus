package de.nilsdruyen.mythicplus.pages

import LocalDataRepository
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
  val dataRepository = LocalDataRepository.current
  var state by remember { mutableStateOf<CharacterViewModel>(CharacterViewModel.Loading) }

  LaunchedEffect(Unit) {
    val dungeons = dataRepository.getDungeons()
    val characterList = dataRepository.getCharacterList(arguments.characterList)
    val currentAffixes = dataRepository.getCurrentAffixeIds()
    state = CharacterViewModel.MythicPlusOverview(characterList, currentAffixes, dungeons)
  }

  when (state) {
    CharacterViewModel.Loading -> LoadingPage()
    is CharacterViewModel.MythicPlusOverview -> {
      when (page.value) {
        Page.MythicPlus -> MythicPlusTable(state as CharacterViewModel.MythicPlusOverview)
        Page.Gear -> GearTable()
      }
    }
  }
}