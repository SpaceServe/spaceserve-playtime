package org.spaceserve.playtime.mixin

import net.minecraft.server.network.ServerPlayerEntity
import org.spaceserve.playtime.Common
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ServerPlayerEntity::class)
abstract class PlaytimeCriterionTrigger {
    @Inject(
        method = ["tick"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancement/criterion/TickCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;)V"
        )]
    )
    private fun trigger(ci: CallbackInfo) {
        Common.PLAYTIME.trigger(this as Any as ServerPlayerEntity)
    }
}