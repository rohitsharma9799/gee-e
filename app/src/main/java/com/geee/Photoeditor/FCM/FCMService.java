/*
package com.geee.Photoeditor.FCM;

*/
/**
 * Created by Ajay Tiwari on 26/08/18.
 *//*


import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.geee.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
//    Utils utils = new Utils();
    String Tag = "FCMService";

    Context context;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedHelper.putKey(getApplicationContext(), "device_token", "" + s);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),remoteMessage.getData());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sendNotification(String messageTitle, String messageBody, Map<String, String> data) {
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" +"");  //Here is FILE_NAME is the name of file that you want to play
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(String.valueOf(data));
        intent.putExtra("messageBody", messageBody);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 */
/* Request code *//*
, intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.camcrowslogo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(Notification.PRIORITY_MAX); //Important for heads-up notification
        Notification buildNotification = mBuilder.build();
        buildNotification.defaults |= Notification.DEFAULT_SOUND;
        mBuilder.setVibrate(pattern);
        int notifyId = (int) System.currentTimeMillis(); //For each push the older one will not be replaced for this unique id
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH; //Important   for heads-up notification
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id),name,importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setSound(sound,attributes);
            channel.setVibrationPattern(pattern);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(notifyId, buildNotification);
            }
        }else{
            NotificationManager mNotifyMgr =   (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            if (mNotifyMgr != null) {
                mNotifyMgr.notify(notifyId, buildNotification);
            }
        }
    }
}
*/
