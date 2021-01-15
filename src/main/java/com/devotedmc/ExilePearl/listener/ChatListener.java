package com.devotedmc.ExilePearl.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.devotedmc.ExilePearl.ExilePearlApi;
import com.devotedmc.ExilePearl.ExileRule;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends RuleListener {


	public ChatListener(ExilePearlApi pearlApi) {
		super(pearlApi);
	}

	/**
	 * Prevents exiled players from chatting globally
	 * @param e The event
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChatEvent(AsyncPlayerChatEvent e) {
		checkAndCancelRule(ExileRule.CHAT, e, e.getPlayer());
	}
}
