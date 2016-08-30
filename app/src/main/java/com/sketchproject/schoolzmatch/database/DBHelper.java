package com.sketchproject.schoolzmatch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Sketch Project Studio
 * Created by Angga on 30/08/2016 12.50.
 */
public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SchoolzMatch.db";

    private static final String SQL_CREATE_PROFILES = "CREATE TABLE " + Profile.TABLE + " (" +
            Profile.KEY + " CHAR(50) PRIMARY KEY NOT NULL," +
            Profile.VALUE + " TEXT NOT NULL)";
    private static final String SQL_CREATE_SCHEDULES = "CREATE TABLE " + Schedule.TABLE + " (" +
            Schedule.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Schedule.LABEL + " TEXT NOT NULL," +
            Schedule.DESCRIPTION + " TEXT," +
            Schedule.TIME + " TEXT NOT NULL)";

    private static final String SQL_DROP_PROFILES = "DROP TABLE IF EXISTS " + Profile.TABLE;
    private static final String SQL_DROP_SCHEDULES = "DROP TABLE IF EXISTS " + Schedule.TABLE;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create database for the first time.
     *
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_PROFILES);
        sqLiteDatabase.execSQL(SQL_CREATE_SCHEDULES);
    }

    /**
     * Upgrade database when android detect database version is decremented.
     *
     * @param db         SQLite database
     * @param oldVersion number old version
     * @param newVersion number new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for profile data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DROP_PROFILES);
        sqLiteDatabase.execSQL(SQL_DROP_SCHEDULES);
        onCreate(sqLiteDatabase);
    }
}
