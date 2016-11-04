package com.devotedmc.testbukkit;

import java.lang.reflect.InvocationHandler;

public interface ServerProxy extends InvocationHandler {
	
	TestServer getServer();

}
