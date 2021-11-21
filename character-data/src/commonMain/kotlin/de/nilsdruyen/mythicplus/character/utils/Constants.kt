package de.nilsdruyen.mythicplus.character.utils

object Constants {

  val Dungeons = listOf(
    "NW",
    "SOA",
    "MISTS",
    "DOS",
    "SD",
    "HOA",
    "PF",
    "TOP",
  )

  const val TYRANNICAL = 9
  const val FORTIFIED = 10

  object Icons {

    // https://cdnassets.raider.io/images/keystone-icons/13228.jpg
//    const val DungeonIconUrl = "https://cdnassets.raider.io/images/keystone-icons/ZONEID.jpg"

    fun dungeonIcon(zoneId: Int): String {
      return "https://cdnassets.raider.io/images/keystone-icons/$zoneId.jpg"
    }

    fun gearIcon(id: String): String {
      return "https://cdnassets.raider.io/images/wow/icons/medium/$id.jpg"
    }

    const val FORTIFIED_URL = "https://cdnassets.raider.io/images/wow/icons/medium/ability_toughness.jpg"
    const val TYRANNICAL_URL = "https://cdnassets.raider.io/images/wow/icons/medium/achievement_boss_archaedas.jpg"

    // spec_CLASS_SPEC
    const val SpecIconUrl = "https://cdnassets.raider.io/images/classes/spec_paladin_holy.png"

    const val HEALER = "https://cdnassets.raider.io/assets/img/role_healer-984e5e9867d6508a714a9c878d87441b.png"
    const val TANK = "https://cdnassets.raider.io/assets/img/role_healer-984e5e9867d6508a714a9c878d87441b.png"
    const val DPS = "https://cdnassets.raider.io/assets/img/role_dps-eb25989187d4d3ac866d609dc009f090.png"
  }
}