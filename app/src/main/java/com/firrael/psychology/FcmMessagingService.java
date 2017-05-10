package com.firrael.psychology;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firrael.psychology.view.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Railag on 09.06.2016.
 */
public class FcmMessagingService extends FirebaseMessagingService {
    private static final String TAG = FcmMessagingService.class.getSimpleName();

    private final static String CODE_FIELD = "code";
    private final static String DATA_FIELD = "data";

    public final static String PN_CODE_KEY = "pnCode";
    public final static String PN_DATA_KEY = "pnData";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                Log.d(TAG, entry.getKey() + ": " + entry.getValue());
            }
        }

        if (notification != null) {
            if (notification.getBody() != null)
                Log.i(TAG, notification.getBody());

            showPN(notification, data);
        }
    }

    private void showPN(RemoteMessage.Notification notification, Map<String, String> data) {
        Intent resultIntent = new Intent(getBaseContext(), MainActivity.class);

        if (data.containsKey(CODE_FIELD)) {
            String codeField = data.get(CODE_FIELD);
            try {
                int code = Integer.parseInt(codeField);
                resultIntent.putExtra(PN_CODE_KEY, code);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (data.containsKey(DATA_FIELD))
            resultIntent.putExtra(PN_DATA_KEY, data.get(DATA_FIELD));

        PendingIntent resultPendingIntent = PendingIntent
                .getActivity(getBaseContext(), 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


        //Intent intent = new Intent(CANCEL_NOTIFICATION_ACTION);
        //PendingIntent deleteIntent = PendingIntent.getBroadcast(context, 0,
        //        intent, 0);

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                //   .setSound(RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(resultPendingIntent)
                .setLights(Color.GREEN, 1000, 4000)
                .setSmallIcon(R.drawable.ic_menu_send);*/
        //   .setDeleteIntent(deleteIntent);


        /*int mNotificationId = 1;
        NotificationManager mNotifyMgr = (NotificationManager) getBaseContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification appNotification = builder.build();
        mNotifyMgr.notify(mNotificationId, appNotification);*/
    }
}