
package com.mindwarriorhack.app.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.view.Home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private LocalBroadcastManager notificationBroadcaster;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String NOTIFICATION_CHANNEL_NAME = "Mind Warrior";
    Bitmap bitmap;


    @Override
    public void onCreate() {
        notificationBroadcaster = LocalBroadcastManager.getInstance(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round);
    }


    @Override
    public void onNewToken(String token) {
        PreferenceManager.setDeviceToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("fromnoti", "From: " + remoteMessage.getFrom());
        try {
            JSONObject notiobject = new JSONObject(String.valueOf(remoteMessage.getNotification()));
            Log.d("notify", "" + notiobject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject object = new JSONObject(remoteMessage.getData());
        Log.d("NotifyObject", object.toString());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("datanoti", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("noti", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (PreferenceManager.isUserLogin()) {
            generateNotification(remoteMessage);
        }
    }


    @SuppressLint("NewApi")
    public void generateNotification(RemoteMessage remoteMessage) {

        int requestID = (int) System.currentTimeMillis();
        Log.d("Inside", "Called");
        Intent intent = new Intent(this, com.mindwarriorhack.app.view.Notification.Notification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notificationFlag", "notify");


        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notify)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLargeIcon(bitmap)
                /*.setContentTitle(remoteMessage.getData().get("title"))*/
                .setContentTitle("Mind Warrior")
                .setContentText(remoteMessage.getData().get("body"))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(remoteMessage.getData().get("body")))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());

    }




    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.canShowBadge();
            //notificationChannel.setSound(Uri.parse("android.resource://com.mysafeschool.app/" + R.raw.notification_1),att);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.BADGE_ICON_LARGE);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
    }
