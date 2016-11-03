package com.devotedmc.testbukkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this method is a proxy method that is implementing
 * an interface method.
 * 
 * @author Gordon
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyMethod {
	/**
	 * The interface to which the method belongs
	 * @return The method interface
	 */
	Class<?> value() default void.class;
}
