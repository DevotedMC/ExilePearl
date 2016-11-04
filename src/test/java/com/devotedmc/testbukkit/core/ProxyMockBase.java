package com.devotedmc.testbukkit.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.devotedmc.testbukkit.ProxyMock;
import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestServer;

class ProxyMockBase<T> implements ProxyMock<T>, InvocationHandler {
	
	private final TestServer server;
	private final Class<T> proxyType;
	private final Class<?>[] interfaces;
	private T proxyTarget;
	
	public ProxyMockBase(final Class<T> proxyType, final Class<?>... interfaces) {
		this.proxyType = proxyType;
		this.interfaces = interfaces;
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
	
	protected TestServer getServer() {
		return server;
	}
	
	protected <P> P createInstance(Class<P> clazz, Object... args) {
		return server.getProxyFactory().createInstance(clazz, args);
	}
	
	/**
	 * Recreates the proxy target with a set of interfaces
	 * @param interfaces The interfaces
	 */
	protected void bindProxy(final Class<?>... interfaces) {
		proxyTarget = createProxyInstance();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return TestBukkit.getServer().invokeProxy(proxyType, proxy, method, args);
	}
	
	private final T createProxyInstance() {
		try {
			Class<?>[] proxyArg = new  Class<?>[interfaces.length + 1];
			proxyArg[0] = proxyType;
			for(int i = 1; i <= interfaces.length; i++) {
				proxyArg[i] = interfaces[i];
			}
			
			return proxyType.cast(Proxy.getProxyClass(getClass().getClassLoader(), proxyArg).getConstructor(InvocationHandler.class).newInstance(this));
		} catch (Exception ex) {
			throw new RuntimeException("Unable to create proxy for type " + proxyType.getSimpleName(), ex);
		}
	}
}
