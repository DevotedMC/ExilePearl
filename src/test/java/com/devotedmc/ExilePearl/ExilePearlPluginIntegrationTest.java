package com.devotedmc.ExilePearl;

import static com.devotedmc.testbukkit.TestBukkit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devotedmc.testbukkit.TestBukkitRunner;
import com.devotedmc.testbukkit.TestPlayer;
import com.devotedmc.testbukkit.TestPlugin;
import com.devotedmc.testbukkit.TestServer;
import com.devotedmc.testbukkit.annotation.TestOptions;
import com.devotedmc.testbukkit.v1_10_R1.TestServer_v1_10_R1;

import vg.civcraft.mc.civmodcore.util.TextUtil;

@SuppressWarnings("unused")
@RunWith(TestBukkitRunner.class)
@TestOptions(useLogger = true, server = TestServer_v1_10_R1.class, isIntegration = true)
public class ExilePearlPluginIntegrationTest {
	
	private static TestServer server;
	private static TestPlugin<ExilePearlPlugin> testPlugin;
	
	private TestPlayer player1;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		server = getServer();
		
		// Use MySQL for the integration test
		testPlugin = server.addPlugin(ExilePearlPlugin.class)
			.appendConfig("storage.type", 1) // MySQL type
			.appendConfig("storage.mysql.host", "localhost")
			.appendConfig("storage.mysql.dbname", "exilepearl")
			.appendConfig("storage.mysql.username", "bukkit");
		
		server.loadPlugins();
		server.enablePlugins();
		
		ExilePearlPlugin plugin = testPlugin.getInstance();
		
		
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		server.disablePlugins();
	}
	
	@Before
	public void setUp() throws Exception {
		server.getOnlinePlayers().clear();
		player1 = createPlayer("player1");
		player1.connect();
	}

	@Ignore
	@Test
	public void testConsoleCommands() {
		assertTrue(consoleCommand("ep"));
		
		assertTrue(consoleCommand("ep reload"));
		assertTrue(consoleCommand("ep check"));
		assertTrue(consoleCommand("ep decay"));
		assertTrue(consoleCommand("ep exileany"));
		assertTrue(consoleCommand("ep freeany"));
		assertTrue(consoleCommand("ep list"));
		assertTrue(consoleCommand("ep sethealth"));
		assertTrue(consoleCommand("ep setkiller"));
		assertTrue(consoleCommand("ep settype"));
		
		assertTrue(consoleCommand("ep config"));
		assertTrue(consoleCommand("ep config list"));
		assertTrue(consoleCommand("ep config load"));
		assertTrue(consoleCommand("ep config save"));
		assertTrue(consoleCommand("ep config set"));
		
		assertTrue(consoleCommand("ep broadcast"));
		assertTrue(consoleCommand("ep confirm"));
		assertTrue(consoleCommand("ep silence"));
		assertTrue(consoleCommand("ep free"));
		assertTrue(consoleCommand("ep locate"));
		
		assertTrue(consoleCommand("suicide"));
	}
	
	/**
	 * The command will always return true because it's being psuedo-executed
	 */
	@Test
	public void testPlayerCommands() {
		player1.clearMessages();
		
		assertTrue(player1.runCommand("ep reload"));
		assertTooManyArgs(player1, "reload");
		
		assertTrue(player1.runCommand("ep check"));
		assertTooManyArgs(player1, "check");
		
		assertTrue(player1.runCommand("ep decay"));
		assertTooManyArgs(player1, "decay");
		
		assertTrue(player1.runCommand("ep exileany"));
		assertTooManyArgs(player1, "exileany");
		
		assertTrue(player1.runCommand("ep freeany"));
		assertTooManyArgs(player1, "freeany");
		
		assertTrue(player1.runCommand("ep list"));
		assertTooManyArgs(player1, "list");
		
		assertTrue(player1.runCommand("ep sethealth"));
		assertTooManyArgs(player1, "sethealth");
		
		assertTrue(player1.runCommand("ep setkiller"));
		assertTooManyArgs(player1, "setkiller");
		
		assertTrue(player1.runCommand("ep settype"));
		assertTooManyArgs(player1, "settype");
		
		assertTrue(player1.runCommand("ep config"));
		assertTooManyArgs(player1, "config");
		
		assertTrue(player1.runCommand("ep config list"));
		assertTooManyArgs(player1, "config list");
		
		assertTrue(player1.runCommand("ep config load"));
		assertTooManyArgs(player1, "config load");
		
		assertTrue(player1.runCommand("ep config save"));
		assertTooManyArgs(player1, "config save");
		
		assertTrue(player1.runCommand("ep config set"));
		assertTooManyArgs(player1, "config set");
		
		player1.clearMessages();
		
		// These should pass
		assertTrue(player1.runCommand("ep broadcast"));
		assertNotTooManyArgs(player1, "config broadcast");
		
		assertTrue(player1.runCommand("ep confirm"));
		assertNotTooManyArgs(player1, "config confirm");
		
		assertTrue(player1.runCommand("ep silence"));
		assertNotTooManyArgs(player1, "config silence");
		
		assertTrue(player1.runCommand("ep free"));
		assertNotTooManyArgs(player1, "config free");
		
		assertTrue(player1.runCommand("ep locate"));
		assertNotTooManyArgs(player1, "config locate");
		
		assertTrue(player1.runCommand("suicide"));
		assertNotTooManyArgs(player1, "config suicide");
	}
	
	private void assertTooManyArgs(TestPlayer player, String cmd) {
		assertEquals(TextUtil.parse(Lang.commandToManyArgs, cmd), player.pollMessage());
		player.clearMessages();
	}
	
	private void assertNotTooManyArgs(TestPlayer player, String cmd) {
		assertNotSame(TextUtil.parse(Lang.commandToManyArgs, cmd), player.pollMessage());
		player.clearMessages();
	}
}
