package com.devotedmc.testbukkit;

import org.bukkit.Location;

public class TestLocation extends Location {

	public TestLocation(TestWorld world, double x, double y, double z) {
		super(world, x, y, z);
	}
	
	public TestLocation(final TestWorld world, final double x, final double y, final double z, final float yaw, final float pitch) {
		super(world, x, y, z, yaw, pitch);
	}
	
	public TestLocation(Location l) {
		super(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
	}

	@Override
	public TestWorld getWorld() {
		return TestWorld.class.cast(super.getWorld());
	}
	
	@Override
	public TestChunk getChunk() {
		return TestChunk.class.cast(super.getChunk());
	}
	
	@Override
	public TestBlock getBlock() {
		return TestBlock.class.cast(super.getBlock());
	}
}
