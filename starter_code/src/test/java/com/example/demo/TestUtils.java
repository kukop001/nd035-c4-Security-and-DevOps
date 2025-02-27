package com.example.demo;

import java.lang.reflect.*;

public class TestUtils {
	public static void injectObjects (Object target, String fieldName, Object objectToInject) {
        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if (!f.canAccess(target)) {
                f.setAccessible(true);
                wasPrivate = true;
            }

            f.set(target, objectToInject);
            if (wasPrivate) {
                f.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
