package com.sketchproject.schoolzmatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AlertDialog dialogExitConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AppTheme_AlertDialogStyle));
        builder.setTitle("Confirm Exit App");
        builder.setMessage("Are you sure want to exit app? the alarm still active in background");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogExitConfirm = builder.create();

        Button buttonProfile = (Button) findViewById(R.id.buttonProfile);
        assert buttonProfile != null;
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        Button buttonSchedule = (Button) findViewById(R.id.buttonSchedule);
        assert buttonSchedule != null;
        buttonSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scheduleIntent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(scheduleIntent);
            }
        });

        Button buttonAbout = (Button) findViewById(R.id.buttonAbout);
        assert buttonAbout != null;
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
            }
        });

        Button buttonExit = (Button) findViewById(R.id.buttonExit);
        assert buttonExit != null;
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogExitConfirm.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (dialogExitConfirm != null) {
            if (dialogExitConfirm.isShowing()) {
                dialogExitConfirm.cancel();
            } else {
                dialogExitConfirm.show();
            }
        }
    }
}
