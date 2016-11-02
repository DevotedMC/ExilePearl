package com.devotedmc.testbukkit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import vg.civcraft.mc.civmodcore.util.Guard;

public class TestServerProxy implements InvocationHandler {
	
	final TestServer server;
	
	public TestServerProxy(final TestServer server) {
		Guard.ArgumentNotNull(server, "server");
		this.server = server;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(server, args);
	}
}
