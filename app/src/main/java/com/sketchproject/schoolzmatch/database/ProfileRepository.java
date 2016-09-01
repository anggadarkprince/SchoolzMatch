package com.sketchproject.schoolzmatch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Sketch Project Studio
 * Created by Angga on 31/08/2016 15.39.
 */
public class ProfileRepository {
    private DBHelper dbHelper;
    private SQLiteDatabase mDatabase;

    public ProfileRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Retrieve all pairs data object and wrap into HashMap.
     *
     * @return key-value collection inside HashMap
     */
    public Map<String, Object> retrieve() {
        mDatabase = dbHelper.getReadableDatabase();

        String[] projection = {Profile.KEY, Profile.VALUE};

        Cursor cursor = mDatabase.query(Profile.TABLE, projection, null, null, null, null, null);

        Map<String, Object> profiles = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                profiles.put(cursor.getString(cursor.getColumnIndexOrThrow(Profile.KEY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Profile.VALUE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();

        return profiles;
    }

    /**
     * Retrieve single data by key.
     *
     * @param key of data identity
     * @return Object
     */
    public Object retrieveValueOf(String key) {
        Map<String, Object> profiles = retrieve();
        if (profiles.containsKey(key)) {
            return profiles.get(key);
        }
        return null;
    }

    /**
     * Store data by passing profile object
     *
     * @param profile object
     * @return status of storing into persistent storage
     */
    public boolean store(Profile profile) {
        return store(profile.getKey(), profile.getValue());
    }

    /**
     * Store data by individual pair value instead profile object.
     * Check if data new or the old data just need to be updated.
     *
     * @param key   of profile data
     * @param value of profile data
     * @return status of storing into persistent storage
     */
    public boolean store(String key, Object value) {
        Profile profile = new Profile(key, value);

        mDatabase = dbHelper.getReadableDatabase();

        String[] projection = {Profile.KEY};
        String selection = Profile.KEY + " = ?";
        String[] selectionArgs = {String.valueOf(profile.getKey())};
        Cursor cursor = mDatabase.query(Profile.TABLE, projection, selection, selectionArgs, null, null, null);

        mDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Profile.KEY, profile.getKey());
        values.put(Profile.VALUE, String.valueOf(profile.getValue()));

        if (cursor.moveToFirst()) {
            // update
            mDatabase.update(Profile.TABLE, values, selection, selectionArgs);
        } else {
            // insert
            mDatabase.insert(Profile.TABLE, null, values);
        }
        cursor.close();
        mDatabase.close();

        return true;
    }
}
