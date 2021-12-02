package de.nilsdruyen.mythicplus.character.extensions

import de.nilsdruyen.mythicplus.character.enums.Affliction
import de.nilsdruyen.mythicplus.character.enums.Arcane
import de.nilsdruyen.mythicplus.character.enums.Arms
import de.nilsdruyen.mythicplus.character.enums.Assassination
import de.nilsdruyen.mythicplus.character.enums.Balance
import de.nilsdruyen.mythicplus.character.enums.BeastMastery
import de.nilsdruyen.mythicplus.character.enums.Blood
import de.nilsdruyen.mythicplus.character.enums.Brewmaster
import de.nilsdruyen.mythicplus.character.enums.DeathKnightSpec
import de.nilsdruyen.mythicplus.character.enums.Demonology
import de.nilsdruyen.mythicplus.character.enums.Destruction
import de.nilsdruyen.mythicplus.character.enums.Discipline
import de.nilsdruyen.mythicplus.character.enums.DruidSpec
import de.nilsdruyen.mythicplus.character.enums.Elemental
import de.nilsdruyen.mythicplus.character.enums.Enhancement
import de.nilsdruyen.mythicplus.character.enums.Feral
import de.nilsdruyen.mythicplus.character.enums.Fire
import de.nilsdruyen.mythicplus.character.enums.Fury
import de.nilsdruyen.mythicplus.character.enums.Guardian
import de.nilsdruyen.mythicplus.character.enums.Havoc
import de.nilsdruyen.mythicplus.character.enums.MageSpec
import de.nilsdruyen.mythicplus.character.enums.Marksmanship
import de.nilsdruyen.mythicplus.character.enums.Mistweaver
import de.nilsdruyen.mythicplus.character.enums.Outlaw
import de.nilsdruyen.mythicplus.character.enums.PaladinSpec
import de.nilsdruyen.mythicplus.character.enums.PriestSpec
import de.nilsdruyen.mythicplus.character.enums.Retribution
import de.nilsdruyen.mythicplus.character.enums.Shadow
import de.nilsdruyen.mythicplus.character.enums.ShamanSpec
import de.nilsdruyen.mythicplus.character.enums.Specialization
import de.nilsdruyen.mythicplus.character.enums.Subtlety
import de.nilsdruyen.mythicplus.character.enums.Survival
import de.nilsdruyen.mythicplus.character.enums.Unholy
import de.nilsdruyen.mythicplus.character.enums.Vengeance
import de.nilsdruyen.mythicplus.character.enums.WarriorSpec
import de.nilsdruyen.mythicplus.character.enums.Windwalker
import de.nilsdruyen.mythicplus.character.enums.WoWClass

fun WoWClass.getSpec(specializationRaw: String): Specialization {
  return when (this) {
    WoWClass.DEATH_KNIGHT -> listOf(DeathKnightSpec.Frost, Blood, Unholy)
    WoWClass.DEMON_HUNTER -> listOf(Havoc, Vengeance)
    WoWClass.DRUID -> listOf(DruidSpec.Restoration, Balance, Feral, Guardian)
    WoWClass.HUNTER -> listOf(BeastMastery, Marksmanship, Survival)
    WoWClass.MAGE -> listOf(MageSpec.Frost, Fire, Arcane)
    WoWClass.MONK -> listOf(Brewmaster, Mistweaver, Windwalker)
    WoWClass.PALADIN -> listOf(PaladinSpec.Holy, Retribution, PaladinSpec.Protection)
    WoWClass.PRIEST -> listOf(PriestSpec.Holy, Discipline, Shadow)
    WoWClass.ROGUE -> listOf(Assassination, Outlaw, Subtlety)
    WoWClass.SHAMAN -> listOf(ShamanSpec.Restoration, Elemental, Enhancement)
    WoWClass.WARLOCK -> listOf(Affliction, Demonology, Destruction)
    WoWClass.WARRIOR -> listOf(WarriorSpec.Protection, Fury, Arms)
  }.firstOrNull { it.name == specializationRaw.lowercase() } ?: Blood
}