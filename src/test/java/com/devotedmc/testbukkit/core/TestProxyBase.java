package com.devotedmc.testbukkit.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestMethodHandler;

public abstract class TestProxyBase implements InvocationHandler, TestMethodHandler {
	
	private final Class<?> proxyType;
	
	public TestProxyBase(final Class<?> proxyType) {
		this.proxyType = proxyType;

		TestBukkit.getServer().addProxyHandler(proxyType, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return TestBukkit.getServer().invokeProxy(proxyType, proxy, method, args);
	}
	
	
	protected <T> T createProxyInstance(Class<T> proxyClass) throws Exception {
		return proxyClass.cast(Proxy.getProxyClass(getClass().getClassLoader(), proxyClass).getConstructor(InvocationHandler.class).newInstance(this));
	}
}
