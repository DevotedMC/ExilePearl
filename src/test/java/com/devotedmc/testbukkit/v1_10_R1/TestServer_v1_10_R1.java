package com.devotedmc.testbukkit.v1_10_R1;

import java.lang.reflect.Method;

import com.devotedmc.testbukkit.ServerProxy;
import com.devotedmc.testbukkit.TestServer;

public class TestServer_v1_10_R1 implements ServerProxy {
	
	final ServerProxy serverProxy;
	
    public TestServer_v1_10_R1(ServerProxy serverProxy) {
    	this.serverProxy = serverProxy;
    }

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(serverProxy, args);
	}

	@Override
	public TestServer getServer() {
		return serverProxy.getServer();
	}
}
