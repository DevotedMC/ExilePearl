package com.devotedmc.testbukkit.core;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.inventory.PlayerInventory;

import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestPlayer;

abstract class CoreTestPlayer implements TestPlayer {

	private String name;
	private UUID uid;
	private Location location;
	private PlayerInventory inventory;
	private Queue<String> messages;
	
	public static TestPlayer createInstance(String name, UUID uid) {
		CoreTestPlayer player = mock(CoreTestPlayer.class);
		player.name = name;
		player.uid = uid;
		player.location = null;
		player.inventory = mock(PlayerInventory.class);
		player.messages = new LinkedBlockingQueue<String>();
		
		when(player.getName()).thenCallRealMethod();
		when(player.getUniqueId()).thenCallRealMethod();
		when(player.isOnline()).thenCallRealMethod();
		when(player.toString()).thenCallRealMethod();
		when(player.getServer()).thenCallRealMethod();
		when(player.runCommand(any())).thenCallRealMethod();
		when(player.connect()).thenCallRealMethod();
		doCallRealMethod().when(player).disconnect();
		when(player.getInventory()).thenReturn(player.inventory);		
		doCallRealMethod().when(player).sendMessage(anyString());
		doCallRealMethod().when(player).sendMessage(any(String[].class));
		
		when(player.getLocation()).thenCallRealMethod();
		
		return player;
		
	}

	public static TestPlayer createInstance(String name) {
		return createInstance(name, UUID.randomUUID());
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public UUID getUniqueId() {
		return uid;
	}
	
	@Override
	public boolean isOnline() {
		return TestBukkit.getServer().getOnlinePlayers().contains(this);
	}
	
	@Override
	public String toString() {
		return String.format("TestPlayer{ name: %s, uid: %s }", name, uid.toString());
	}

	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public Server getServer() {
		return TestBukkit.getServer();
	}

	@Override
	public boolean connect() {
    	final InetSocketAddress address = new InetSocketAddress("localhost", 25565);
    	final AsyncPlayerPreLoginEvent preLoginEvent = new AsyncPlayerPreLoginEvent(name, address.getAddress(), uid);
    	TestBukkit.getPluginManager().callEvent(preLoginEvent);
    	if (preLoginEvent.getLoginResult() != Result.ALLOWED) {
    		return false;
    	}
    	
    	final PlayerLoginEvent loginEvent = new PlayerLoginEvent(this, "localhost", address.getAddress());
    	TestBukkit.getPluginManager().callEvent(loginEvent);

    	if (loginEvent.getResult() != PlayerLoginEvent.Result.ALLOWED) {
    		return false;
    	}
    	
    	final PlayerJoinEvent joinEvent = new PlayerJoinEvent(this, "");
    	TestBukkit.getPluginManager().callEvent(joinEvent);
		
		TestBukkit.getServer().addPlayer(this);
		return true;
	}

	@Override
	public void disconnect() {
		TestBukkit.getServer().getOnlinePlayers().remove(this);
		
		final PlayerQuitEvent quitEvent = new PlayerQuitEvent(this, "");
    	TestBukkit.getPluginManager().callEvent(quitEvent);
	}
	
	@Override
    public boolean runCommand(String commandLine) {
    	getServer().getLogger().log(Level.INFO, String.format("Running player command '%s'", commandLine));
    	return getServer().dispatchCommand(this, commandLine);
    }
    
    @Override
    public void sendMessage(String message) {
    	if (message != null) {
        	messages.add(message);
    	}
    }
    
    @Override
    public void sendMessage(String[] messages) {
    	for(String s : messages) {
    		sendMessage(s);
    	}
    }
    
    @Override
    public Queue<String> getMessages() {
    	return messages;
    }
}
