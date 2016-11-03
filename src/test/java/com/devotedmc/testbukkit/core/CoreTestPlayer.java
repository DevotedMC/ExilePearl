package com.devotedmc.testbukkit.core;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.inventory.PlayerInventory;

import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestInventory;
import com.devotedmc.testbukkit.TestPlayer;
import com.devotedmc.testbukkit.TestServer;
import com.devotedmc.testbukkit.annotation.ProxyMock;
import com.devotedmc.testbukkit.annotation.ProxyStub;

@ProxyMock(TestPlayer.class)
class CoreTestPlayer extends TestProxyBase {

	private final TestServer server;
	private final TestPlayer player;
	private final String name;
	private UUID uid;
	private Location location;
	private PlayerInventory inventory;
	private Queue<String> messages;
	
	private CoreTestPlayer(String name, UUID uid) throws Exception {
		super(Player.class);
		this.name = name;
		this.uid = uid;
		this.server = TestBukkit.getServer();
		location = server.getWorld("world").getSpawnLocation();
		messages = new LinkedBlockingQueue<String>();
		player = createProxyInstance(TestPlayer.class);
		inventory = (PlayerInventory) TestInventory.create(player, InventoryType.PLAYER);
	}
	
	public static TestPlayer createInstance(String name, UUID uid) {
		try {			
			CoreTestPlayer player = new CoreTestPlayer(name, uid);
			return player.player;
		} catch(Exception ex) {
            throw new Error(ex);
		}
	}

	public static TestPlayer createInstance(String name) {
		return createInstance(name, UUID.randomUUID());
	}	

	@ProxyStub
	public String getName() {
		return name;
	}

	@ProxyStub
	public UUID getUniqueId() {
		return uid;
	}

	@ProxyStub
	public boolean isOnline() {
		return TestBukkit.getServer().getOnlinePlayers().contains(this);
	}
	
	@ProxyStub
	public String toString() {
		return String.format("TestPlayer{ name: %s, uid: %s }", name, uid.toString());
	}

	public Location getLocation() {
		return location;
	}
	
	public Server getServer() {
		return TestBukkit.getServer();
	}

	public boolean connect() {
    	final InetSocketAddress address = new InetSocketAddress("localhost", 25565);
    	server.log("UUID of player %s is %s", player.getName(), uid.toString());
    	final AsyncPlayerPreLoginEvent preLoginEvent = new AsyncPlayerPreLoginEvent(name, address.getAddress(), uid);
    	TestBukkit.getPluginManager().callEvent(preLoginEvent);
    	if (preLoginEvent.getLoginResult() != Result.ALLOWED) {
    		return false;
    	}
    	
    	final PlayerLoginEvent loginEvent = new PlayerLoginEvent(player, "localhost", address.getAddress());
    	TestBukkit.getPluginManager().callEvent(loginEvent);

    	if (loginEvent.getResult() != PlayerLoginEvent.Result.ALLOWED) {
    		return false;
    	}
    	
    	final PlayerJoinEvent joinEvent = new PlayerJoinEvent(player, "");
    	TestBukkit.getPluginManager().callEvent(joinEvent);
		
    	Location l = player.getLocation();
    	server.log("%s logged in at ([%s]%f, %f, %f)", player.getName(), l.getWorld().getName(), l.getX(), l.getY(),l.getZ());
		server.addPlayer(player);
		return true;
	}

	public void disconnect() {
		TestBukkit.getServer().getOnlinePlayers().remove(this);
		
		final PlayerQuitEvent quitEvent = new PlayerQuitEvent(player, "");
    	TestBukkit.getPluginManager().callEvent(quitEvent);
    	server.log("%s left the game", player.getName());
	}
	
    public boolean runCommand(String commandLine) {
    	getServer().getLogger().log(Level.INFO, String.format("Running player command '%s'", commandLine));
    	return getServer().dispatchCommand(player, commandLine);
    }
    
    public void sendMessage(String message) {
    	if (message != null) {
        	messages.add(message);
    	}
    }
    
    public void sendMessage(String[] messages) {
    	for(String s : messages) {
    		sendMessage(s);
    	}
    }
    
    public Queue<String> getMessages() {
    	return messages;
    }
    
    public PlayerInventory getInventory() {
    	return inventory;
    }
}
