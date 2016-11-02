package com.devotedmc.testbukkit.core;

import java.util.UUID;

import com.devotedmc.testbukkit.TestFactory;
import com.devotedmc.testbukkit.TestPlayer;

public class CoreTestFactory implements TestFactory {
	
	public CoreTestFactory() {
		
	}

	@Override
	public TestPlayer createPlayer(String name, UUID uid) {
		return CoreTestPlayer.createInstance(name, uid);
	}
	
	@Override
	public TestPlayer createPlayer(String name) {
		return CoreTestPlayer.createInstance(name, UUID.randomUUID());
	}
}
