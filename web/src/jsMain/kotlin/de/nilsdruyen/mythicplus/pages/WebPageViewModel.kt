package de.nilsdruyen.mythicplus.pages

import de.nilsdruyen.mythicplus.character.CharacterUsecase
import de.nilsdruyen.mythicplus.character.models.InputCharacter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class WebPageViewModel(
  private val coroutineScope: CoroutineScope,
  private val characterUsecase: CharacterUsecase
) {

  private val _state = MutableStateFlow(WebPageState(true))
  val state: StateFlow<WebPageState> = _state

  val intent = Channel<WebPageIntent>(Channel.CONFLATED)

  init {
    println("init WebPageViewModel")
    observe()
  }

  private fun observe() {
    coroutineScope.launch {
      intent.consumeAsFlow().collect {
        when (it) {
          is WebPageIntent.LoadData -> getCharacterOverview(it.characterList)
        }
      }
    }
  }

  private fun getCharacterOverview(characterList: List<InputCharacter>) {
    coroutineScope.launch {
      _state.value = state.value.copy(isLoading = false, characterOverview = characterUsecase.execute(characterList))
    }
  }
}