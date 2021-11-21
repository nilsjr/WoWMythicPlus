package de.nilsdruyen.mythicplus.character.enums

enum class ItemSlot {
  Head,
  Neck,
  Shoulder,
  Back,
  Chest,
  Wrist,
  Hands,
  Waist,
  Legs,
  Feet,
  Finger1,
  Finger2,
  Trinket1,
  Trinket2,
  MainHand,
  Offhand
}

fun String.toSlot() = ItemSlot.values().firstOrNull { it.name.lowercase() == this }