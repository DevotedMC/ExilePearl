package com.devotedmc.testbukkit.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.devotedmc.testbukkit.ProxyFactory;
import com.devotedmc.testbukkit.ProxyMock;
import com.devotedmc.testbukkit.TestPlayer;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

import javassist.Modifier;

public class CoreProxyFactory implements ProxyFactory {
	
	private static final Map<Class<?>, Class<? extends ProxyMock<?>>> proxyMocks = new HashMap<Class<?>, Class<? extends ProxyMock<?>>>();
	
	static {
		registerProxyInternal(CoreTestPlayer.class);
		registerProxyInternal(CoreTestBlock.class);
		registerProxyInternal(CoreTestWorld.class);
		registerProxyInternal(CoreTestChunk.class);
		registerProxyInternal(CoreTestBlockState.class);
		registerProxyInternal(CoreTestInventory.class);
	}
	
	public CoreProxyFactory() {
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
	public void registerProxy(Class<? extends ProxyMock<?>> proxyType) {
		registerProxyInternal(proxyType);
	}
	
	private static void registerProxyInternal(Class<? extends ProxyMock<?>> proxyType) {
		ProxyTarget targetAnnotation = proxyType.getAnnotation(ProxyTarget.class);
		if (targetAnnotation == null) {
			throw new Error(String.format("Proxy class %s must be annotated with %s", proxyType.getName(), ProxyTarget.class.getName()));
		}
		Class<?> targetType = targetAnnotation.value();
		
		// Make sure any stubbed methods actually exist in the target class
		for (Method m : proxyType.getDeclaredMethods()) {
			ProxyStub stub = m.getAnnotation(ProxyStub.class);
			if (stub == null) {
				continue;
			}
			
			if (stub.value() != void.class) {
				checkMethod(m, stub.value());
				continue;
			}
			
			checkMethod(m, targetType);
		}
		
		proxyMocks.put(targetType, proxyType);
	}
	
	
	private static void checkMethod(Method method, Class<?> clazz) throws NoSuchMethodError {
		if (!checkMethodExists(method, clazz)) {
			errorWrongParams(method, clazz);
		}
		checkMethodPublic(method, clazz);
	}
	
	private static boolean checkMethodExists(Method method, Class<?> clazz) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(method.getName()) && Arrays.equals(method.getParameterTypes(), m.getParameterTypes()) && method.getReturnType().equals(m.getReturnType())) {
				return true;
			}
		}
		return false;
	}
	
	private static void checkMethodPublic(Method method, Class<?> clazz) throws NoSuchMethodError {
		if (!Modifier.isPublic(method.getModifiers())) {
			throw new NoSuchMethodError(String.format("The proxy stub %s.%s() isn't public", clazz.getSimpleName(), method.getName()));
		}
	}
	
	private static void errorWrongParams(Method method, Class<?> clazz) throws NoSuchMethodError {
		String params = "";
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length == 0) {
			params = "void";
		} else {
			for (Class<?> p : paramTypes) {
				params += p.getSimpleName();
				params += ", ";
			}
			params = params.substring(0, params.length() - 2);
		}
		
		throw new NoSuchMethodError(String.format("The proxy stub %s.%s(%s) doesn't exist in %s", clazz.getSimpleName(), method.getName(), params, clazz.getName()));
	}
}
