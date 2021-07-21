@file:OptIn(ExperimentalTime::class)

package org.spaceserve.playtime.api

import net.minecraft.util.Identifier
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

interface ITrackPlaytime {
    /**
     * Map of Dimension Ids to <A: All time in dimension, B: Afk time in dimension>
     */
    val times: MutableMap<Identifier, Pair<Duration, Duration>>

    /**
     * If the player is currently afk
     */
    var isAfk: Boolean

    /**
     * Last time the player sent a look packet
     */
    var lastLookTime: Long

    /**
     * Get the total playtime
     */
    fun getPlaytime(): Duration {
        var time = Duration.ZERO
        times.values.forEach { (all, _) ->
            time += all
        }
        return time
    }

    /**
     * Gets the playtime based on the provided conditions
     *
     * @param dimensions The dimensions that the playtime occurred in, or null (default) for all dimensions
     * @param type The playtime type, default: [PlaytimeType.All]
     */
    fun conditionalPlaytime(
        dimensions: List<Identifier>? = null,
        type: PlaytimeType = PlaytimeType.All,
    ): Duration {
        var time = Duration.ZERO

        (dimensions ?: times.keys).forEach { dimensionId ->
            val (all, afk) = times[dimensionId] ?: Duration.ZERO to Duration.ZERO

            time += when (type) {
                PlaytimeType.Active -> all - afk
                PlaytimeType.Inactive -> afk
                PlaytimeType.All -> all
            }
        }

        return time
    }
}
