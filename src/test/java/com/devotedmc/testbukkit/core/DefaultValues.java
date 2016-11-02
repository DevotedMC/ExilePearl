package com.devotedmc.testbukkit.core;

import java.util.HashMap;
import java.util.Map;

class DefaultValues {
    static final Map<Class<?>,Object> defaultValues = new HashMap<Class<?>,Object>();

    // load
    static {
        defaultValues.put(boolean.class, Boolean.FALSE);
        defaultValues.put(byte.class, new Byte((byte)0));
        defaultValues.put(short.class, new Short((short)0));
        defaultValues.put(int.class, new Integer(0));
        defaultValues.put(long.class, new Long(0L));
        defaultValues.put(char.class, new Character('\0'));
        defaultValues.put(float.class, new Float(0.0F));
        defaultValues.put(double.class, new Double(0.0));
    }

    @SuppressWarnings("unchecked")
	public static final <T> T defaultValueFor(Class<T> clazz) {
        return (T)defaultValues.get(clazz);
    }
}
