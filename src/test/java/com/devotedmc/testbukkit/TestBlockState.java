package com.devotedmc.testbukkit;

import org.bukkit.block.BlockState;

public interface TestBlockState extends BlockState {
	
	@Override
	TestBlock getBlock();
	
	@Override
	TestWorld getWorld();
	
	@Override
	TestLocation getLocation();
}
