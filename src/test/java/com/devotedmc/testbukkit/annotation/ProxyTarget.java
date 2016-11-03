package com.devotedmc.testbukkit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * A proxy class that implements calls to an interface.
 * 
 * @author Gordon
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyTarget {
	/**
	 * @return The proxy interface
	 */
	Class<?> value();
}
