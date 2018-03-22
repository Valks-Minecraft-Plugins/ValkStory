package com.ValkStory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.ValkStory.Story.Story;
import com.ValkStory.commands.StoryCmd;

public class ValkStory extends JavaPlugin {
	File storyConfigFile = new File(getDataFolder(), "story.yml");
	FileConfiguration storyConfig = YamlConfiguration.loadConfiguration(storyConfigFile);
	
	public List<Location> locs = new ArrayList<>();

	public boolean[] look = null;
	public String[] message = null;
	public double[] wait = null;
	public double[] locX = null;
	public double[] locY = null;
	public double[] locZ = null;
	public float[] locPitch = null;
	public float[] locYaw = null;
	public String[] world = null;
	
	public int length = 0;
	
	public ValkStory() {
		refreshData();
	}
	
	public void refreshData() {
		initArrays();
		fillArrays();
		initLocs();
	}

	@Override
	public void onEnable() {
		defaults();
		registerEvents();
		registerCommands();
	}
	
	@Override
	public void onDisable() {
		saveStoryConfig();
	}
	
	private void defaults() {
		if (!storyConfig.isSet("path.0")) {
			storyConfig.set("path.0.look", "false");
			storyConfig.set("path.0.message", "NONE");
			storyConfig.set("path.0.wait", "0");
			storyConfig.set("path.0.location.x", "0");
			storyConfig.set("path.0.location.y", "50");
			storyConfig.set("path.0.location.z", "0");
			storyConfig.set("path.0.location.pitch", "0");
			storyConfig.set("path.0.location.yaw", "0");
			storyConfig.set("path.0.location.world", "world");
			saveStoryConfig();
		}
	}

	private void initArrays() {
		length = registerArraysLength();

		look = new boolean[length];
		message = new String[length];
		wait = new double[length];
		locX = new double[length];
		locY = new double[length];
		locZ = new double[length];
		locPitch = new float[length];
		locYaw = new float[length];
		world = new String[length];
	}
	
	private void fillArrays() {
		for (int i = 0; i < length; i++) {
			look[i] = storyConfig.getBoolean("path." + i + ".look");
	    message[i] = storyConfig.getString("path." + i + ".message");
	    wait[i] = storyConfig.getDouble("path." + i + ".wait");
	    locX[i] = storyConfig.getDouble("path." + i + ".location.x");
	    locY[i] = storyConfig.getDouble("path." + i + ".location.y");
	    locZ[i] = storyConfig.getDouble("path." + i + ".location.z");
	    locPitch[i] = (float) storyConfig.getDouble("path." + i + ".location.yaw");
	    locYaw[i] = (float) storyConfig.getDouble("path." + i + ".location.pitch");
	    world[i] = storyConfig.getString("path." + i + ".location.world");
		}
	}
	
	private void initLocs() {
		for (int i = 0; i < length; i++) {
			Location loc = new Location(Bukkit.getWorld(world[i]), locX[i], locY[i], locZ[i], locYaw[i], locPitch[i]);
			locs.add(loc);
		}
	}

	public int registerArraysLength() {
		int length = 0;
		while (storyConfig.isSet("path." + length)) {
			length++;
		}
		return length;
	}

	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Story(), this);
	}
	
	private void registerCommands() {
		getCommand("story").setExecutor(new StoryCmd());
	}

	public Configuration getStoryConfig() {
		return storyConfig;
	}

	public void saveStoryConfig() {
		try {
			storyConfig.save(storyConfigFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
