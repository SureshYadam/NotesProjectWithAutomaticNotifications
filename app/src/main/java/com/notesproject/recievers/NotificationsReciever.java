package com.notesproject.recievers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.notesproject.AppPreferences;
import com.notesproject.R;

/**
 * Created by suresh on 26/2/17.
 */

public class NotificationsReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Showing notification", Toast.LENGTH_LONG).show();

        showNotification(context);

    }

    private void showNotification(Context context) {

        AppPreferences.getAppPreferences(context).setNotificationCount(AppPreferences.getAppPreferences(context).getNotificationCount() + 1);

        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification\t" + AppPreferences.getAppPreferences(context).getNotificationCount())
                        .setContentText("Here is my latest notification !")
                        .setContentIntent(pendingIntent); //Required on Gingerbread and below


        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(AppPreferences.getAppPreferences(context).getNotificationCount(), mBuilder.build());

    }
}
