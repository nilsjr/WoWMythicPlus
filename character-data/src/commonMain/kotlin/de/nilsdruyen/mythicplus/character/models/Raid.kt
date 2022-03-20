package de.nilsdruyen.mythicplus.character.models

data class Raid(
  val id: Int,
  val slug: String,
  val name: String,
  val shortName: String,
  val icon: String,
  val encounters: List<Encounter>
)

data class Encounter(
  val id: Int,
  val slug: String,
  val name: String,
)
