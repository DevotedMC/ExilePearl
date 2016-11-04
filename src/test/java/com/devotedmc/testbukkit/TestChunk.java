package com.devotedmc.testbukkit;

import org.bukkit.Chunk;

public interface TestChunk extends Chunk {
	
	@Override
	TestWorld getWorld();

	@Override
	TestBlock getBlock(int x, int y, int z);
	
}
