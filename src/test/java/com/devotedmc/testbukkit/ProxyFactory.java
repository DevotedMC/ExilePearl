package com.devotedmc.testbukkit;

import java.util.UUID;

public interface ProxyFactory {
	
	/**
	 * Registers a proxy class
	 * @param proxy The proxy class
	 */
	void registerProxy(Class<? extends ProxyMock<?>> proxy);

	/**
	 * Creates a new proxy instance
	 * @param clazz The target class
	 * @param args The proxy arguments
	 * @return The proxy instance
	 */
	<T> T createInstance(Class<T> clazz, Object... args);

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
