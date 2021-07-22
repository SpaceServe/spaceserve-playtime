package org.spaceserve.playtime

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.advancement.criterion.Criteria
import org.spaceserve.playtime.advancement.PlaytimeCriterion
import org.spaceserve.playtime.scoreboard.PlaytimeCriteria

object Common : ModInitializer {
    val PLAYTIME: PlaytimeCriterion = Criteria.register(PlaytimeCriterion())
    val SCOREBOARD_CRITERIA = PlaytimeCriteria()

    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(SCOREBOARD_CRITERIA::reloadDimensionalCriteria)
    }
}
