package com.devotedmc.testbukkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TestOptions {
	/**
	 * Whether the test server instance should log to the console.
	 * <p>
	 * This should usually be set to false for normal unit-tests.
	 * @return true if the logger will be used
	 */
	boolean useLogger() default false;
	
	/**
	 * Whether the test being run is an integration test.
	 * <p>
	 * Setting this to true allows full plugins to be loaded and the test
	 * server will run to it's full potential. 
	 * <p>
	 * This should usually be false for normal unit-tests.
	 * @return true if the test is an integration test.
	 */
	boolean isIntegration() default false;
	
	/**
	 * 
	 * @return Gets the optional server interface
	 */
	Class<? extends TestServer> server() default TestServer.class;
}
