package de.nilsdruyen.mythicplus.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.styles.ButtonStyle
import de.nilsdruyen.mythicplus.styles.InputStyle
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text

@Composable
fun CharacterAddPane(onAdd: (String, String) -> Unit) {
  var realm by mutableStateOf("")
  var charName by mutableStateOf("")

  Div({
    style {
      padding(8.px)
      display(DisplayStyle.Flex)
    }
  }) {
    Input(type = InputType.Text) {
      classes(InputStyle.input)
      value(realm)
      onInput {
        println("change realm: ${it.value}")
        realm = it.value
      }
    }
    Input(type = InputType.Text) {
      classes(InputStyle.input)
      value(charName)
      onInput {
        println("change char: ${it.value}")
        charName = it.value
      }
    }
    Button({
      classes(ButtonStyle.button)
      onClick {
        if (realm.isNotEmpty() && charName.isNotEmpty()) {
          onAdd(realm, charName)
          realm = ""
          charName = ""
        } else {
          println("empty input")
        }
      }
    }) {
      Text("add")
    }
  }
}