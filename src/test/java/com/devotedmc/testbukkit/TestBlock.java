package com.devotedmc.testbukkit;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public interface TestBlock extends Block {
	
	@Override
	TestWorld getWorld();
	
	@Override
	TestChunk getChunk();

	@Override
	TestBlockState getState();
	
	@Override
	TestLocation getLocation();

    TestBlock getFace(final BlockFace face);

    TestBlock getFace(final BlockFace face, final int distance);

	@Override
    TestBlock getRelative(final int modX, final int modY, final int modZ);

	@Override
    TestBlock getRelative(BlockFace face);

	@Override
    TestBlock getRelative(BlockFace face, int distance);

	@Override
    BlockFace getFace(final Block block);
	
	DataMap getTestData();
}
