package com.devotedmc.ExilePearl.test;

import org.bukkit.entity.Player;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestBukkitRunner;
import com.devotedmc.testbukkit.TestMethodHandler;
import com.devotedmc.testbukkit.TestPlayer;
import com.devotedmc.testbukkit.annotation.ProxyTarget;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.TestOptions;

@SuppressWarnings("unused")
@RunWith(TestBukkitRunner.class)
@TestOptions(useLogger=true)
public class TestTest<T> implements TestMethodHandler {
	
	private TestPlayer player;
	
	@Before
	public void setUp() {
		TestBukkit.getServer().addProxyHandler(Player.class, this);

		player = TestBukkit.createPlayer("Player1");
		player.connect();		
	}
	
	@After
	public void tearDown() {
		player.disconnect();
	}

	@Test
	public void test() {
		String name = player.getName();
		player.getAllowFlight();
	}
	
	@ProxyStub(Player.class)
	public boolean getAllowFlight() {
		return true;
	}
	
}
