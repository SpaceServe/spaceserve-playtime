@file:OptIn(ExperimentalTime::class)

package org.spaceserve.playtime.mixin

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Util
import org.apache.logging.log4j.LogManager
import org.spaceserve.playtime.api.ITrackPlaytime
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Mixin(ServerPlayNetworkHandler::class)
abstract class PlaytimeTracking {
    private lateinit var player: ServerPlayerEntity
    
    private lateinit var playtimePlayer: ITrackPlaytime

    private var previousPlaytimeUpdate: Long = Util.getMeasuringTimeMs()

    @Inject(
        method = ["<init>", "onClientStatus"],
        at = [At(value = "TAIL")]
    )
    private fun lateinit(ci: CallbackInfo) {
        player = (this as Any as ServerPlayNetworkHandler).player
        playtimePlayer = player as ITrackPlaytime
    }

    @Inject(
        method = ["tick"],
        at = [At(value = "TAIL")]
    )
    private fun incrementTime(ci: CallbackInfo) {
        val previous = previousPlaytimeUpdate
        val now = Util.getMeasuringTimeMs()
        val elapsed = Duration.milliseconds(now - previous)
        previousPlaytimeUpdate = now

        val dimId = player.world.registryKey.value
        var (all, afk) = Duration.ZERO to Duration.ZERO
        if (playtimePlayer.times.containsKey(dimId)) {
            all = playtimePlayer.times[dimId]!!.first
            afk = playtimePlayer.times[dimId]!!.second
        }

        if (playtimePlayer.isAfk) {
            playtimePlayer.times[dimId] = (all + elapsed) to (afk + elapsed)
        } else {
            playtimePlayer.times[dimId] = (all + elapsed) to afk
        }
    }

    @Inject(
        method = ["tick"],
        at = [At(value = "HEAD")]
    )
    private fun checkForAfk(ci: CallbackInfo) {
        val elapsed = Util.getMeasuringTimeMs() - playtimePlayer.lastLookTime
        if (elapsed >= 30000) { // 300000 ms == 5 min
            if (!playtimePlayer.isAfk) {
                // Add the past 5 minutes of afk time to the afk time counter. If this dimension wasn't being tracked,
                // set the all time and afk time to the past 5 minutes
                val dimId = player.world.registryKey.value
                val timePair = playtimePlayer.times[dimId]
                val elapsedDuration = Duration.milliseconds(elapsed)
                playtimePlayer.times[dimId] =
                    (timePair?.first ?: elapsedDuration) to ((timePair?.second ?: Duration.ZERO) + elapsedDuration)

                playtimePlayer.isAfk = true
            }
        }
    }

    @Inject(
        method = ["onPlayerMove"],
        at = [At(value = "HEAD")]
    )
    private fun improvedAfkDetection(packet: PlayerMoveC2SPacket, ci: CallbackInfo) {
        if (packet is PlayerMoveC2SPacket.Full || packet is PlayerMoveC2SPacket.LookAndOnGround) {
            playtimePlayer.lastLookTime = Util.getMeasuringTimeMs()

            if (playtimePlayer.isAfk) {
                playtimePlayer.isAfk = false
            }
        }
    }
}