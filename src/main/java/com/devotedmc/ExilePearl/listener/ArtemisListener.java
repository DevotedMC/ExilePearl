package com.devotedmc.ExilePearl.listener;

import com.devotedmc.ExilePearl.ExilePearlApi;
import com.github.maxopoly.artemis.events.PlayerAttemptLeaveShard;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class ArtemisListener extends RuleListener {
	/**
	 * Creates a new ExileListener instance
	 *
	 * @param pearlApi The PearlApi instance
	 */
	public ArtemisListener(ExilePearlApi pearlApi) {
		super(pearlApi);
	}
	
	@EventHandler
	public void onPlayerShardTransfer(PlayerAttemptLeaveShard event) {
		if (pearlApi.isPlayerExiled(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Exiled players cannot travel between shards.");
			return;
		}

		for (ItemStack itemStack : event.getPlayer().getInventory()) {
			if (pearlApi.getPearlFromItemStack(itemStack) != null) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You cannot carry pearls across shards! Store the pearl before retrying.");
				return;
			}
		}
	}
}
