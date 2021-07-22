# SpaceServe Playtime API
An easy to use api for all your playtime needs. [Playtime Tracker](https://modrinth.com/mod/playtime-tracker) is a simple 
implementation of this library that tracks playtime, has simple leaderboards, and more. 

 - [x] Track Playtime
     - [x] All playtime
     - [x] Improved afk detection
     - [x] Afk time
     - [x] Non-afk playtime
     - [x] The above, but dimension specific
 - [x] Playtime Triggers and Events
     - [x] Playtime event on afk and on active
     - [x] All playtime types as advancement triggers
     - [x] All playtime types as scoreboard objectives 
 - [x] Simple playtime to Duration conversion

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
