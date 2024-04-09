package com.aps.tiktube.model;

import java.lang.reflect.InvocationTargetException;

public class EntityFactory {
    
    
    public static <T extends Entity<T>> T New(Class<T> type) {
        T obj = null;
        try {
            obj = type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }  
        return obj;
    }
}

