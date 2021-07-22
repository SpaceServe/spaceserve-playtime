@file:OptIn(ExperimentalTime::class)

package org.spaceserve.playtime.scoreboard

import net.minecraft.scoreboard.ScoreboardCriterion
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.spaceserve.playtime.api.PlaytimeType
import org.spaceserve.playtime.api.conditionalPlaytime
import kotlin.time.ExperimentalTime

class PlaytimeCriteria {
    private val defaultCriteria = mapOf(
        PlaytimeType.All to register("all"),
        PlaytimeType.Inactive to register("afk"),
        PlaytimeType.Active to register("active"),
    )
    private val dimensionalCriteria = mutableMapOf<Pair<PlaytimeType, Identifier>, List<ScoreboardCriterion>>()

    private fun register(criterion: String): List<ScoreboardCriterion> {
        return listOf(
            ScoreboardCriterion.create(
                "playtime.minutes.$criterion", true, ScoreboardCriterion.RenderType.INTEGER
            ),
            ScoreboardCriterion.create(
                "playtime.hours.$criterion", true, ScoreboardCriterion.RenderType.INTEGER
            ),
        )
    }

    private fun registerDimension(id: Identifier) {
        dimensionalCriteria[PlaytimeType.All to id] = register("all.$id")
        dimensionalCriteria[PlaytimeType.Inactive to id] = register("afk.$id")
        dimensionalCriteria[PlaytimeType.Active to id] = register("active.$id")
    }

    fun reloadDimensionalCriteria(server: MinecraftServer) {
        dimensionalCriteria.clear()
        server.worldRegistryKeys.map { it.value }.forEach(::registerDimension)
    }

    fun updateScores(player: ServerPlayerEntity) {
        defaultCriteria.forEach { (type, criterions) ->
            player.scoreboard.forEachScore(criterions[0], player.entityName) { playerScore ->
                playerScore.score = player.conditionalPlaytime(type = type).inWholeMinutes.toInt()
            }
            player.scoreboard.forEachScore(criterions[1], player.entityName) { playerScore ->
                playerScore.score = player.conditionalPlaytime(type = type).inWholeHours.toInt()
            }
        }

        dimensionalCriteria.forEach { (type, dimId), criterions ->
            player.scoreboard.forEachScore(criterions[0], player.entityName) { playerScore ->
                playerScore.score = player.conditionalPlaytime(listOf(dimId), type).inWholeMinutes.toInt()
            }
            player.scoreboard.forEachScore(criterions[1], player.entityName) { playerScore ->
                playerScore.score = player.conditionalPlaytime(listOf(dimId), type).inWholeHours.toInt()
            }
        }
    }
}