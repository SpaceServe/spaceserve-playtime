package org.spaceserve.playtime.test

import net.fabricmc.api.ModInitializer
import net.minecraft.text.LiteralText
import org.spaceserve.playtime.api.ITrackPlaytime
import org.spaceserve.playtime.api.PlaytimeEvents
import org.spaceserve.playtime.api.PlaytimeType

object Common : ModInitializer {
    override fun onInitialize() {
        PlaytimeEvents.onPlayerActive {
            it.sendMessage(
                LiteralText("WELCOME BACK" +
                        "\nyour playtime is ${(it as ITrackPlaytime).conditionalPlaytime(type = PlaytimeType.Active)}" +
                        "\n" +
                        "your afk time is ${(it as ITrackPlaytime).conditionalPlaytime(type = PlaytimeType.Inactive)}"),
                false
            )
        }

        PlaytimeEvents.onPlayerAfk {
            it.sendMessage(
                LiteralText("GOOD BYE" +
                    "\nyour playtime is ${(it as ITrackPlaytime).conditionalPlaytime(type = PlaytimeType.Active)}" +
                        "\n" +
                        "your afk time is ${(it as ITrackPlaytime).conditionalPlaytime(type = PlaytimeType.Inactive)}"),
                false
            )
        }
    }
}
