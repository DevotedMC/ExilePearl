package com.devotedmc.ExilePearl.test;

import org.bukkit.entity.Player;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestBukkitRunner;
import com.devotedmc.testbukkit.TestMethodHandler;
import com.devotedmc.testbukkit.TestOptions;
import com.devotedmc.testbukkit.TestPlayer;

@SuppressWarnings("unused")
@RunWith(TestBukkitRunner.class)
//@TestOptions(useLogger = true, isIntegration = true)
public class TestTest<T> implements TestMethodHandler {
	
	private TestPlayer player;
	
	@Before
	public void setUp() {
		player = TestBukkit.createPlayer("Player1");
		TestBukkit.getServer().addMethodHandler(Player.class, this);
	}

	@Test
	public void test() {
		String name = player.getName();
		player.getAllowFlight();
	}
	
	
	public boolean getAllowFlight() {
		return true;
	}
	
}
