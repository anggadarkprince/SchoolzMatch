package com.sketchproject.schoolzmatch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sketch Project Studio
 * Created by Angga on 31/08/2016 19.33.
 */
public class ScheduleRepository {
    private DBHelper dbHelper;
    private SQLiteDatabase mDatabase;

    public ScheduleRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<Schedule> retrieve() throws ParseException {
        mDatabase = dbHelper.getReadableDatabase();

        String[] projection = {Schedule.ID, Schedule.LABEL, Schedule.DESCRIPTION, Schedule.TIME};

        Cursor cursor = mDatabase.query(Schedule.TABLE, projection, null, null, null, null, null);

        List<Schedule> scheduleList = new ArrayList<>();
        Schedule schedule;

        if (cursor.moveToFirst()) {
            do {
                schedule = new Schedule();
                schedule.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Schedule.ID)));
                schedule.setLabel(cursor.getString(cursor.getColumnIndex(Schedule.LABEL)));
                schedule.setDescription(cursor.getString(cursor.getColumnIndex(Schedule.DESCRIPTION)));
                schedule.setTime(cursor.getString(cursor.getColumnIndex(Schedule.TIME)));
                scheduleList.add(schedule);
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();

        return scheduleList;
    }

    public Schedule findData(String label) {
        mDatabase = dbHelper.getReadableDatabase();

        String[] projection = {Schedule.ID, Schedule.LABEL, Schedule.DESCRIPTION, Schedule.TIME};

        String selection = Schedule.LABEL + " = ?";
        String[] selectionArgs = {label};

        Schedule schedule = new Schedule();
        Cursor cursor = mDatabase.query(Schedule.TABLE, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            schedule.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Schedule.ID)));
            schedule.setLabel(cursor.getString(cursor.getColumnIndex(Schedule.LABEL)));
            schedule.setDescription(cursor.getString(cursor.getColumnIndex(Schedule.DESCRIPTION)));
            schedule.setTime(cursor.getString(cursor.getColumnIndex(Schedule.TIME)));
            cursor.close();
            mDatabase.close();
        } else {
            return null;
        }
        return schedule;
    }

    public boolean store(Schedule schedule) {
        return store(schedule.getLabel(), schedule.getDescription(), schedule.getTime());
    }

    public boolean store(String label, String description, String time) {
        Schedule schedule = new Schedule(label, description, time);

        mDatabase = dbHelper.getReadableDatabase();

        String[] projection = {Schedule.ID};
        String selection = Schedule.ID + " = ?";
        String[] selectionArgs = {String.valueOf(schedule.getId())};
        Cursor cursor = mDatabase.query(Schedule.TABLE, projection, selection, selectionArgs, null, null, null);

        mDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schedule.LABEL, schedule.getLabel());
        values.put(Schedule.DESCRIPTION, schedule.getDescription());
        values.put(Schedule.TIME, schedule.getTime());

        if (cursor.moveToFirst()) {
            // update
            mDatabase.update(Schedule.TABLE, values, selection, selectionArgs);
        } else {
            // insert
            mDatabase.insert(Schedule.TABLE, null, values);
        }
        cursor.close();
        mDatabase.close();

        return true;
    }
}
