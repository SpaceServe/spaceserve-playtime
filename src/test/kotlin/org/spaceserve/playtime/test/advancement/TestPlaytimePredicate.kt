package org.spaceserve.playtime.test.advancement

import com.google.gson.JsonParser
import net.minecraft.util.Identifier
import org.junit.Test
import org.spaceserve.playtime.advancement.PlaytimePredicate
import org.spaceserve.playtime.advancement.TimeBlock
import kotlin.test.assertEquals

class TestPlaytimePredicate {
    @Test
    fun fromJson() {
        val jsonStr = """
            {
                "time": {
                    "hours": 1.5,
                    "milliseconds": 2
                },
                "dimensions": [
                    "minecraft:the_nether",
                    "minecraft:the_end"
                ]
            }
        """

        assertEquals(
            PlaytimePredicate(
                time = TimeBlock(milliseconds = 2, hours = 1, minutes = 30),
                dimensions = listOf(
                    Identifier("minecraft", "the_nether"),
                    Identifier("minecraft", "the_end")
                )
            ),
            PlaytimePredicate.fromJson(JsonParser().parse(jsonStr))
        )
    }
}