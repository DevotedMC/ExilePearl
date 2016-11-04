package com.devotedmc.testbukkit;

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public final class TestBukkit {
    private static TestServer server;

    /**
     * Static class cannot be initialized.
     */
    private TestBukkit() { }

    /**
     * Gets the current {@link TestServer} singleton
     *
     * @return TestServer instance being ran
     */
    public static TestServer getServer() {
        return server;
    }
    
    /**
     * Attempts to set the {@link Server} singleton.
     * <p>
     * This cannot be done if the Server is already set.
     *
     * @param server Server instance
     */
    public static void setServer(TestServer server) {
        Bukkit.setServer(server);
        TestBukkit.server = server;
    }
    
    /**
     * Creates a test player instance
     * @param name The player name
     * @param uid The player UUID
     * @return The test player instance
     */
    public static TestPlayer createPlayer(String name, UUID uid) {
    	return getProxyFactory().createPlayer(name, uid);
    }
    
    /**
     * Creates a test player instance with a random UUID
     * @param name The player name
     * @return The test player instance
     */
    public static TestPlayer createPlayer(String name) {
    	return getProxyFactory().createPlayer(name);
    }
    
    /**
     * Executes a console command
     * @param commandLine The command
     * @return The command result
     */
    public static boolean consoleCommand(String commandLine) {
    	server.getLogger().log(Level.INFO, String.format("Running console command '%s'", commandLine));
    	return getServer().dispatchCommand(getServer().getConsoleSender(), commandLine);
    }
    
    /**
     * Gets the plugin manager
     * @return The plugin manager
     */
    public static TestPluginManager getPluginManager() {
    	return server.getPluginManager();
    }
    
    /**
     * Gets the test factory
     * @return The test factory
     */
    public static ProxyFactory getProxyFactory() {
    	return server.getProxyFactory();
    }
}
