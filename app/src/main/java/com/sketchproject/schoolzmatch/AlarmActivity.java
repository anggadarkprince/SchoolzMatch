package com.sketchproject.schoolzmatch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AlarmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Button turnOffAlarm = (Button) findViewById(R.id.buttonTurnOff);
    }

    @Override
    public void onBackPressed() {
    }
}
