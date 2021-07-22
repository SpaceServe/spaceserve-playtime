@file:OptIn(ExperimentalTime::class)

package org.spaceserve.playtime.advancement

import com.google.gson.JsonElement
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.spaceserve.playtime.api.ITrackPlaytime
import org.spaceserve.playtime.api.PlaytimeType
import kotlin.time.ExperimentalTime

data class PlaytimePredicate(
    val time: TimeBlock = TimeBlock.ZERO,

    /**
     * true = afk time only
     *
     * false = non afk time only
     *
     * null = all time
     */
    val afk: Boolean? = null,

    /**
     * Only playtime in set dimensions will be considered, unless empty. Then all dimensions are considered
     */
    val dimensions: List<Identifier> = listOf(),
) {
    /**
     * Determines if the player has satisfied the playtime requirements
     */
    fun test(player: ServerPlayerEntity): Boolean {
        val playtime = (player as ITrackPlaytime).conditionalPlaytime(
            dimensions,
            when (afk) { true -> PlaytimeType.Inactive; false -> PlaytimeType.Active; else -> PlaytimeType.All}
        )

        return playtime >= time.toDuration()
    }

    companion object {
        val ANY = PlaytimePredicate()

        fun fromJson(json: JsonElement?): PlaytimePredicate {
            return if (json == null || !json.isJsonObject) {
                ANY
            } else {
                val jsonObj = json.asJsonObject
                val jsonTime = jsonObj["time"]?.toString()
                val jsonAfk = jsonObj["afk"]?.toString()
                val jsonDimensions = jsonObj["dimensions"]?.asJsonArray

                val time = if (jsonTime == null) {
                    TimeBlock.ZERO
                } else {
                    Json.decodeFromString<DecimalTimeBlock>(jsonTime).toTimeBlock()
                }

                val afk = if (jsonAfk == null) {
                    null
                } else {
                    Json.decodeFromString<Boolean?>(jsonAfk)
                }

                val dimensions = mutableListOf<Identifier>()
                jsonDimensions?.forEach { dimension ->
                    dimensions.add(Identifier.tryParse(dimension.asString)!!)
                }

                PlaytimePredicate(time, afk, dimensions)
            }
        }

        @Serializable
        private data class DecimalTimeBlock(
            val milliseconds: Double = 0.0,
            val seconds: Double = 0.0,
            val minutes: Double = 0.0,
            val hours: Double = 0.0,
            val days: Double = 0.0,
            val minecraftDays: Double = 0.0,
        ) {
            fun toTimeBlock(): TimeBlock = TimeBlock(
                milliseconds = milliseconds.toLong() +
                        (seconds * 1000).toLong() +
                        ((minutes + (minecraftDays * 20)) * 60 * 1000).toLong() +
                        (hours * 3600 * 1000).toLong() +
                        (days * 24 * 3600 * 1000).toLong()
            )
        }
    }
}
