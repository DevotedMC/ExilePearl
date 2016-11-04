package com.devotedmc.testbukkit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public interface TestWorld extends World {

	@Override
	TestBlock getBlockAt(int x, int y, int z);
	
	@Override
	TestBlock getBlockAt(Location l);

	@Override
	TestChunk getChunkAt(Location l);

	@Override
	TestChunk getChunkAt(Block b);

	@Override
	TestChunk getChunkAt(int x, int z); 
	
	@Override
	TestLocation getSpawnLocation();

	@Override
	TestChunk[] getLoadedChunks();
}
