package com.devotedmc.testbukkit;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public interface TestServer extends Server {
	
	void configureLogger(boolean useLogger);
	
	void loadPlugins();
	
	void enablePlugins();
	
	void disablePlugins();
	
	@Override
	TestPluginManager getPluginManager();
	
	TestFactory getTestFactory();
	
    World createTestWorld(WorldCreator creator);
    
    <T extends JavaPlugin> TestPlugin<T> addPlugin(Class<T> clazz) throws Exception;
    
    void addPlayer(TestPlayer p);
    
    PluginLoader getPluginLoader();
    
    PluginDescriptionFile getDescription(JavaPlugin plugin) throws InvalidDescriptionException;
    
    FileConfiguration getPluginConfig(JavaPlugin plugin);
    
    File getTestPluginDataFolder(JavaPlugin plugin);
    
    File getTestPluginFile(JavaPlugin plugin);
    
    File getTestPluginConfigFile(JavaPlugin plugin);
    
    void addMethodHandler(Class<?> clazz, TestMethodHandler handler);
    
    Set<TestMethodHandler> getMethodHandlers(Class<?> clazz);
}
