package org.spaceserve.playtime.api

enum class PlaytimeType(val asByte: Byte) {
    /**
     * Playtime where the player was actively playing
     */
    Active(1),

    /**
     * Playtime where the player was afk
     */
    Inactive(-1),

    /**
     * Combined [Active] and [Inactive] playtime
     */
    All(0),
}