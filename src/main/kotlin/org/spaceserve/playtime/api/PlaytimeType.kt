package org.spaceserve.playtime.api

enum class PlaytimeType {
    /**
     * Playtime where the player was actively playing
     */
    Active,

    /**
     * Playtime where the player was afk
     */
    Inactive,

    /**
     * Combined [Active] and [Inactive] playtime
     */
    All,
}