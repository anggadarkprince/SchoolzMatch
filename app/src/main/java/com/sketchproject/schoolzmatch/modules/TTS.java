package com.sketchproject.schoolzmatch.modules;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sketchproject.schoolzmatch.utils.Constant;

import java.util.Locale;

/**
 * Sketch Project Studio
 * Created by Angga on 01/09/2016 22.25.
 */
public class TTS extends Service implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    private TextToSpeech mtts;
    private String message = "No message";
    private int counter = 0;

    @Override
    public void onCreate() {
        Log.i("TTSService", "onCreate");
        mtts = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        Log.i("TTSService", "onInit");
        if (status == TextToSpeech.SUCCESS) {
            int result = mtts.setLanguage(Locale.US);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("TTSService", "speech");
                mtts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                counter++;
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.i("TTSService", "onDestroy");
        if (mtts != null) {
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TTSService", "onStartCommand");
        message = "Not it's time to " + intent.getStringExtra(Constant.ALARM_MESSAGE);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        Log.i("TTSService", "onRepeat");
        if (counter > 10) {
            stopSelf();
        } else {
            onInit(0);
        }
    }
}
