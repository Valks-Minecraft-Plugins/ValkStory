## About
An attempt to make a configurable Bukkit Camera plugin. The camera is initialized on rejoin event by default, you can change this in the source.

Citizens-2.0.22-SNAPSHOT is required.

Commands

/story <set | look | wait | clear>

/story set <path>
  
/story look <path> <true | false>
  
/story wait <path> <wait_time>
  
/story clear

I did not make a command to set the message, though you can set the path in-game than set it in the config. When editing the config make sure the server is offline otherwise the config values will not be saved. Also when done setting values in game make sure to reload the server before rejoining.

You may use this plugin, merge it with your own, modify its source. Totally up to you if you want to mention my name anywhere.

## Compiling
1. Clone the repository directly into your IDE.
2. Build Spigot with [Spigot's Build Tools](https://www.spigotmc.org/wiki/buildtools/) then add the JAR to the projects build path.
3. Fix any outdated code in the project depending on what version of Spigot you installed.
4. Compile the plugin by exporting it to a JAR file.
