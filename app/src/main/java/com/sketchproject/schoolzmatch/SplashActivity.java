package com.sketchproject.schoolzmatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
}
