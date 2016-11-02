package com.devotedmc.testbukkit.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

import com.devotedmc.testbukkit.TestBukkit;
import com.devotedmc.testbukkit.TestMethodHandler;

public abstract class TestProxy implements InvocationHandler {
	
	private final Class<?> proxyType;
	
	public TestProxy(final Class<?> proxyType) {
		this.proxyType = proxyType;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			return getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(this, args);
		} catch(NoSuchMethodException ex) {
			
			// Try to find a method in the handler list
			Set<TestMethodHandler> handlers = TestBukkit.getServer().getMethodHandlers(proxyType);
			
			if (handlers != null) {
				for(TestMethodHandler handler : handlers) {
					try {
						return handler.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(handler, args);
					} catch(NoSuchMethodException ex2) {
						continue;
					}
				}
			}
			
			if (method.getReturnType().isPrimitive()) {
				return DefaultValues.defaultValueFor(method.getReturnType());
			}
			return null;
		}
	}
	
	abstract Object invokeImpl(Object proxy, Method method, Object[] args) throws Throwable;
}
