package com.sketchproject.schoolzmatch.modules;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.sketchproject.schoolzmatch.AlarmActivity;
import com.sketchproject.schoolzmatch.MainActivity;
import com.sketchproject.schoolzmatch.R;
import com.sketchproject.schoolzmatch.database.Schedule;
import com.sketchproject.schoolzmatch.database.ScheduleRepository;
import com.sketchproject.schoolzmatch.utils.AlarmClock;
import com.sketchproject.schoolzmatch.utils.Constant;

import java.util.List;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static Ringtone ringtone;

    @Override
    public void onReceive(final Context context, Intent intent) {

        int alarmId = intent.getIntExtra(Schedule.ID, 0);
        String message = "No Message";
        String next = "No Next Act";

        if (alarmId == Constant.CHECKER_ID) {
            Log.i("Schedule", "Recheck Schedule");
            //AlarmClock.updateAlarmClock(context);
        } else {
            Log.i("Schedule", "Alarm triggered");
            ScheduleRepository scheduleRepository = new ScheduleRepository(context);
            List<Schedule> schedules = scheduleRepository.retrieve();

            for (int i = 0; i < schedules.size(); i++) {
                if (schedules.get(i).getId() == alarmId) {
                    message = schedules.get(i).getLabel();
                    if (i < schedules.size() - 1) {
                        next = schedules.get(i + 1).getLabel();
                    } else {
                        next = "";
                    }
                }
            }

            // start tts service
            Intent serviceIntent = new Intent(context, TTS.class);
            serviceIntent.putExtra(Constant.ALARM_MESSAGE, message);
            context.startService(serviceIntent);

            // alarm sound
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(context, uri);
            //ringtone.play();

            // notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(message)
                    .setContentText("Tap to launch Schoolz Match!");

            // notification tap action
            Intent resultIntent = new Intent(context.getApplicationContext(), MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            notificationManager.notify(alarmId, mBuilder.build());

            // start alarm activity
            Intent alarmIntent = new Intent(context.getApplicationContext(), AlarmActivity.class);
            alarmIntent.putExtra(Constant.ALARM_MESSAGE, message);
            alarmIntent.putExtra(Constant.ALARM_NEXT, next);
            alarmIntent.putExtra(Constant.ALARM_ID, alarmId);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(alarmIntent);
        }
    }
}
