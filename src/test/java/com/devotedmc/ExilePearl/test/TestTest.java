package com.devotedmc.ExilePearl.test;

import static org.junit.Assert.*;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.devotedmc.testbukkit.TestBlock;
import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestBukkitRunner;
import com.devotedmc.testbukkit.TestPlayer;
import com.devotedmc.testbukkit.TestWorld;
import com.devotedmc.testbukkit.annotation.ProxyTarget;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.TestOptions;

@SuppressWarnings("unused")
@RunWith(TestBukkitRunner.class)
@TestOptions(useLogger=true)
public class TestTest<T> {
	
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
		
		TestWorld world = TestBukkit.getServer().getWorld("world");
		
		TestBlock b1 = world.getBlockAt(0, 0, 0);
		b1.setType(Material.CHEST);
		assertTrue(b1.getState() instanceof InventoryHolder);
		InventoryHolder holder1 = (InventoryHolder)b1.getState();
		assertTrue(holder1 instanceof Chest);
		Inventory inv1 = holder1.getInventory();
		
		TestBlock b2 = world.getBlockAt(0, 0, 1);
		b2.setType(Material.CHEST);
		assertTrue(b2.getState() instanceof InventoryHolder);
		InventoryHolder holder2 = (InventoryHolder)b2.getState();
		Inventory inv2 = holder1.getInventory();
		assertTrue(inv2 instanceof DoubleChestInventory);
		DoubleChestInventory dInv2 = (DoubleChestInventory)inv2;
		assertTrue(inv2.getHolder() instanceof DoubleChest);
		
		Inventory left = dInv2.getLeftSide();
		Inventory right = dInv2.getRightSide();
		assertEquals(left, inv2);
		assertEquals(right, inv1);
	}
	
	@ProxyStub(Player.class)
	public boolean getAllowFlight() {
		return true;
	}
	
	@ProxyStub(Player.class)
	public String getName() {
		return "test";
	}
	
}
