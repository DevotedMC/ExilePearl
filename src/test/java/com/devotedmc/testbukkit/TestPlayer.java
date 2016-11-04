package com.devotedmc.testbukkit;

import java.util.Queue;

import org.bukkit.entity.Player;

public interface TestPlayer extends Player {
	
	/**
	 * Connects the player to the server calling all relevant
	 * Bukkit connection events.
	 * @return true if the player connected, otherwise false
	 */
	public boolean connect();
	
	/**
	 * Disconnects the player from the server if he is connected
	 */
	public void disconnect();
	
	/**
	 * Runs a command from the player
	 * @param commandLine The command
	 * @return true if the command was executed
	 */
	boolean runCommand(String commandLine);
	
	/**
	 * Gets the messages sent to the player
	 * @return The message queue
	 */
	Queue<String> getMessages();
	
	/**
	 * Gets the last message sent to the player and removes it from the message queue.
	 * @return The last message sent to the player
	 */
	String pollMessage();
	
	/**
	 * Clears the stored messages
	 */
	void clearMessages();
}
