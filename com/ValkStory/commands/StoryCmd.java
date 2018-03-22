package com.ValkStory.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ValkStory.ValkStory;

public class StoryCmd implements CommandExecutor {
	ValkStory plugin = null;
	public StoryCmd() {
		plugin = JavaPlugin.getPlugin(ValkStory.class);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("story")) {
			Player p = Bukkit.getPlayer(sender.getName());
			Location loc = p.getLocation();
			if (!p.isOp()) {
				sender.sendMessage("Must be op to use this.");
				return true;
			}
			if (args.length < 1) {
				p.sendMessage("/story <set | look | wait | clear>");
				return true;
			}
			
			Configuration config = plugin.getStoryConfig();
			
			if (args[0].toLowerCase().equals("set")) {
				if (args.length < 2) {
					sender.sendMessage("/story set <path>");
					return true;
				}
				
				config.set("path." + args[1] + ".location.x", loc.getX());
				config.set("path." + args[1] + ".location.y", loc.getY());
				config.set("path." + args[1] + ".location.z", loc.getZ());
				config.set("path." + args[1] + ".location.pitch", loc.getPitch());
				config.set("path." + args[1] + ".location.yaw", loc.getYaw());
				config.set("path." + args[1] + ".location.world", loc.getWorld().getName());
				
				config.set("path." + args[1] + ".look", Boolean.parseBoolean("false"));
				config.set("path." + args[1] + ".message", "NONE");
				config.set("path." + args[1] + ".wait", Double.parseDouble("0"));
				
				plugin.saveStoryConfig();
				plugin.refreshData();
				
				sender.sendMessage("Path " + args[1] + " was set!");
				return true;
			}
			
			if (args[0].toLowerCase().equals("look")) {
				if (args.length < 3) {
					sender.sendMessage("/story look <path> <true | false>");
					return true;
				}
				
				if (!config.isSet("path." + args[1] + ".location.x")) {
					sender.sendMessage("Path " + args[1] + " must be set first!");
					return true;
				}
				
				config.set("path." + args[1] + ".look", Boolean.parseBoolean(args[2]));
				config.set("path." + args[1] + ".wait", Double.parseDouble("3"));
				
				plugin.saveStoryConfig();
				plugin.refreshData();
				
				sender.sendMessage("Path " + args[1] + " parameter 'looking' was set to " + args[2] + " to 3 seconds by default.");
				
				return true;
			}
			
			if (args[0].toLowerCase().equals("wait")) {
				if (args.length < 3) {
					sender.sendMessage("/story wait <path> <wait_time>");
					return true;
				}
				
				if (!config.isSet("path." + args[1] + ".location.x")) {
					sender.sendMessage("Path " + args[1] + " must be set first!");
					return true;
				}
				
				config.set("path." + args[1] + ".wait", Double.parseDouble(args[2]));
				
				plugin.saveStoryConfig();
				plugin.refreshData();
				
				sender.sendMessage("Path " + args[1] + " parameter 'wait' was set to " + args[2]);
				return true;
			}

			if (args[0].toLowerCase().equals("clear")) {
				config.set("path", null);
				plugin.saveStoryConfig();
				sender.sendMessage("Config was cleared!");
				return true;
			}
			return true;
		}
		return true;
	}

}
