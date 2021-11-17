package de.nilsdruyen.mythicplus.screens

import androidx.compose.runtime.Composable
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.components.CharacterRow
import de.nilsdruyen.mythicplus.components.TableHeader
import org.jetbrains.compose.web.dom.Table

@Composable
fun CharacterOverview(characters: List<Character>, currentAffixIds: List<Int>) {
  Table({
    style {
      property("border-spacing", "8px")
    }
  }) {
    TableHeader()
    if (characters.isNotEmpty()) {
      characters.forEach {
        CharacterRow(it, currentAffixIds)
      }
    }
  }
}