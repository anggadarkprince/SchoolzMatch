package com.sketchproject.schoolzmatch.modules;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.sketchproject.schoolzmatch.MainActivity;
import com.sketchproject.schoolzmatch.R;

/**
 * Sketch Project Studio
 * Created by Angga on 01/09/2016 14.23.
 */
public class BootReceiverHandler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Schoolz Match")
                    .setContentText("Hello alarm, the schedule has been set!");

            Intent resultIntent = new Intent(context, MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            notificationManager.notify(111, mBuilder.build());
        }
    }
}
