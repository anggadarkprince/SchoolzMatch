package com.sketchproject.schoolzmatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.sketchproject.schoolzmatch.database.Profile;
import com.sketchproject.schoolzmatch.database.ProfileRepository;
import com.sketchproject.schoolzmatch.database.Schedule;
import com.sketchproject.schoolzmatch.database.ScheduleRepository;
import com.sketchproject.schoolzmatch.utils.AlarmClock;
import com.sketchproject.schoolzmatch.utils.Constant;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleActivity extends AppCompatActivity implements
        NumberPickerDialogFragment.NumberPickerDialogHandlerV2,
        HmsPickerDialogFragment.HmsPickerDialogHandlerV2 {

    private static final int BUTTON_PICK_EXPECTATION = 0;
    private static final int BUTTON_PICK_DISTANCE = 1;

    private static final int BUTTON_PICK_HOMEWORK = 0;
    private static final int BUTTON_PICK_WAKEUP = 1;
    private static final int BUTTON_PICK_PRAY = 2;
    private static final int BUTTON_PICK_WORKOUT = 3;
    private static final int BUTTON_PICK_SHOWER = 4;
    private static final int BUTTON_PICK_BREAKFAST = 5;
    private static final int BUTTON_PICK_SCHOOL = 6;

    private NumberPickerBuilder numberPickerBuilder;
    private HmsPickerBuilder hmsPickerBuilder;

    private ScheduleRepository scheduleRepository;
    private ProfileRepository profileRepository;

    private Schedule homeworkDuration;
    private Schedule wakeupDuration;
    private Schedule prayDuration;
    private Schedule workoutDuration;
    private Schedule showerDuration;
    private Schedule breakfastDuration;
    private Schedule schoolDuration;
    private Schedule statusAlarm;

    private int arriveBefore;
    private float distance;

    @BindView(R.id.buttonSave)
    Button buttonSave;

    @BindView(R.id.arrivalExpectation)
    TextView arrivalExpectation;
    @BindView(R.id.schoolDistance)
    TextView schoolDistance;
    @BindView(R.id.alarmStatus)
    SwitchCompat alarmStatus;

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

    @OnClick(R.id.buttonDetail)
    void detail() {
        Intent intentDetail = new Intent(ScheduleActivity.this, BellActivity.class);
        startActivity(intentDetail);
    }

    @OnClick(R.id.buttonSave)
    void save() {
        scheduleRepository.store(homeworkDuration);
        scheduleRepository.store(wakeupDuration);
        scheduleRepository.store(prayDuration);
        scheduleRepository.store(workoutDuration);
        scheduleRepository.store(showerDuration);
        scheduleRepository.store(breakfastDuration);
        scheduleRepository.store(schoolDuration);

        profileRepository.store(new Profile(Constant.ARRIVE_BEFORE, arriveBefore));
        profileRepository.store(new Profile(Constant.SCHOOL_DISTANCE, distance));
        profileRepository.store(new Profile(Constant.ALARM_STATUS, alarmStatus.isChecked() ? "on" : "off"));

        if (alarmStatus.isChecked()) {
            AlarmClock.updateAlarmClock(getApplicationContext());
        } else {
            AlarmClock.cancelAlarms();
        }

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

    @OnClick(R.id.controlArrival)
    void changeArrivalBefore() {
        numberPickerBuilder
                .setMaxNumber(new BigDecimal(60))
                .setMinNumber(new BigDecimal(0))
                .setDecimalVisibility(View.INVISIBLE)
                .setReference(BUTTON_PICK_EXPECTATION)
                .setLabelText("MINUTES")
                .show();
    }

    @OnClick(R.id.controlDistance)
    void changeSchoolDistance() {
        numberPickerBuilder
                .setMaxNumber(new BigDecimal(100))
                .setMinNumber(new BigDecimal(0))
                .setDecimalVisibility(View.VISIBLE)
                .setReference(BUTTON_PICK_DISTANCE)
                .setLabelText("KM")
                .show();
    }

    @OnClick(R.id.controlHomework)
    void changeHomeworkDuration() {
        hmsPickerBuilder.setReference(BUTTON_PICK_HOMEWORK);
        hmsPickerBuilder.show();
    }

    @OnClick(R.id.controlWakeup)
    void changeWakeupDuration() {
        hmsPickerBuilder.setReference(BUTTON_PICK_WAKEUP);
        hmsPickerBuilder.show();
    }

    @OnClick(R.id.controlPray)
    void changePrayDuration() {
        hmsPickerBuilder.setReference(BUTTON_PICK_PRAY);
        hmsPickerBuilder.show();
    }

    @OnClick(R.id.controlWorkout)
    void changeWorkoutDuration() {
        hmsPickerBuilder.setReference(BUTTON_PICK_WORKOUT);
        hmsPickerBuilder.show();
    }

    @OnClick(R.id.controlShower)
    void changeShowerDuration() {
        hmsPickerBuilder.setReference(BUTTON_PICK_SHOWER);
        hmsPickerBuilder.show();
    }

    @OnClick(R.id.controlBreakfast)
    void changeBreakfastDuration() {
        hmsPickerBuilder.setReference(BUTTON_PICK_BREAKFAST);
        hmsPickerBuilder.show();
    }

    @OnClick(R.id.controlSchool)
    void changeSchoolDuration() {
        hmsPickerBuilder.setReference(BUTTON_PICK_SCHOOL);
        hmsPickerBuilder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);

        numberPickerBuilder = new NumberPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.CustomBetterPickerTheme)
                .setPlusMinusVisibility(View.INVISIBLE);

        hmsPickerBuilder = new HmsPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.CustomBetterPickerTheme);

        scheduleRepository = new ScheduleRepository(getApplicationContext());
        profileRepository = new ProfileRepository(getApplicationContext());

        arriveBefore = Integer.parseInt(String.valueOf(profileRepository.retrieveValueOf(Constant.ARRIVE_BEFORE)));
        onDialogNumberSet(BUTTON_PICK_EXPECTATION, new BigInteger(String.valueOf(arriveBefore)), 0, false, null);
        distance = Float.parseFloat(String.valueOf(profileRepository.retrieveValueOf(Constant.SCHOOL_DISTANCE)));
        onDialogNumberSet(BUTTON_PICK_DISTANCE, new BigInteger("0"), 0, false, new BigDecimal(distance));
        if (profileRepository.retrieveValueOf(Constant.ALARM_STATUS).equals("on")) {
            alarmStatus.setChecked(true);
        } else {
            alarmStatus.setChecked(false);
        }

        homeworkDuration = scheduleRepository.findData(Constant.ACT_HOMEWORK);
        wakeupDuration = scheduleRepository.findData(Constant.ACT_WAKEUP);
        prayDuration = scheduleRepository.findData(Constant.ACT_PRAY);
        workoutDuration = scheduleRepository.findData(Constant.ACT_WORKOUT);
        showerDuration = scheduleRepository.findData(Constant.ACT_SHOWER);
        breakfastDuration = scheduleRepository.findData(Constant.ACT_BREAKFAST);
        schoolDuration = scheduleRepository.findData(Constant.ACT_SCHOOL);

        homework.setText(AlarmClock.formatHHmm(homeworkDuration.getTime()));
        wakeup.setText(AlarmClock.formatHHmm(wakeupDuration.getTime()));
        pray.setText(AlarmClock.formatHHmm(prayDuration.getTime()));
        workout.setText(AlarmClock.formatHHmm(workoutDuration.getTime()));
        shower.setText(AlarmClock.formatHHmm(showerDuration.getTime()));
        breakfast.setText(AlarmClock.formatHHmm(breakfastDuration.getTime()));
        school.setText(AlarmClock.formatHHmm(schoolDuration.getTime()));
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        if (reference == BUTTON_PICK_EXPECTATION) {
            String es = number.compareTo(new BigInteger("1")) == 1 ? "s" : "";
            arrivalExpectation.setText(getString(R.string.arrival_expectation_value, String.valueOf(number), es));
            arriveBefore = Integer.parseInt(String.valueOf(number));
        } else if (reference == BUTTON_PICK_DISTANCE) {
            schoolDistance.setText(getString(R.string.school_distance_value, fullNumber));
            distance = Float.parseFloat(String.valueOf(fullNumber));
        }
    }

    @Override
    public void onDialogHmsSet(int reference, boolean isNegative, int hours, int minutes, int seconds) {
        switch (reference) {
            case BUTTON_PICK_HOMEWORK:
                homeworkDuration.setTime(AlarmClock.formatTime(ScheduleActivity.this, hours, minutes));
                homework.setText(AlarmClock.formatDuration(hours, minutes));
                break;
            case BUTTON_PICK_WAKEUP:
                wakeupDuration.setTime(AlarmClock.formatTime(ScheduleActivity.this, hours, minutes));
                wakeup.setText(AlarmClock.formatDuration(hours, minutes));
                break;
            case BUTTON_PICK_PRAY:
                prayDuration.setTime(AlarmClock.formatTime(ScheduleActivity.this, hours, minutes));
                pray.setText(AlarmClock.formatDuration(hours, minutes));
                break;
            case BUTTON_PICK_WORKOUT:
                workoutDuration.setTime(AlarmClock.formatTime(ScheduleActivity.this, hours, minutes));
                workout.setText(AlarmClock.formatDuration(hours, minutes));
                break;
            case BUTTON_PICK_SHOWER:
                showerDuration.setTime(AlarmClock.formatTime(ScheduleActivity.this, hours, minutes));
                shower.setText(AlarmClock.formatDuration(hours, minutes));
                break;
            case BUTTON_PICK_BREAKFAST:
                breakfastDuration.setTime(AlarmClock.formatTime(ScheduleActivity.this, hours, minutes));
                breakfast.setText(AlarmClock.formatDuration(hours, minutes));
                break;
            case BUTTON_PICK_SCHOOL:
                schoolDuration.setTime(AlarmClock.formatTime(ScheduleActivity.this, hours, minutes));
                school.setText(AlarmClock.formatDuration(hours, minutes));
                break;
        }
    }
}
