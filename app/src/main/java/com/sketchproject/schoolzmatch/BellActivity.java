package com.sketchproject.schoolzmatch;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.sketchproject.schoolzmatch.database.ProfileRepository;
import com.sketchproject.schoolzmatch.utils.AlarmClock;
import com.sketchproject.schoolzmatch.utils.Constant;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BellActivity extends AppCompatActivity implements TimePickerDialogFragment.TimePickerDialogHandler {
    ProfileRepository profileRepository;

    TimePickerBuilder timePickerBuilder;

    Map<String, Object> schoolTimes;

    @BindView(R.id.buttonSave)
    Button buttonSave;

    @BindView(R.id.monday)
    TextView monday;
    @BindView(R.id.tuesday)
    TextView tuesday;
    @BindView(R.id.wednesday)
    TextView wednesday;
    @BindView(R.id.thursday)
    TextView thursday;
    @BindView(R.id.friday)
    TextView friday;
    @BindView(R.id.saturday)
    TextView saturday;

    @BindView(R.id.controlMonday)
    View controlMonday;
    @BindView(R.id.controlTuesday)
    View controlTuesday;
    @BindView(R.id.controlWednesday)
    View controlWednesday;
    @BindView(R.id.controlThursday)
    View controlThursday;
    @BindView(R.id.controlFriday)
    View controlFriday;
    @BindView(R.id.controlSaturday)
    View controlSaturday;

    @OnClick(R.id.controlMonday)
    void changeMondaySchool() {
        timePickerBuilder.setReference(Constant.NUM_DAY_MONDAY);
        timePickerBuilder.show();
    }

    @OnClick(R.id.controlTuesday)
    void changeTuesdaySchool() {
        timePickerBuilder.setReference(Constant.NUM_DAY_TUESDAY);
        timePickerBuilder.show();
    }

    @OnClick(R.id.controlWednesday)
    void changeWednesdaySchool() {
        timePickerBuilder.setReference(Constant.NUM_DAY_WEDNESDAY);
        timePickerBuilder.show();
    }

    @OnClick(R.id.controlThursday)
    void changeThursdaySchool() {
        timePickerBuilder.setReference(Constant.NUM_DAY_THURSDAY);
        timePickerBuilder.show();
    }

    @OnClick(R.id.controlFriday)
    void changeFridaySchool() {
        timePickerBuilder.setReference(Constant.NUM_DAY_FRIDAY);
        timePickerBuilder.show();
    }

    @OnClick(R.id.controlSaturday)
    void changeSaturdaySchool() {
        timePickerBuilder.setReference(Constant.NUM_DAY_SATURDAY);
        timePickerBuilder.show();
    }

    @OnClick(R.id.buttonSave)
    void save() {
        profileRepository.store(Constant.DAY_MONDAY, schoolTimes.get(Constant.DAY_MONDAY));
        profileRepository.store(Constant.DAY_TUESDAY, schoolTimes.get(Constant.DAY_TUESDAY));
        profileRepository.store(Constant.DAY_WEDNESDAY, schoolTimes.get(Constant.DAY_WEDNESDAY));
        profileRepository.store(Constant.DAY_THURSDAY, schoolTimes.get(Constant.DAY_THURSDAY));
        profileRepository.store(Constant.DAY_FRIDAY, schoolTimes.get(Constant.DAY_FRIDAY));
        profileRepository.store(Constant.DAY_SATURDAY, schoolTimes.get(Constant.DAY_SATURDAY));

        if (profileRepository.retrieveValueOf(Constant.ALARM_STATUS).equals("on")) {
            AlarmClock.updateAlarmClock(getApplicationContext());
        } else {
            AlarmClock.cancelAlarms();
        }

        Toast toast = Toast.makeText(buttonSave.getContext(), "School time has been updated", Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.color.colorSuccess);
        view.setPadding(20, 15, 20, 15);
        TextView tv = (TextView) view.findViewById(android.R.id.message);
        tv.setShadowLayer(0, 0, 0, ContextCompat.getColor(buttonSave.getContext(), R.color.transparent));
        tv.setGravity(Gravity.CENTER);
        toast.setView(view);
        toast.show();
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell);
        ButterKnife.bind(this);

        timePickerBuilder = new TimePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.CustomBetterPickerTheme);

        profileRepository = new ProfileRepository(getApplicationContext());

        schoolTimes = new HashMap<>();

        Map<String, Object> profiles = profileRepository.retrieve();

        schoolTimes.put(Constant.DAY_MONDAY, profiles.get(Constant.DAY_MONDAY));
        monday.setText(String.valueOf(schoolTimes.get(Constant.DAY_MONDAY)));

        schoolTimes.put(Constant.DAY_TUESDAY, profiles.get(Constant.DAY_TUESDAY));
        tuesday.setText(String.valueOf(schoolTimes.get(Constant.DAY_TUESDAY)));

        schoolTimes.put(Constant.DAY_WEDNESDAY, profiles.get(Constant.DAY_WEDNESDAY));
        wednesday.setText(String.valueOf(schoolTimes.get(Constant.DAY_WEDNESDAY)));

        schoolTimes.put(Constant.DAY_THURSDAY, profiles.get(Constant.DAY_THURSDAY));
        thursday.setText(String.valueOf(schoolTimes.get(Constant.DAY_THURSDAY)));

        schoolTimes.put(Constant.DAY_FRIDAY, profiles.get(Constant.DAY_FRIDAY));
        friday.setText(String.valueOf(schoolTimes.get(Constant.DAY_FRIDAY)));

        schoolTimes.put(Constant.DAY_SATURDAY, profiles.get(Constant.DAY_SATURDAY));
        saturday.setText(String.valueOf(schoolTimes.get(Constant.DAY_SATURDAY)));
    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
        String pickedTime = AlarmClock.formatTime(BellActivity.this, hourOfDay, minute);
        switch (reference) {
            case Constant.NUM_DAY_MONDAY:
                monday.setText(pickedTime);
                schoolTimes.put(Constant.DAY_MONDAY, pickedTime);
                break;
            case Constant.NUM_DAY_TUESDAY:
                tuesday.setText(pickedTime);
                schoolTimes.put(Constant.DAY_TUESDAY, pickedTime);
                break;
            case Constant.NUM_DAY_WEDNESDAY:
                wednesday.setText(pickedTime);
                schoolTimes.put(Constant.DAY_WEDNESDAY, pickedTime);
                break;
            case Constant.NUM_DAY_THURSDAY:
                thursday.setText(pickedTime);
                schoolTimes.put(Constant.DAY_THURSDAY, pickedTime);
                break;
            case Constant.NUM_DAY_FRIDAY:
                friday.setText(pickedTime);
                schoolTimes.put(Constant.DAY_FRIDAY, pickedTime);
                break;
            case Constant.NUM_DAY_SATURDAY:
                saturday.setText(pickedTime);
                schoolTimes.put(Constant.DAY_SATURDAY, pickedTime);
                break;
        }
    }
}
