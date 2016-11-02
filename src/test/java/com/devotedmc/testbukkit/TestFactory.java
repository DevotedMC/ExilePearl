package com.devotedmc.testbukkit;

import java.util.UUID;

public interface TestFactory {

	/**
	 * Creates a test player instance
	 * @param name The player name
	 * @param uid The player UUID
	 * @return The test player instance
	 */
	TestPlayer createPlayer(String name, UUID uid);
	
	/**
	 * Creates a test player instance
	 * @param name The player name
	 * @param uid The player UUID
	 * @return The test player instance
	 */
	TestPlayer createPlayer(String name);
}
