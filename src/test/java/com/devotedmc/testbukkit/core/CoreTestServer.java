package com.devotedmc.testbukkit.core;

import static org.mockito.Mockito.*;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitWorker;
import org.mockito.Mockito;

import com.devotedmc.testbukkit.TestCommandMap;
import com.devotedmc.testbukkit.TestConsoleCommandSender;
import com.devotedmc.testbukkit.ProxyFactory;
import com.devotedmc.testbukkit.TestItemFactory;
import com.devotedmc.testbukkit.TestMethodHandler;
import com.devotedmc.testbukkit.TestPlayer;
import com.devotedmc.testbukkit.TestPlugin;
import com.devotedmc.testbukkit.TestPluginManager;
import com.devotedmc.testbukkit.TestScheduler;
import com.devotedmc.testbukkit.TestServer;
import com.devotedmc.testbukkit.TestWorld;
import com.devotedmc.testbukkit.annotation.ProxyStub;

class CoreTestServer extends ProxyMockBase<TestServer> {
	
	private final TestServer server;
    private final String serverName = "TestBukkit";
    private final String serverVersion = "0.0.0";
    private final String bukkitVersion = "0.0.0";
    private Logger logger;
    private final ServicesManager servicesManager = new SimpleServicesManager();
    private final TestCommandMap commandMap;
    private final StandardMessenger messenger = new StandardMessenger();
    private final TestPluginManager pluginManager;
    private final TestScheduler scheduler = spy(new TestScheduler());
    private final TestItemFactory itemFactory = spy(new TestItemFactory());
    private final Map<String, TestWorld> worlds = new LinkedHashMap<String, TestWorld>();
    private List<TestPlayer> onlinePlayers = new LinkedList<TestPlayer>();
    private List<OfflinePlayer> offlinePlayers = new LinkedList<OfflinePlayer>();
    private TestConsoleCommandSender consoleSender;
	private Set<Recipe> recipes = new HashSet<Recipe>();
	private PluginLoader pluginLoader = Mockito.mock(PluginLoader.class);
	private CoreProxyFactory testFactory = new CoreProxyFactory();
	private Map<Class<?>, List<TestMethodHandler>> proxyHandlers = new HashMap<Class<?>, List<TestMethodHandler>>();
    private int maxPlayers = 50;
    private int viewDistance = 4;
    private boolean allowNether = true;
    private boolean allowEnd = true;
	private boolean allowFlight = false;
	private boolean isHardcore = false;
	private boolean useLogger = false;;
    
    /**
     * Use @RunWith(TestBukkitRunner.class) to use this class
     * @param useLogger
     */
	public CoreTestServer(boolean useLogger) {
		super(TestServer.class);
        addProxyHandler(TestServer.class, this);
		server = getProxy();
		commandMap = new TestCommandMap(server);
		pluginManager = spy(new TestPluginManager(server, commandMap));
		
		configureLogger(useLogger);
        
        // Create default worlds
        createTestWorld(new WorldCreator("world"));
        createTestWorld(new WorldCreator("world_nether"));
        createTestWorld(new WorldCreator("world_the_end"));
        
        consoleSender = new TestConsoleCommandSender();
    }
    
    public CoreTestServer() {
    	this(false);
    }
    

	@ProxyStub
    public void configureLogger(boolean useLogger) {
    	if (this.useLogger != useLogger && logger != null) {
    		return;
    	}
    	this.useLogger = useLogger;
    	
    	if (useLogger) {
    		logger = Logger.getLogger(serverName);
    	} else {
    		logger = Mockito.mock(Logger.class);
    		return;
    	}
    	
		// Format the logger output
		Formatter formatter = new Formatter() {
			private final DateFormat df = new SimpleDateFormat("hh:mm:ss");
			@ProxyStub
			public String format(LogRecord record) {
				String level = record.getLevel().getLocalizedName().toUpperCase();
				if (level.equals("WARNING")) {
					level = "WARN";
				}
				
				Throwable thrown = record.getThrown();
				if (thrown != null) {
					thrown.printStackTrace();
				}
				
				return String.format("[%s %s]: %s\n", df.format(new Date(record.getMillis())), level, formatMessage(record));
			}
		};
		
		logger.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        logger.addHandler(handler);
    }
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return invokeProxy(TestServer.class, proxy, method, args);
	}

    @ProxyStub
    public Object invokeProxy(Class<?> proxyClass, Object proxy, Method method, Object[] args) throws Throwable {
    	List<TestMethodHandler> handlers = proxyHandlers.get(proxyClass);
    	if (handlers == null) {
    		return null;
    	}
    	
    	// Iterate backwards so the most recently added handler is called
    	ListIterator<TestMethodHandler> li = handlers.listIterator(handlers.size());
    	while(li.hasPrevious()) {
    		TestMethodHandler handler = li.previous();
			try {
				return handler.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(handler, args);
			} catch(NoSuchMethodException ex) {
				continue;
			}
    	}
    	
    	// Progressively try to find a handler for the class interfaces
    	for(Class<?> in : proxyClass.getInterfaces()) {
    		handlers = proxyHandlers.get(in);
        	if (handlers == null) {
        		continue;
        	}
        	
        	li = handlers.listIterator(handlers.size());
        	while(li.hasPrevious()) {
        		TestMethodHandler handler = li.previous();
    			try {
    				return handler.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(handler, args);
    			} catch(NoSuchMethodException ex) {
    				continue;
    			}
        	}
    	}
    	
    	if (method.getReturnType().isPrimitive()) {
    		return DefaultValues.defaultValueFor(method.getReturnType());
    	}
    	return null;
    }
    


	@ProxyStub
	public void reload() {
        pluginManager.clearPlugins();
        commandMap.clearCommands();

        List<BukkitWorker> overdueWorkers = getScheduler().getActiveWorkers();
        for (BukkitWorker worker : overdueWorkers) {
            Plugin plugin = worker.getOwner();
            String author = "<NoAuthorGiven>";
            if (plugin.getDescription().getAuthors().size() > 0) {
                author = plugin.getDescription().getAuthors().get(0);
            }
            getLogger().log(Level.SEVERE, String.format(
                "Nag author: '%s' of '%s' about the following: %s",
                author,
                plugin.getDescription().getName(),
                "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin"
            ));
        }
        loadPlugins();
        enablePlugins();

	}

	@ProxyStub
    public void loadPlugins() {
        Plugin[] plugins = pluginManager.loadPlugins();
        for (Plugin plugin : plugins) {
            try {
                String message = String.format("Loading %s", plugin.getDescription().getFullName());
                plugin.getLogger().info(message);
                plugin.onLoad();
            } catch (Throwable ex) {
                logger.log(Level.SEVERE, ex.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
            }
        }
    }

	@ProxyStub
    public void enablePlugins() {
        Plugin[] plugins = pluginManager.getPlugins();

        for (Plugin plugin : plugins) {
            if (!plugin.isEnabled()) {
                enablePlugin(plugin);
            }
        }
    }
    
    private void enablePlugin(Plugin plugin) {
        try {
            List<Permission> perms = plugin.getDescription().getPermissions();

            for (Permission perm : perms) {
                try {
                    pluginManager.addPermission(perm);
                } catch (IllegalArgumentException ex) {
                    getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
                }
            }

            pluginManager.enablePlugin(plugin);
        } catch (Throwable ex) {
            logger.log(Level.SEVERE, ex.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
    }

	@ProxyStub
    public void disablePlugins() {
        pluginManager.disablePlugins();
    }

	@ProxyStub
	public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(getMessenger(), source, channel, message);

        for (Player player : getOnlinePlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
	}

	@ProxyStub
	public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getOnlinePlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
	}

	@ProxyStub
	public String getName() {
        return serverName;
	}

	@ProxyStub
	public String getVersion() {
        return serverVersion;
	}

	@ProxyStub
	public String getBukkitVersion() {
        return bukkitVersion;
	}

	@ProxyStub
	public Collection<? extends Player> getOnlinePlayers() {
		return onlinePlayers;
	}

	@ProxyStub
	public int getMaxPlayers() {
        return maxPlayers;
	}

	@ProxyStub
	public int getViewDistance() {
		return viewDistance;
	}

	@ProxyStub
	public String getIp() {
		return "localhost";
	}
	
	@ProxyStub
	public TestServer getServer() {
		return server;
	}

	@ProxyStub
	public String getServerName() {
		return serverName;
	}

	@ProxyStub
	public String getServerId() {
		return "";
	}

	@ProxyStub
	public String getWorldType() {
		return "DEFAULT";
	}

	@ProxyStub
	public boolean getAllowEnd() {
		return allowEnd;
	}

	@ProxyStub
	public boolean getAllowNether() {
		return allowNether;
	}

	@ProxyStub
	public Set<OfflinePlayer> getWhitelistedPlayers() {
		return new LinkedHashSet<OfflinePlayer>();
	}

	@ProxyStub
	public int broadcastMessage(String message) {
        return broadcast(message, "");
	}

	@ProxyStub
	public String getUpdateFolder() {
		return "update";
	}

	@ProxyStub
	public Player getPlayer(String name) {
		for(Player p : onlinePlayers) {
			if (p.getName() == name) {
				return p;
			}
		}
		return null;
	}

	@ProxyStub
	public Player getPlayerExact(String name) {
		for(Player p : onlinePlayers) {
			if (p.getName() == name) {
				return p;
			}
		}
		return null;
	}

	@ProxyStub
    @Deprecated
	public List<Player> matchPlayer(String partialName) {
        Validate.notNull(partialName, "PartialName cannot be null");

        List<Player> matchedPlayers = new ArrayList<Player>();

        for (Player iterPlayer : this.getOnlinePlayers()) {
            String iterPlayerName = iterPlayer.getName();

            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                // Exact match
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }
            if (iterPlayerName.toLowerCase(java.util.Locale.ENGLISH).contains(partialName.toLowerCase(java.util.Locale.ENGLISH))) {
                // Partial match
                matchedPlayers.add(iterPlayer);
            }
        }

        return matchedPlayers;
	}

	@ProxyStub
	public Player getPlayer(UUID id) {
		for(Player p : onlinePlayers) {
			if (p.getUniqueId() == id) {
				return p;
			}
		}
		return null;
	}

	@ProxyStub
	public TestPluginManager getPluginManager() {
		return pluginManager;
	}

	@ProxyStub
	public BukkitScheduler getScheduler() {
        return scheduler;
	}

	@ProxyStub
	public ServicesManager getServicesManager() {
        return servicesManager;
	}

	@ProxyStub
	public List<World> getWorlds() {
        return new ArrayList<World>(worlds.values());
	}
	
	public List<TestWorld> getTestWorlds() {
        return new ArrayList<TestWorld>(worlds.values());
	}

	@ProxyStub
	public World createWorld(WorldCreator creator) {
		throw new UnsupportedOperationException();
	}

	@ProxyStub
	public boolean unloadWorld(String name, boolean save) {
		return true;
	}

	@ProxyStub
	public boolean unloadWorld(World world, boolean save) {
		return true;
	}

	@ProxyStub
	public World getWorld(String name) {
        Validate.notNull(name, "Name cannot be null");
        return worlds.get(name.toLowerCase(java.util.Locale.ENGLISH));
	}

	@ProxyStub
	public World getWorld(UUID uid) {
        for (World world : worlds.values()) {
            if (world.getUID().equals(uid)) {
                return world;
            }
        }
        return null;
	}

	@ProxyStub
	public Logger getLogger() {
        return logger;
	}

	@ProxyStub
	public PluginCommand getPluginCommand(String name) {
        Command command = commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand) command;
        } else {
            return null;
        }
	}

	@ProxyStub
	public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(commandLine, "CommandLine cannot be null");

        if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        sender.sendMessage("Unknown command. Type \"/help\" for help.");

        return false;
	}

	@ProxyStub
	public boolean addRecipe(Recipe recipe) {
		recipes.add(recipe);
		return true;
	}

	@ProxyStub
	public List<Recipe> getRecipesFor(ItemStack result) {
        Validate.notNull(result, "Result cannot be null");

        List<Recipe> results = new ArrayList<Recipe>();
        Iterator<Recipe> iter = recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            ItemStack stack = recipe.getResult();
            if (stack.getType() != result.getType()) {
                continue;
            }
            if (result.getDurability() == -1 || result.getDurability() == stack.getDurability()) {
                results.add(recipe);
            }
        }
        return results;
	}

	@ProxyStub
	public Iterator<Recipe> recipeIterator() {
		return recipes.iterator();
	}

	@ProxyStub
	public boolean getAllowFlight() {
		return allowFlight;
	}

	@ProxyStub
	public boolean isHardcore() {
		return isHardcore;
	}

	@ProxyStub
	public int broadcast(String message, String permission) {
        int count = 0;
        Set<Permissible> permissibles = getPluginManager().getPermissionSubscriptions(permission);

        for (Permissible permissible : permissibles) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                CommandSender user = (CommandSender) permissible;
                user.sendMessage(message);
                count++;
            }
        }

        return count;
	}

	@ProxyStub
	public OfflinePlayer getOfflinePlayer(String name) {
		for(OfflinePlayer p : offlinePlayers) {
			if (p.getName() == name) {
				return p;
			}
		}
		return null;
	}

	@ProxyStub
	public OfflinePlayer getOfflinePlayer(UUID id) {
		for(OfflinePlayer p : offlinePlayers) {
			if (p.getUniqueId() == id) {
				return p;
			}
		}
		return null;
	}

	@ProxyStub
	public Set<String> getIPBans() {
		return new HashSet<String>();
	}

	@ProxyStub
	public Set<OfflinePlayer> getBannedPlayers() {
		return new HashSet<OfflinePlayer>();
	}

	@ProxyStub
	public Set<OfflinePlayer> getOperators() {
		return new HashSet<OfflinePlayer>();
	}

	@ProxyStub
	public GameMode getDefaultGameMode() {
		return GameMode.SURVIVAL;
	}

	@ProxyStub
	public ConsoleCommandSender getConsoleSender() {
		return consoleSender;
	}

	@ProxyStub
	public OfflinePlayer[] getOfflinePlayers() {
		return new OfflinePlayer[0];
	}

	@ProxyStub
	public Messenger getMessenger() {
        return messenger;
	}

	@ProxyStub
	public boolean isPrimaryThread() {
		return true;
	}

	@ProxyStub
	public String getMotd() {
		return "";
	}

	@ProxyStub
	public String getShutdownMessage() {
		return "";
	}

	@ProxyStub
	public WarningState getWarningState() {
		return WarningState.DEFAULT;
	}

	@ProxyStub
	public ItemFactory getItemFactory() {
		return itemFactory;
	}
	
    public World createTestWorld(WorldCreator creator) {    	
    	TestWorld testWorld = TestWorld.create(creator.name(), creator.environment(), creator.type());
    	worlds.put(testWorld.getName(), testWorld);
    	return testWorld;
    }
    
    public <T extends JavaPlugin> TestPlugin<T> addPlugin(Class<T> clazz) throws Exception {
    	return pluginManager.addPlugin(clazz);
    }
    
    public void addPlayer(TestPlayer p) {
    	this.onlinePlayers.add(p);
    }
    
    public PluginLoader getPluginLoader() {
    	return pluginLoader;
    }
    
    public PluginDescriptionFile getDescription(JavaPlugin plugin) throws InvalidDescriptionException {
    	return pluginManager.createDescription(plugin);
    }
    
    public FileConfiguration getPluginConfig(JavaPlugin plugin) {
    	return pluginManager.getPluginConfig(plugin);
    }
    
    public File getTestPluginDataFolder(JavaPlugin plugin) {
    	String name = plugin.getName();
    	File file = mock(File.class);
    	when(file.getPath()).thenReturn("\\plugins\\" + name);
    	when(file.getAbsolutePath()).thenReturn("\\plugins\\" + name);
    	when(file.getName()).thenReturn("\\plugins\\" + name);
    	when(file.getParent()).thenReturn("\\plugins\\");
    	when(file.exists()).thenReturn(true);
    	when(file.isFile()).thenReturn(false);
    	when(file.isDirectory()).thenReturn(true);
    	return file;
    }
    
    public File getTestPluginFile(JavaPlugin plugin) {
    	String name = plugin.getName() + ".jar";
    	File file = mock(File.class);
    	when(file.getPath()).thenReturn("\\plugins\\" + name);
    	when(file.getAbsolutePath()).thenReturn("\\plugins\\" + name);
    	when(file.getName()).thenReturn("\\plugins\\" + name);
    	when(file.getParent()).thenReturn("\\plugins\\");
    	when(file.exists()).thenReturn(true);
    	when(file.isFile()).thenReturn(true);
    	when(file.isDirectory()).thenReturn(false);
    	return file;
    }
    
    public File getTestPluginConfigFile(JavaPlugin plugin) {
    	String name = plugin.getName();
    	File file = mock(File.class);
    	when(file.getPath()).thenReturn("\\plugins\\" + name + "\\config.yml");
    	when(file.getAbsolutePath()).thenReturn("\\plugins\\" + name + "\\config.yml");
    	when(file.getName()).thenReturn("\\plugins\\" + name + "\\config.yml");
    	when(file.getParent()).thenReturn("\\plugins\\" + name);
    	when(file.exists()).thenReturn(true);
    	when(file.isFile()).thenReturn(true);
    	when(file.isDirectory()).thenReturn(false);
    	return file;
    }
    
    @ProxyStub
    public ProxyFactory getTestFactory() {
    	return testFactory;
    }

    @ProxyStub
    public void addProxyHandler(Class<?> clazz, TestMethodHandler handler) {
    	List<TestMethodHandler> handlers = proxyHandlers.get(clazz);
    	if (handlers == null) {
    		handlers = new ArrayList<TestMethodHandler>();
    		proxyHandlers.put(clazz, handlers);
    	}
    	if (!handlers.contains(handler)) {
    		handlers.add(handler);
    	}
    }
    
	@ProxyStub
	public void log(Level level, String msg, Object... args) {
		logger.log(level, String.format(msg, args));
	}

	@ProxyStub
	public void log(String msg, Object... args) {
		logger.log(Level.INFO, String.format(msg, args));
	}
	
	@Override
	public String toString() {
		return "CoreTestServer - " + getVersion();
	}
}
