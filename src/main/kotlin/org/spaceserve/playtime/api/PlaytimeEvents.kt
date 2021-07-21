package org.spaceserve.playtime.api

import net.minecraft.server.network.ServerPlayerEntity
import org.spaceserve.playtime.mixin.PlaytimeTracking

/**
 * Collection of events relating to playtime. Register an action to an event by calling the `onEvent(action)` method
 * with the action to happen as the parameter.
 */
object PlaytimeEvents {
    private val playerAfkActions = mutableSetOf<(ServerPlayerEntity) -> Unit>()
    private val playerActiveActions = mutableSetOf<(ServerPlayerEntity) -> Unit>()

    /**
     * Raised when a player has been afk for 5 minutes.
     */
    fun onPlayerAfk(action: (ServerPlayerEntity) -> Unit) {
        playerAfkActions.add(action)
    }

    /**
     * Raised when a player is no longer afk
     */
    fun onPlayerActive(action: (ServerPlayerEntity) -> Unit) {
        playerActiveActions.add(action)
    }

    internal fun raise(event: IEventuate) {
        when (event) {
            is PlayerAfkEvent -> playerAfkActions.forEach { it(event.player) }
            is PlayerActiveEvent -> playerActiveActions.forEach { it(event.player) }
        }
    }

    internal interface IEventuate
    internal class PlayerAfkEvent(val player: ServerPlayerEntity) : IEventuate
    internal class PlayerActiveEvent(val player: ServerPlayerEntity) : IEventuate
}