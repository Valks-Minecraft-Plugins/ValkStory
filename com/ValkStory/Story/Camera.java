package com.ValkStory.Story;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.ValkStory.ValkStory;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.NavigatorParameters;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;

public class Camera extends BukkitRunnable {
	Player p = null;
	NPC npc = null;
	double timer = 0;
	
	ValkStory plugin = null;
	
	public Camera(Player p, NPC npc) {
		this.p = p;
		this.npc = npc;
		
		this.plugin = JavaPlugin.getPlugin(ValkStory.class);
	}
	
	
	boolean sentMsg = false;
	int index = 0;
	


	public void run() {
		System.out.println(index);
		Navigator nav = npc.getNavigator();
		NavigatorParameters params = nav.getDefaultParameters();
		
		Location npcloc = npc.getEntity().getLocation();
		
		if (!p.isOnline()) {
			npc.destroy();
			cancel();
		}
		
		if (p.getGameMode().equals(GameMode.SPECTATOR)) {
			p.setSpectatorTarget(npc.getEntity());
		} else {
			npc.destroy();
			p.setGameMode(GameMode.CREATIVE);
			cancel();
		}
		
		if (plugin.wait[index] == 0) {
			params.speedModifier(1f);
			nav.setTarget(plugin.locs.get(index));
			if (!sentMsg) {
				if (!plugin.message[index].equals("NONE")) {
					sentMsg = true;
					p.sendMessage(plugin.message[index]);
				}
			}
			if (npcloc.distance(plugin.locs.get(index)) < 2) {
				sentMsg = false;
				index++;
				nav.cancelNavigation();
			}
		} else {
			timer++;
			double seconds = timer / 20.0;
			Location lookAt = plugin.locs.get(index);
			look(npc.getEntity(), (float) Math.toDegrees(lookAt.getYaw()), (float) Math.toDegrees(lookAt.getPitch()));
			if (!sentMsg) {
				if (!plugin.message[index].equals("NONE")) {
					sentMsg = true;
					p.sendMessage(plugin.message[index]);
				}
			}
			if (seconds >= plugin.wait[index]) {
				sentMsg = false;
				index++;
				nav.cancelNavigation();
			}
		}
		
		if (index >= plugin.length) {
			npc.destroy();
			p.setGameMode(GameMode.CREATIVE);
			cancel();
		}
	}
	
  public void look(Entity entity, float yaw, float pitch) {
      net.minecraft.server.v1_12_R1.Entity handle = ((CraftEntity) entity).getHandle();
      if (handle != null) {
          handle.yaw = yaw;
          if (handle instanceof EntityLiving) {
              EntityLiving livingHandle = (EntityLiving) handle;
              while (yaw < -180.0F) {
                  yaw += 360.0F;
              }
              while (yaw >= 180.0F) {
                  yaw -= 360.0F;
              }
              livingHandle.aP = yaw;
              if (!(handle instanceof EntityHuman)) {
                  livingHandle.aO = yaw;
              }
              livingHandle.aQ = yaw;
          }
          handle.pitch = pitch;
      }
  }
}
