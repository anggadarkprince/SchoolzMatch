package com.sketchproject.schoolzmatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sketchproject.schoolzmatch.database.ProfileRepository;
import com.sketchproject.schoolzmatch.database.Schedule;
import com.sketchproject.schoolzmatch.database.ScheduleRepository;
import com.sketchproject.schoolzmatch.modules.AlarmReceiver;
import com.sketchproject.schoolzmatch.utils.AlarmClock;
import com.sketchproject.schoolzmatch.utils.Constant;

import java.util.Map;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initDatabaseData();

        final int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent applicationIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(applicationIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    /**
     * Add default school time data.
     */
    private void initDatabaseData() {
        ProfileRepository profileRepository = new ProfileRepository(getApplicationContext());
        Map<String, Object> profiles = profileRepository.retrieve();

        // set sunday school time
        if (!profiles.containsKey(Constant.DAY_MONDAY)) {
            profileRepository.store(Constant.DAY_MONDAY, "07:00");
        }

        // set monday school time
        if (!profiles.containsKey(Constant.DAY_TUESDAY)) {
            profileRepository.store(Constant.DAY_TUESDAY, "07:00");
        }

        // set wednesday school time
        if (!profiles.containsKey(Constant.DAY_WEDNESDAY)) {
            profileRepository.store(Constant.DAY_WEDNESDAY, "07:00");
        }

        // set thursday school time
        if (!profiles.containsKey(Constant.DAY_THURSDAY)) {
            profileRepository.store(Constant.DAY_THURSDAY, "07:00");
        }

        // set friday school time
        if (!profiles.containsKey(Constant.DAY_FRIDAY)) {
            profileRepository.store(Constant.DAY_FRIDAY, "06:45");
        }

        // set saturday school time
        if (!profiles.containsKey(Constant.DAY_SATURDAY)) {
            profileRepository.store(Constant.DAY_SATURDAY, "07:00");
        }

        // set default arrive before
        if (!profiles.containsKey(Constant.ARRIVE_BEFORE)) {
            profileRepository.store(Constant.ARRIVE_BEFORE, "5");
        }

        // set default alarm status
        if (!profiles.containsKey(Constant.ALARM_STATUS)) {
            profileRepository.store(Constant.ALARM_STATUS, "on");
        }

        // set default arrive before
        if (!profiles.containsKey(Constant.SCHOOL_DISTANCE)) {
            profileRepository.store(Constant.SCHOOL_DISTANCE, "10");
        }

        ScheduleRepository scheduleRepository = new ScheduleRepository(getApplicationContext());

        // set default homework duration
        if (scheduleRepository.findData(Constant.ACT_HOMEWORK) == null) {
            scheduleRepository.store(new Schedule(Constant.ACT_HOMEWORK, "Doing homework before sleep", "01:00"));
        }

        // set default sleep duration
        if (scheduleRepository.findData(Constant.ACT_WAKEUP) == null) {
            scheduleRepository.store(new Schedule(Constant.ACT_WAKEUP, "Rest of the day", "08:00"));
        }

        // set default pray duration
        if (scheduleRepository.findData(Constant.ACT_PRAY) == null) {
            scheduleRepository.store(new Schedule(Constant.ACT_PRAY, "Pray and shalat for moslem", "00:10"));
        }

        // set default workout duration
        if (scheduleRepository.findData(Constant.ACT_WORKOUT) == null) {
            scheduleRepository.store(new Schedule(Constant.ACT_WORKOUT, "Exercise and light sport", "00:15"));
        }

        // set default sleep duration
        if (scheduleRepository.findData(Constant.ACT_SHOWER) == null) {
            scheduleRepository.store(new Schedule(Constant.ACT_SHOWER, "Take a bath and cleaning body", "00:15"));
        }

        // set default breakfast duration
        if (scheduleRepository.findData(Constant.ACT_BREAKFAST) == null) {
            scheduleRepository.store(new Schedule(Constant.ACT_BREAKFAST, "Fill my energy with food", "00:15"));
        }

        // set default school duration
        if (scheduleRepository.findData(Constant.ACT_SCHOOL) == null) {
            scheduleRepository.store(new Schedule(Constant.ACT_SCHOOL, "Go to my school", "00:20"));
            AlarmClock.setupAlarmChecker(getApplicationContext());
        }

        AlarmClock.updateAlarmClock(getApplicationContext());
        if (AlarmReceiver.mp != null) {
            AlarmReceiver.mp.stop();
        }
    }
}
