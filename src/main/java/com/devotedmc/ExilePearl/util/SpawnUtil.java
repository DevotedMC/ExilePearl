package com.devotedmc.ExilePearl.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.devotedmc.ExilePearl.ExilePearlPlugin;

public class SpawnUtil {

	/**
	 * Spawns a player in the world without killing them
	 * Uses Randomspawn if available, otherwise natural spawn
	 *
	 * @param player The player to spawn
	 * @param world  The world in which to spawn the player
	 */
	public static void spawnPlayer(Player player, World world) {
		player.teleport(chooseSpawn(world).add(0, 0.5, 0));
	}

	public static Location chooseSpawn(World world) {
		Location spawn = null;
		spawn = world.getSpawnLocation();
		return spawn;
	}
}
