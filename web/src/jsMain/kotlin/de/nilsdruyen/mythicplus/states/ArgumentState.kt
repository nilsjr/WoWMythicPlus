package de.nilsdruyen.mythicplus.states

sealed class ArgumentState {

  object NoArguments : ArgumentState()

  class PageArguments(
    val realm: String,
    val characterNameList: List<String>,
  ) : ArgumentState()
}
