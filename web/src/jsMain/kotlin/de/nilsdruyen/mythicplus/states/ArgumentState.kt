package de.nilsdruyen.mythicplus.states

import de.nilsdruyen.mythicplus.character.models.InputCharacter

sealed class ArgumentState {

  object NoArguments : ArgumentState()

  object InvalidArguments : ArgumentState()

  class PageArguments(
    val characterList: List<InputCharacter>,
  ) : ArgumentState()
}
