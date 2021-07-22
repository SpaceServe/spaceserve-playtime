@file:OptIn(ExperimentalTime::class)

package org.spaceserve.playtime.advancement

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

data class TimeBlock(
    val milliseconds: Long = 0,
    val seconds: Long = 0,
    val minutes: Long = 0,
    val hours: Long = 0,
    val days: Long = 0,
    val minecraftDays: Long = 0,
) {
    fun toDuration(): Duration = Duration.milliseconds(milliseconds)
        .plus(Duration.seconds(seconds))
        .plus(Duration.minutes(minutes + (minecraftDays * 20)))
        .plus(Duration.hours(hours))
        .plus(Duration.days(days))

    override fun equals(other: Any?): Boolean {
        return if (other is TimeBlock) {
            this.toDuration().inWholeMilliseconds == other.toDuration().inWholeMilliseconds
        } else {
            false
        }
    }

    companion object {
        val ZERO = TimeBlock()

        fun fromDuration(duration: Duration): TimeBlock = TimeBlock(milliseconds = duration.inWholeMilliseconds)
    }
}
