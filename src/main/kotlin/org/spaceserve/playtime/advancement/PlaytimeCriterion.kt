package org.spaceserve.playtime.advancement

import com.google.gson.JsonObject
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class PlaytimeCriterion : AbstractCriterion<PlaytimeConditions>() {
    override fun getId(): Identifier = Companion.id

    override fun conditionsFromJson(
        json: JsonObject?,
        playerPredicate: EntityPredicate.Extended?,
        predicateDeserializer: AdvancementEntityPredicateDeserializer?
    ): PlaytimeConditions {
        return PlaytimeConditions(playerPredicate, PlaytimePredicate.fromJson(json?.get("playtime")))
    }

    fun trigger(player: ServerPlayerEntity) {
        test(player) { conditions ->
            conditions.matches(player)
        }
    }

    companion object {
        val id = Identifier("spaceserve", "playtime")
    }
}
