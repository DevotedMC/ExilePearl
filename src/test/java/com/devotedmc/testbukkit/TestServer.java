package com.devotedmc.testbukkit;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public interface TestServer extends Server, ServerProxy {
	
	void configureLogger(boolean useLogger);
	
	void loadPlugins();
	
	void enablePlugins();
	
	void disablePlugins();
	
	@Override
	TestPluginManager getPluginManager();
	
	ProxyFactory getProxyFactory();
	
    World createTestWorld(WorldCreator creator);
    
    <T extends JavaPlugin> TestPlugin<T> addPlugin(Class<T> clazz) throws Exception;
    
    void addPlayer(TestPlayer p);
    
    PluginLoader getPluginLoader();
    
    PluginDescriptionFile getDescription(JavaPlugin plugin) throws InvalidDescriptionException;
    
    FileConfiguration getPluginConfig(JavaPlugin plugin);
    
    File getTestPluginDataFolder(JavaPlugin plugin);
    
    File getTestPluginFile(JavaPlugin plugin);
    
    File getTestPluginConfigFile(JavaPlugin plugin);
    
    void addProxyHandler(Class<?> clazz, Object handler);
    
    Object invokeProxy(Class<?> proxyClass, Object proxy, Method method, Object[] args) throws Throwable;
    
    void createWorlds() ;
    
    @Override
    TestWorld getWorld(String name);
    
	/**
	 * Logs a message
	 * @param level The logging level
	 * @param msg The message
	 * @param args The message arguments
	 */
	void log(Level level, String msg, Object... args);
	
	/**
	 * Logs a message
	 * @param msg The message
	 * @param args The message arguments
	 */
	void log(String msg, Object... args);
}
