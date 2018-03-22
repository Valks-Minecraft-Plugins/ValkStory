package com.ValkStory.Story;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ValkStory.ValkStory;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

public class Story implements Listener {
	ValkStory plugin = null;
	public Story() {
		plugin = JavaPlugin.getPlugin(ValkStory.class);
	}
	
	@EventHandler
	private void storyOnJoin(PlayerJoinEvent e) {
		plugin.refreshData();
		
		final Player p = e.getPlayer();
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 4 + 20 * 1, 1));
		p.teleport(new Location(p.getWorld(), 2, 36, -46));
		
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.createNPC(EntityType.PLAYER, "");
		MetadataStore data = npc.data();
		
		data.setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false);
		data.setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, p.getName());
		npc.getNavigator().getDefaultParameters().speedModifier(1f);
		npc.getNavigator().getDefaultParameters().range(50.0f);
		npc.spawn(p.getLocation());
		

		Equipment equipTrait = npc.getTrait(Equipment.class);
		equipTrait.set(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));
		equipTrait.set(EquipmentSlot.OFF_HAND, new ItemStack(Material.IRON_SWORD));
		
		p.setGameMode(GameMode.SPECTATOR);
		
		new BukkitRunnable() {
			public void run() {
				p.teleport(npc.getEntity());
			}
		}.runTaskLater(plugin, 20 * 2);
		
		new BukkitRunnable() {
			public void run() {
				new Camera(p, npc).runTaskTimer(plugin, 0, 1);
			}
		}.runTaskLater(plugin, 20 * 4);
	}
}
