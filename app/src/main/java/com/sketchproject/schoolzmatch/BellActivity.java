package com.sketchproject.schoolzmatch;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BellActivity extends AppCompatActivity {
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

    }

    @OnClick(R.id.controlTuesday)
    void changeTuesdaySchool() {

    }

    @OnClick(R.id.controlWednesday)
    void changeWednesdaySchool() {

    }

    @OnClick(R.id.controlThursday)
    void changeThursdaySchool() {

    }

    @OnClick(R.id.controlFriday)
    void changeFridaySchool() {

    }

    @OnClick(R.id.controlSaturday)
    void changeSaturdaySchool() {

    }

    @OnClick(R.id.buttonSave)
    void save() {
        Toast toast = Toast.makeText(buttonSave.getContext(), "Schedule data has been updated", Toast.LENGTH_LONG);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell);
        ButterKnife.bind(this);
    }
}
