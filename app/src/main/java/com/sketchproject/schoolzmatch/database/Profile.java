package com.sketchproject.schoolzmatch.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Sketch Project Studio
 * Created by Angga on 30/08/2016 12.58.
 */
public class Profile {
    public static final String TABLE = "profiles";
    public static final String KEY = "key";
    public static final String VALUE = "value";

    private String key;
    private Object value;

    public Profile() {
    }

    public Profile(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPair(String key, Object value){
        this.key = key;
        this.value = value;
    }
}
