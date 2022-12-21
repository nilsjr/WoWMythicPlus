package de.nilsdruyen.mythicplus.character.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PeriodWebEntity(
  val periods: List<PeriodItemWebEntity>
)

@Serializable
data class PeriodItemWebEntity(
  val region: String,
  val previous: PeriodDetailWebEntity,
  val next: PeriodDetailWebEntity,
  val current: PeriodDetailWebEntity,
)

@Serializable
data class PeriodDetailWebEntity(
  val period: Int,
  val start: String,
  val end: String,
) {

  val startDate = start.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())
  private val endDate = end.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())

  fun isCurrentWeek(now: LocalDateTime): Boolean = now in startDate..endDate
}