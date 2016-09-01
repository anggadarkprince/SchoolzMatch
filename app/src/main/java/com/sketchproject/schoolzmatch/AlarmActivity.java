package com.sketchproject.schoolzmatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketchproject.schoolzmatch.utils.AlarmClock;
import com.sketchproject.schoolzmatch.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmActivity extends Activity {
    private int alarmId;

    @BindView(R.id.wave)
    ImageView wave;

    @BindView(R.id.activity)
    TextView textActivity;

    @BindView(R.id.nextActivity)
    TextView textNext;

    @OnClick(R.id.buttonTurnOff)
    void cancelAlarm() {
        AlarmClock.cancelAlarm(alarmId);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString(Constant.ALARM_MESSAGE);
        String next = bundle.getString(Constant.ALARM_NEXT);
        alarmId = bundle.getInt(Constant.ALARM_ID);

        textActivity.setText(message);
        textNext.setText(next);

        Animation waveAnimation = AnimationUtils.loadAnimation(this, R.anim.wave_anim);
        waveAnimation.setRepeatCount(Animation.INFINITE);
        wave.startAnimation(waveAnimation);
    }

    @Override
    public void onBackPressed() {
    }
}
