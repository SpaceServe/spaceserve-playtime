@file:OptIn(ExperimentalTime::class)

package org.spaceserve.playtime.api

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * Get the total playtime
 * @see ITrackPlaytime.getPlaytime
 */
val ServerPlayerEntity.playtime: Duration
    get() = (this as ITrackPlaytime).getPlaytime()

/**
 * Gets the playtime based on the provided conditions
 *
 * @param dimensions The dimensions that the playtime occurred in, or null (default) for all dimensions
 * @param type The playtime type, default: [PlaytimeType.All]
 * @see ITrackPlaytime.conditionalPlaytime
 */
fun ServerPlayerEntity.conditionalPlaytime(
    dimensions: List<Identifier>? = null,
    type: PlaytimeType = PlaytimeType.All,
): Duration = (this as ITrackPlaytime).conditionalPlaytime(dimensions, type)
