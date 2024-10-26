package de.nilsdruyen.mythicplus.character.utils

import de.nilsdruyen.mythicplus.character.enums.Specialization
import de.nilsdruyen.mythicplus.character.enums.WoWClass
import de.nilsdruyen.mythicplus.character.extensions.toIdentifier

object Constants {

  const val EXPANSION = 10
  const val SEASON_SLUG = "season-tww-1"

  object Api {

    object Fields {

      private const val BEST_RUNS = "mythic_plus_best_runs:all"
      private const val ALT_RUNS = "mythic_plus_alternate_runs:all"
      private const val RECENT_RUNS = "mythic_plus_recent_runs"
      private const val SCORES_BY_SEASON = "mythic_plus_scores_by_season:current"
      private const val GEAR = "gear"
      private const val RAID_PROGRESS = "raid_progression"

      fun all() = listOf(
        BEST_RUNS,
        ALT_RUNS,
        RECENT_RUNS,
        SCORES_BY_SEASON,
        GEAR,
        RAID_PROGRESS,
      ).joinToString(",")
    }
  }

  object Icons {

    fun dungeonIcon(zoneId: Int): String = "https://cdnassets.raider.io/images/keystone-icons/$zoneId.jpg"

    fun gearIcon(id: String): String = "https://cdnassets.raider.io/images/wow/icons/medium/$id.jpg"

    fun clazzSpecIcon(specialization: Specialization): String =
      "https://cdnassets.raider.io/images/classes/spec_${specialization.wowClass.toIdentifier()}_${
        specialization.name.replace(
          " ",
          "-"
        )
      }.png"

    fun clazzIcon(clazz: WoWClass) =
      "https://cdnassets.raider.io/images/classes/class_${clazz.toIdentifier()}.png"

    const val HEALER = "https://cdnassets.raider.io/assets/img/role_healer-984e5e9867d6508a714a9c878d87441b.png"
    const val TANK = "https://cdnassets.raider.io/assets/img/role_tank-6cee7610058306ba277e82c392987134.png"
    const val DPS = "https://cdnassets.raider.io/assets/img/role_dps-eb25989187d4d3ac866d609dc009f090.png"

    const val RAIDER_IO = "https://cdnassets.raider.io/images/brand/Logo_2ColorWhite.svg"
    const val WOW_ARMORY = "https://cdnassets.raider.io/assets/img/wow-icon-a718385c1d75ca9edbb3eed0a5546c30.png"
  }

  fun wowArmoryUrl(realm: String, name: String) = "https://worldofwarcraft.com/en-gb/character/eu/$realm/$name"
}