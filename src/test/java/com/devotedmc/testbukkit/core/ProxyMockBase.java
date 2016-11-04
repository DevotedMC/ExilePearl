package com.devotedmc.testbukkit.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.devotedmc.testbukkit.ProxyMock;
import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestMethodHandler;
import com.devotedmc.testbukkit.TestServer;

class ProxyMockBase<T> implements ProxyMock<T>, InvocationHandler, TestMethodHandler {
	
	private final TestServer server;
	private final Class<T> proxyType;
	private final T proxyTarget;
	
	public ProxyMockBase(final Class<T> proxyType) {
		this.proxyType = proxyType;
		proxyTarget = createProxyInstance();
		if (proxyType == TestServer.class) {
			server = TestServer.class.cast(proxyTarget);
		} else {
			server = TestBukkit.getServer();
			server.addProxyHandler(proxyType, this);
		}
	}

	/**
	 * Gets the proxy target instance
	 */
	@Override
	public final T getProxy() {
		return proxyTarget;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return TestBukkit.getServer().invokeProxy(proxyType, proxy, method, args);
	}
	
	private final T createProxyInstance() {
		try {
			return proxyType.cast(Proxy.getProxyClass(getClass().getClassLoader(), proxyType).getConstructor(InvocationHandler.class).newInstance(this));
		} catch (Exception ex) {
			throw new RuntimeException("Unable to create proxy for type " + proxyType.getSimpleName(), ex);
		}
	}
}
