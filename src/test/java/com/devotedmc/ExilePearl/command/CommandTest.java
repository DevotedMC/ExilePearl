package com.devotedmc.ExilePearl.command;

import static com.devotedmc.testbukkit.TestBukkit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.devotedmc.ExilePearl.ExilePearl;
import com.devotedmc.ExilePearl.ExilePearlApi;
import com.devotedmc.ExilePearl.LoreProvider;
import com.devotedmc.ExilePearl.PearlFreeReason;
import com.devotedmc.ExilePearl.PearlManager;
import com.devotedmc.ExilePearl.config.PearlConfig;
import com.devotedmc.ExilePearl.holder.PearlHolder;
import com.devotedmc.testbukkit.TestBlock;
import com.devotedmc.testbukkit.TestBukkitRunner;
import com.devotedmc.testbukkit.TestPlayer;
import com.devotedmc.testbukkit.TestServer;
import com.devotedmc.testbukkit.TestWorld;
import com.devotedmc.testbukkit.annotation.ProxyStub;

import vg.civcraft.mc.civmodcore.util.TextUtil;

@RunWith(TestBukkitRunner.class)
public class CommandTest {
	
	private TestServer server;
	private HashSet<BaseCommand<?>> commands = new HashSet<BaseCommand<?>>();
	private ExilePearlApi pearlApi;
	private LoreProvider loreProvider;
	private PearlManager pearlManager;
	private TestPlayer player1;
	private TestPlayer player2;
	private TestPlayer player3;
	private ExilePearl pearl;
	
	private List<String> testArgs;
	private List<String> testArgsExt;

	@Before
	public void setUp() throws Exception {
		server = getServer();
		server.addProxyHandler(Player.class, this);
		
		player1 = createPlayer("Player1");
		player1.connect();
		player2 = createPlayer("Player2");
		player3 = createPlayer("Player3");
		
		// A variety of different argument values to test
		testArgs = Arrays.asList("-10", "0", "1", "3.1415", "world", "true", "false", "null", player2.getUniqueId().toString(), player3.getUniqueId().toString());
		testArgsExt = Arrays.asList("world", "-5", "15");
		
		pearlApi = mock(ExilePearlApi.class);
		when(pearlApi.getPlayer(player1.getName())).thenReturn(player1);
		when(pearlApi.getPlayer(player1.getUniqueId())).thenReturn(player1);
		when(pearlApi.getRealPlayerName(player1.getUniqueId())).thenReturn(player1.getName());
		when(pearlApi.getPlayer(player2.getName())).thenReturn(player2);
		when(pearlApi.getPlayer(player2.getUniqueId())).thenReturn(player2);
		when(pearlApi.getRealPlayerName(player2.getUniqueId())).thenReturn(player2.getName());
		when(pearlApi.getPlayer(player3.getName())).thenReturn(player3);
		when(pearlApi.getPlayer(player3.getUniqueId())).thenReturn(player3);
		when(pearlApi.getRealPlayerName(player3.getUniqueId())).thenReturn(player3.getName());
		when(pearlApi.getServer()).thenReturn(server);
		
		CmdAutoHelp autoHelp = new CmdAutoHelp(pearlApi);
		when(pearlApi.getAutoHelp()).thenReturn(autoHelp);
		
		PearlConfig config = mock(PearlConfig.class);
		when(config.getPearlHealthMaxValue()).thenReturn(100);
		when(pearlApi.getPearlConfig()).thenReturn(config);
		
		pearlManager = mock(PearlManager.class);
		when(pearlApi.getPearlManager()).thenReturn(pearlManager);
		
		loreProvider = mock(LoreProvider.class);
		when(loreProvider.generateLore(any(ExilePearl.class))).thenReturn(new ArrayList<String>());
		when(loreProvider.generatePearlInfo(any(ExilePearl.class))).thenReturn(new ArrayList<String>());
		
		when(pearlApi.getLoreProvider()).thenReturn(loreProvider);
		
		pearl = mock(ExilePearl.class);
		when(pearl.getPlayer()).thenReturn(player2);
		when(pearl.getPlayerName()).thenReturn(player2.getName());
		when(pearl.getPlayerId()).thenReturn(player2.getUniqueId());
		when(pearl.getKillerName()).thenReturn(player1.getName());
		when(pearl.getKillerId()).thenReturn(player1.getUniqueId());
		
		when(pearlApi.getPearl(player2.getName())).thenReturn(pearl);
		when(pearlApi.getPearl(player2.getUniqueId())).thenReturn(pearl);
		when(pearlApi.isPlayerExiled(player2)).thenReturn(true);
		when(pearlApi.isPlayerExiled(player2.getUniqueId())).thenReturn(true);
		when(pearlApi.getPearls()).thenReturn(Arrays.asList(pearl));

		commands.clear();
		commands.add(new CmdExilePearl(pearlApi));
	}

	@Ignore
	@Test
	public void testParameters() throws Exception {
		testParamArgs("ep decay");
		testParamArgs("ep sethealth");
		testParamArgs("ep exileany");
		testParamArgs("ep freeany");
		testParamArgs("ep list");
		testParamArgs("ep reload");
		testParamArgs("ep setkiller");
		testParamArgs("ep settype");
		testParamArgs("ep help");
		testParamArgs("ep config");
		testParamArgs("ep config list");
		testParamArgs("ep config load");
		testParamArgs("ep config save");
		testParamArgs("ep config set");
		testParamArgs(player2, "ep broadcast");
		testParamArgs("ep confirm");
		testParamArgs("ep silence");
		testParamArgs("ep free");
		testParamArgs(player2, "ep locate");
	}
	
	@Test
	public void testCheck() throws Exception {
		runCommand("ep check " + player2.getUniqueId());
		verify(loreProvider).generatePearlInfo(pearl);
		
		testParamArgs("ep check");
	}
	
	@Test
	public void testDecay() throws Exception {
		runCommand("ep decay");
		verify(pearlManager).decayPearls();
		
		testParamArgs("ep decay");
	}

	@Ignore
	@Test
	public void testExileAny() throws Exception {
		ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<PearlHolder> arg3 = ArgumentCaptor.forClass(PearlHolder.class);
		
		runCommand("ep exileany %s", player3.getUniqueId());
		verify(pearlApi).exilePlayer(arg1.capture(), arg2.capture(), arg3.capture());
		assertEquals(player3.getUniqueId(), arg1.getValue());
		assertEquals(player1.getUniqueId(), arg2.getValue());
		assertEquals(player1.getName(), arg3.getValue().getName());
		
		testParamArgs("ep exileany");
	}
	
	@Test
	public void testExileAnyLocation() throws Exception {
		ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<Location> arg3 = ArgumentCaptor.forClass(Location.class);
		
		TestWorld world = server.getWorld("world");
		TestBlock block = world.getBlockAt(1, 2, 3);
		block.setType(Material.CHEST);
		
		runCommand("ep exileany %s %s world 1 2 3", player3.getUniqueId(), player1.getUniqueId());
		verify(pearlApi).exilePlayer(arg1.capture(), arg2.capture(), arg3.capture());
		assertEquals(player3.getUniqueId(), arg1.getValue());
		assertEquals(player1.getUniqueId(), arg2.getValue());
		assertEquals(block.getLocation(), arg3.getValue());
	}
	
	@Test
	public void testFreeAny() throws Exception {		
		runCommand("ep freeany %s", player2.getUniqueId());
		verify(pearlApi).freePearl(pearl, PearlFreeReason.FREED_BY_ADMIN);
		
		testParamArgs("ep freeany");
	}
	
	
	
	
	private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
	private static int MAX_ARGS = 4;
	private static String strTooManyArgs = TextUtil.parse("<b>Strange arg");
	
	private void testParamArgs(CommandSender sender, String commandLine) throws Exception {
    	System.out.print("Testing arguments for command '" + commandLine + "'\n");
		buildAndRunCommand(sender, 0, MAX_ARGS, commandLine);
	}
	
	private void testParamArgs(String commandLine) throws Exception {
		testParamArgs(player1, commandLine);
	}
	
	
	/**
	 * This recursive method calls the command using every possible set of arguments
	 * that exist in testArgs.
	 * It detects when too many args are sent so it doesn't perform unneeded tests.
	 * @param The call depth
	 * @param sequenceNum The sequence number
	 * @param commandLine The command string
	 * @return The sequence number that should be subtracted
	 * @throws Exception
	 */
	private int buildAndRunCommand(CommandSender sender, int depth, int sequenceNum, String commandLine) throws Exception {
		runCommand(sender, commandLine);
		String msg = player1.getMessages().poll();
		if (msg != null && msg.startsWith(strTooManyArgs)) {
			return sequenceNum + 1;
		}
		
		int retVal = 0;
		
		if (sequenceNum > 0) {
			int next = sequenceNum - 1;
			List<String> args = testArgs;
			if (depth > 2) {
				args = testArgsExt;
			}
			
			for(String testArg : args) {
				int ret = buildAndRunCommand(sender, depth + 1, next, commandLine + " " + testArg);
				if (ret > 0 && retVal == 0) {
					retVal = ret;
					next -= retVal;
					if (next < 0) {
						break;
					}
				}
			}
		}
		return retVal;
	}
	
    
    public boolean runCommand(CommandSender sender, String commandLine) throws Exception {		
        String[] args = PATTERN_ON_SPACE.split(commandLine);

        if (args.length == 0) {
            return false;
        }

        try {
        	player1.getMessages().clear();
        	//System.out.print("Running command: " + commandLine + "\n");
            return onCommand(player1, args[0], Arrays.copyOfRange(args, 1, args.length));
        } catch(Exception ex) {
        	ex.printStackTrace();
        	throw new Exception("Failed command: " + commandLine, ex);
        }
	}
    
    public boolean runCommand(CommandSender sender, String commandLine, Object... args) throws Exception {
		return runCommand(sender, String.format(commandLine, args));
    }
    
    public boolean runCommand(String commandLine, Object... args) throws Exception {
		return runCommand(player1, String.format(commandLine, args));
    }
    
    public boolean runCommand(String commandLine) throws Exception {
		return runCommand(player1, commandLine);
    }
	
	public boolean onCommand(CommandSender sender, String alias, String[] args) {
		for (BaseCommand<?> c : commands) {
			List<String> aliases = c.getAliases();
			if (aliases.contains(alias)) {
				
				c.execute(sender, new ArrayList<String>(Arrays.asList(args)));
				return true;
			}
		}
		
		return false;
	}
	
	
	public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
		for (BaseCommand<?> c : commands) {
			List<String> aliases = c.getAliases();
			if (aliases.contains(alias)) {
				
				return c.getTabList(sender, new ArrayList<String>(Arrays.asList(args)));
			}
		}
		return null;
	}
	
	@ProxyStub(Player.class)
	public boolean hasPermission(String perm) {
		return true;
	}
}
