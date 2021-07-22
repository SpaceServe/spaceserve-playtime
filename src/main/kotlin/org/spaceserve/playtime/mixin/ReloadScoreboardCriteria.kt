package org.spaceserve.playtime.mixin

import net.minecraft.server.MinecraftServer
import org.spaceserve.playtime.Common
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.concurrent.CompletableFuture

@Mixin(MinecraftServer::class)
abstract class ReloadScoreboardCriteria {
    @Inject(
        method = ["reloadResources"],
        at = [At(value = "RETURN")]
    )
    private fun reload(datapacks: Collection<String>, cir: CallbackInfoReturnable<CompletableFuture<Void>>) {
        Common.SCOREBOARD_CRITERIA.reloadDimensionalCriteria(this as Any as MinecraftServer)
    }
}