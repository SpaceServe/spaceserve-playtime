package org.spaceserve.playtime.advancement

import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.server.network.ServerPlayerEntity

class PlaytimeConditions(player: EntityPredicate.Extended?, private val time: PlaytimePredicate) :
    AbstractCriterionConditions(PlaytimeCriterion.id, player) {

    fun matches(player: ServerPlayerEntity): Boolean = time.test(player)

    companion object {
        fun create(): PlaytimeConditions = PlaytimeConditions(EntityPredicate.Extended.EMPTY, PlaytimePredicate.ANY)

        fun create(predicate: PlaytimePredicate) = PlaytimeConditions(EntityPredicate.Extended.EMPTY, predicate)
    }
}
