package de.nilsdruyen.mythicplus.character.utils

fun String.convertToList(): List<String> {
  return if (this.isNotEmpty()) {
    if (this.contains(",")) this.split(",") else listOf(this)
  } else emptyList()
}