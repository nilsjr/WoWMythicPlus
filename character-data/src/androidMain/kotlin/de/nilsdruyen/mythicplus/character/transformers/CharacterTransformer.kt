package de.nilsdruyen.mythicplus.character.transformers

import de.nilsdruyen.mythicplus.character.entities.CharacterEntity
import de.nilsdruyen.mythicplus.character.entities.DominationShardEntity
import de.nilsdruyen.mythicplus.character.entities.DungeonScoreEntity
import de.nilsdruyen.mythicplus.character.entities.GearEntity
import de.nilsdruyen.mythicplus.character.entities.ItemEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreEntity
import de.nilsdruyen.mythicplus.character.extensions.getSpec
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.DominationShard
import de.nilsdruyen.mythicplus.character.models.DungeonScore
import de.nilsdruyen.mythicplus.character.models.Gear
import de.nilsdruyen.mythicplus.character.models.Item
import de.nilsdruyen.mythicplus.character.models.Score

interface CharacterTransformer {

  fun toEntity(model: Character): CharacterEntity

  fun toModel(entity: CharacterEntity): Character
}

class CharacterTransformerImpl : CharacterTransformer {

  override fun toEntity(model: Character): CharacterEntity {
    return with(model) {
      CharacterEntity(
        uuid = "${name}_$realm",
        name = name,
        realm = realm,
        wowClass = specialization.wowClass,
        specName = specialization.name,
        profileUrl = profileUrl,
        score = score,
        scoreColorHex = scoreColorHex,
        dungeons = dungeons.map { fromDungeonsScore(it) },
        gear = fromGear(gear),
        completedKeysThisWeek = completedKeysThisWeek,
      )
    }
  }

  override fun toModel(entity: CharacterEntity): Character {
    return with(entity) {
      Character(
        name = name,
        realm = realm,
        specialization = wowClass.getSpec(specName),
        profileUrl = profileUrl,
        score = score,
        scoreColorHex = scoreColorHex,
        dungeons = dungeons.map { toDungeonsScore(it) },
        gear = toGear(gear),
        completedKeysThisWeek = completedKeysThisWeek,
      )
    }
  }

  private fun fromGear(model: Gear): GearEntity {
    return with(model) {
      GearEntity(
          itemLevel,
          items.map {
        ItemEntity(
          it.id,
          it.slot,
          it.name,
          it.level,
          it.icon,
          it.isLegendary,
          it.dominationShards.map { shard ->
            with(shard) {
              DominationShardEntity(quality, name, icon, id)
            }
          },
          it.gems,
          it.bonuses
        )
      }
      )
    }
  }

  private fun toGear(entity: GearEntity): Gear {
    return with(entity) {
      Gear(
          itemLevel,
          items.map {
        Item(
          it.id,
          it.slot,
          it.name,
          it.level,
          it.icon,
          it.isLegendary,
          it.dominationShards.map { shard ->
            with(shard) {
              DominationShard(quality, name, icon, id)
            }
          },
          it.gems,
          it.bonuses
        )
      }
      )
    }
  }

  private fun fromDungeonsScore(model: DungeonScore): DungeonScoreEntity {
    return with(model) {
      DungeonScoreEntity(shortName, fromScore(tyrannicalScore), fromScore(fortifiedScore))
    }
  }

  private fun toDungeonsScore(model: DungeonScoreEntity): DungeonScore {
    return with(model) {
      DungeonScore(shortName, toScore(tyrannicalScore), toScore(fortifiedScore))
    }
  }

  private fun fromScore(model: Score): ScoreEntity = with(model) {
    ScoreEntity(id, score, level, upgrade, cleanTimeMs)
  }

  private fun toScore(entity: ScoreEntity): Score = with(entity) {
    Score(id, score, level, upgrade, cleanTimeMs)
  }
}