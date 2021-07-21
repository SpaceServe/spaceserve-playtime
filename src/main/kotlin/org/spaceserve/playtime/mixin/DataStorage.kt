@file:OptIn(ExperimentalTime::class)

package org.spaceserve.playtime.mixin

import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtLong
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import org.apache.logging.log4j.LogManager
import org.spaceserve.playtime.api.ITrackPlaytime
import org.spaceserve.playtime.api.PlaytimeEvents
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Mixin(ServerPlayerEntity::class)
abstract class DataStorage : ITrackPlaytime {
    override val times: MutableMap<Identifier, Pair<Duration, Duration>> = LinkedHashMap()

    override var isAfk: Boolean = false
        set(value) { // TODO: Move event raising to proper places, currently causes double events when respawning
            if (value == field) { return } // Don't raise events if the set value is the same as the current value

            if (value) {
                PlaytimeEvents.raise(PlaytimeEvents.PlayerAfkEvent(this as Any as ServerPlayerEntity))
            } else {
                PlaytimeEvents.raise(PlaytimeEvents.PlayerActiveEvent(this as Any as ServerPlayerEntity))
            }

            field = value
        }

    override var lastLookTime: Long = Util.getMeasuringTimeMs()

    @Inject(
        method = ["writeCustomDataToNbt"],
        at = [At(value = "TAIL")]
    )
    private fun saveData(tag: NbtCompound, ci: CallbackInfo) {
        val spaceserveTag = tag.getCompound("SpaceServe")
        val playtimeTag = NbtCompound()
        val afkTag = NbtByte.of(isAfk)
        val dimensionTimesTag = NbtCompound()

        times.forEach { id, (all, afk) ->
            val dimensionTimeTag = NbtCompound()
            val allTimeTag = NbtLong.of(all.inWholeMilliseconds)
            val afkTimeTag = NbtLong.of(afk.inWholeMilliseconds)

            dimensionTimeTag.put("all", allTimeTag)
            dimensionTimeTag.put("afk", afkTimeTag)
            dimensionTimesTag.put(id.toString(), dimensionTimeTag)
        }

        playtimeTag.put("afk", afkTag)
        playtimeTag.put("DimensionTimes", dimensionTimesTag)
        spaceserveTag.put("Playtime", playtimeTag)
        tag.put("SpaceServe", spaceserveTag)
    }

    @Inject(
        method = ["readCustomDataFromNbt"],
        at = [At(value = "TAIL")]
    )
    private fun readData(tag: NbtCompound, ci: CallbackInfo) {
        if (tag.contains("SpaceServe")) {
            val spaceserveTag = tag.getCompound("SpaceServe")
            if (spaceserveTag.contains("Playtime")) {
                val playtimeTag = spaceserveTag.getCompound("Playtime")

                isAfk = playtimeTag.getBoolean("afk")

                val dimensionTimesTag = playtimeTag.getCompound("DimensionTimes")
                dimensionTimesTag.keys.forEach {
                    val dimensionTimeTag = dimensionTimesTag.getCompound(it)
                    val id = Identifier.tryParse(it)
                    val all = dimensionTimeTag.getLong("all")
                    val afk = dimensionTimeTag.getLong("afk")

                    if (id != null) {
                        times[id] = Duration.milliseconds(all) to Duration.milliseconds(afk)
                    }
                }
            }
        }
    }

    @Inject(
        method = ["copyFrom"],
        at = [At(value = "TAIL")]
    )
    private fun copyData(oldPlayer: ServerPlayerEntity, alive: Boolean, ci: CallbackInfo) {
        isAfk = (oldPlayer as ITrackPlaytime).isAfk
        times.clear()
        times.putAll(oldPlayer.times)
        lastLookTime = oldPlayer.lastLookTime
    }
}