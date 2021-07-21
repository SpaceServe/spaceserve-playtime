package org.spaceserve.playtime

import net.fabricmc.api.ModInitializer
import net.minecraft.advancement.criterion.Criteria
import org.spaceserve.playtime.advancement.PlaytimeCriterion

object Common : ModInitializer {
    val PLAYTIME: PlaytimeCriterion = Criteria.register(PlaytimeCriterion())

    override fun onInitialize() {

    }
}
