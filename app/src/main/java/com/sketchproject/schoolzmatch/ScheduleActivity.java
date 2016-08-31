package com.sketchproject.schoolzmatch;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleActivity extends AppCompatActivity {
    @BindView(R.id.buttonDetail)
    Button buttonDetail;
    @BindView(R.id.buttonSave)
    Button buttonSave;

    @BindView(R.id.arrivalExpectation)
    TextView arrivalExpectation;
    @BindView(R.id.schoolDistance)
    TextView schoolDistance;

    @BindView(R.id.homework)
    TextView homework;
    @BindView(R.id.wakeup)
    TextView wakeup;
    @BindView(R.id.pray)
    TextView pray;
    @BindView(R.id.workout)
    TextView workout;
    @BindView(R.id.shower)
    TextView shower;
    @BindView(R.id.breakfast)
    TextView breakfast;
    @BindView(R.id.school)
    TextView school;

    @BindView(R.id.controlArrival)
    View controlArrival;
    @BindView(R.id.controlDistance)
    View controlDistance;
    @BindView(R.id.controlHomework)
    View controlHomework;
    @BindView(R.id.controlWakeup)
    View controlWakeup;
    @BindView(R.id.controlPray)
    View controlPray;
    @BindView(R.id.controlWorkout)
    View controlWorkout;
    @BindView(R.id.controlShower)
    View controlShower;
    @BindView(R.id.controlBreakfast)
    View controlBreakfast;
    @BindView(R.id.controlSchool)
    View controlSchool;

    @OnClick(R.id.buttonDetail)
    void detail() {
        //Intent intentDetail = new Intent(ScheduleActivity.this, DetailSchedule.class);
    }

    @OnClick(R.id.buttonSave)
    void save() {
        Snackbar snackbar = Snackbar.make(buttonSave, "Schedule data has been updated", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(ContextCompat.getColor(buttonSave.getContext(), R.color.colorLight));
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorSuccess);
        snackbar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
    }
}
