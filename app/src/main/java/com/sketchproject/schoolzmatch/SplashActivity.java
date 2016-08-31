package com.sketchproject.schoolzmatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.sketchproject.schoolzmatch.database.ProfileRepository;
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

        // thursday
        if (!profiles.containsKey(Constant.DAY_THURSDAY)) {
            profileRepository.store(Constant.DAY_THURSDAY, "07:00");
        }

        if (!profiles.containsKey(Constant.DAY_FRIDAY)) {
            profileRepository.store(Constant.DAY_FRIDAY, "06:45");
        }

        if (!profiles.containsKey(Constant.DAY_SATURDAY)) {
            profileRepository.store(Constant.DAY_SATURDAY, "07:00");
        }
    }
}
