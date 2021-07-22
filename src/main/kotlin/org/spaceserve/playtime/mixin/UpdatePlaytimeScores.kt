package org.spaceserve.playtime.mixin

import net.minecraft.server.network.ServerPlayerEntity
import org.spaceserve.playtime.Common
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ServerPlayerEntity::class)
abstract class UpdatePlaytimeScores {
    @Inject(
        method = ["playerTick"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;updateScores(Lnet/minecraft/scoreboard/ScoreboardCriterion;I)V",
            ordinal = 5,
            shift = At.Shift.BY,
            by = 2,
        )]
    )
    private fun updateScores(ci: CallbackInfo) {
        Common.SCOREBOARD_CRITERIA.updateScores(this as Any as ServerPlayerEntity)
    }
}