package de.nilsdruyen.mythicplus.character.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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

  // Convert start/end to UTC to avoid system-dependent timezone shifts
  @OptIn(ExperimentalTime::class)
  val startDate = Instant.parse(start).toLocalDateTime(TimeZone.UTC)
  @OptIn(ExperimentalTime::class)
  private val endDate = Instant.parse(end).toLocalDateTime(TimeZone.UTC)

  // Use half-open interval [start, end) to prevent overlap on boundary
  fun isCurrentWeek(now: LocalDateTime): Boolean = now >= startDate && now < endDate
}