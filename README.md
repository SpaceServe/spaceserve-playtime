# SpaceServe Playtime API
An easy to use api for all your playtime needs. [Playtime Tracker](https://modrinth.com/mod/playtime-tracker) is a simple 
implementation of this library that tracks playtime, has simple leaderboards, and more. 

 - Track Playtime
     - All playtime
     - Improved afk detection
     - Afk time
     - Non-afk playtime
     - The above, but dimension specific
 - Playtime Triggers and Events
     - Playtime event on afk and on active
     - All playtime types as advancement triggers
     - All playtime types as scoreboard objectives 
 - Simple playtime to Duration conversion

## Add to your project
```kotlin
repositories {
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
}

dependencies {
    // Playtime
    modImplementation("maven.modrinth:playtime:0.1.0-rc1")
    include("maven.modrinth:playtime:0.1.0-rc1")
}
```
