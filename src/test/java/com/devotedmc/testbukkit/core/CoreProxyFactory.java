package com.devotedmc.testbukkit.core;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.devotedmc.testbukkit.ProxyFactory;
import com.devotedmc.testbukkit.ProxyMock;
import com.devotedmc.testbukkit.TestPlayer;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

public class CoreProxyFactory implements ProxyFactory {
	
	private final Map<Class<?>, Class<? extends ProxyMock<?>>> proxyMocks = new HashMap<Class<?>, Class<? extends ProxyMock<?>>>();
	
	public CoreProxyFactory() {
		proxyMocks.put(TestPlayer.class, CoreTestPlayer.class);
	}

	@Override
	public TestPlayer createPlayer(String name, UUID uid) {
		return createInstance(TestPlayer.class, name, uid);
	}
	
	@Override
	public TestPlayer createPlayer(String name) {
		return createPlayer(name, UUID.randomUUID());
	}

	@Override
	public <T> T createInstance(Class<T> clazz, Object... initArgs) {
		try {
			Class<? extends ProxyMock<?>> proxyType = proxyMocks.get(clazz);
			if (proxyType != null) {
				Constructor<?> c = proxyType.getDeclaredConstructors()[0];
				return clazz.cast(ProxyMock.class.cast(c.newInstance(initArgs)).getProxy());
			}
		} catch (Exception ex) {
			throw new RuntimeException("Unable to create proxy for " + clazz.getName());
		}
		return null;
	}

	@Override
	public void registerProxy(Class<? extends ProxyMock<?>> proxy) {
		ProxyTarget an = proxy.getAnnotation(ProxyTarget.class);
		if (an == null) {
			throw new RuntimeException(String.format("Proxy class %s must be annotated with %s", proxy.getName(), ProxyTarget.class.getName()));
		}
		proxyMocks.put(an.value(), proxy);
	}
}
