package com.sketchproject.schoolzmatch.database;

import java.sql.Time;

/**
 * Sketch Project Studio
 * Created by Angga on 30/08/2016 12.59.
 */
public class Schedule {
    public static final String TABLE = "schedules";
    public static final String ID = "id";
    public static final String LABEL = "label";
    public static final String DESCRIPTION = "description";
    public static final String TIME = "time";

    private int id;
    private String label;
    private String description;
    private Time time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
