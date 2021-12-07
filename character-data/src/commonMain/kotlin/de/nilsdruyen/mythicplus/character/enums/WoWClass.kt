package de.nilsdruyen.mythicplus.character.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class WoWClass {
  @SerialName("Death Knight")
  DEATH_KNIGHT,

  @SerialName("Demon Hunter")
  DEMON_HUNTER,

  @SerialName("Druid")
  DRUID,

  @SerialName("Hunter")
  HUNTER,

  @SerialName("Mage")
  MAGE,

  @SerialName("Monk")
  MONK,

  @SerialName("Paladin")
  PALADIN,

  @SerialName("Priest")
  PRIEST,

  @SerialName("Rouge")
  ROGUE,

  @SerialName("Shaman")
  SHAMAN,

  @SerialName("Warlock")
  WARLOCK,

  @SerialName("Warrior")
  WARRIOR,
}

sealed class Specialization(
  val name: String,
  private val role: Role,
  val wowClass: WoWClass
) {

  override fun toString(): String = "$name - ${role.name} - ${wowClass.name}"
}

sealed class DeathKnightSpec(name: String, role: Role) : Specialization(name, role, WoWClass.DEATH_KNIGHT) {

  object Frost : DeathKnightSpec("frost", Role.DPS)
}

object Blood : DeathKnightSpec("blood", Role.TANK)
object Unholy : DeathKnightSpec("unholy", Role.DPS)

sealed class DemonHunterSpec(name: String, role: Role) : Specialization(name, role, WoWClass.DEMON_HUNTER)
object Havoc : DemonHunterSpec("havoc", Role.DPS)
object Vengeance : DemonHunterSpec("vengeance", Role.TANK)

sealed class DruidSpec(name: String, role: Role) : Specialization(name, role, WoWClass.DRUID) {

  object Restoration : DruidSpec("restoration", Role.HEALER)
}

object Balance : DruidSpec("balance", Role.DPS)
object Feral : DruidSpec("feral", Role.DPS)
object Guardian : DruidSpec("guardian", Role.TANK)

sealed class HunterSpec(name: String, role: Role) : Specialization(name, role, WoWClass.HUNTER)
object BeastMastery : HunterSpec("beast mastery", Role.DPS)
object Marksmanship : HunterSpec("marksmanship", Role.DPS)
object Survival : HunterSpec("survival", Role.DPS)

sealed class MageSpec(name: String, role: Role) : Specialization(name, role, WoWClass.MAGE) {

  object Frost : MageSpec("frost", Role.DPS)
}

object Arcane : MageSpec("arcane", Role.DPS)
object Fire : MageSpec("fire", Role.DPS)

sealed class MonkSpec(name: String, role: Role) : Specialization(name, role, WoWClass.MONK)
object Brewmaster : MonkSpec("brewmaster", Role.TANK)
object Mistweaver : MonkSpec("mistweaver", Role.HEALER)
object Windwalker : MonkSpec("windwalker", Role.DPS)

sealed class PaladinSpec(name: String, role: Role) : Specialization(name, role, WoWClass.PALADIN) {

  object Holy : PaladinSpec("holy", Role.HEALER)
  object Protection : PaladinSpec("protection", Role.TANK)
}

object Retribution : PaladinSpec("retribution", Role.DPS)

sealed class PriestSpec(name: String, role: Role) : Specialization(name, role, WoWClass.PRIEST) {

  object Holy : PriestSpec("holy", Role.HEALER)
}

object Shadow : PriestSpec("shadow", Role.DPS)
object Discipline : PriestSpec("discipline", Role.HEALER)

sealed class RogueSpec(name: String, role: Role) : Specialization(name, role, WoWClass.ROGUE)
object Assassination : RogueSpec("assassination", Role.DPS)
object Outlaw : RogueSpec("outlaw", Role.DPS)
object Subtlety : RogueSpec("subtlety", Role.DPS)

sealed class ShamanSpec(name: String, role: Role) : Specialization(name, role, WoWClass.SHAMAN) {

  object Restoration : ShamanSpec("restoration", Role.HEALER)
}

object Elemental : ShamanSpec("elemental", Role.DPS)
object Enhancement : ShamanSpec("enhancement", Role.DPS)

sealed class WarlockSpec(name: String, role: Role) : Specialization(name, role, WoWClass.WARLOCK)
object Affliction : WarlockSpec("affliction", Role.DPS)
object Demonology : WarlockSpec("demonology", Role.DPS)
object Destruction : WarlockSpec("destruction", Role.DPS)

sealed class WarriorSpec(name: String, role: Role) : Specialization(name, role, WoWClass.WARRIOR) {

  object Protection : WarriorSpec("protection", Role.TANK)
}

object Arms : WarriorSpec("arms", Role.DPS)
object Fury : WarriorSpec("fury", Role.DPS)