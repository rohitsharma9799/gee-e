package com.geee.Chat.Videos;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Main_F;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AQEEL on 6/7/2018.
 */

public class Chat_Send_file_Service extends Service {

    Uri uri;
    StorageReference reference;
    DatabaseReference rootref;
    String senderId, senderName, senderPic;
    String receiverid, receiverName, receiverPic;

    String token;

    public Chat_Send_file_Service() {
        super();
    }


    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);
        reference = FirebaseStorage.getInstance().getReference();
        rootref = FirebaseDatabase.getInstance().getReference();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            if (intent.getAction().equals("startservice")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startMyOwnForeground();
                else
                    showNotification();


                String uri_string = intent.getStringExtra("uri");
                uri = Uri.parse(uri_string);


                senderId = intent.getStringExtra("sender_id");
                senderName = intent.getStringExtra("sender_name");
                senderPic = intent.getStringExtra("sender_pic");

                receiverid = intent.getStringExtra("receiver_id");
                receiverName = intent.getStringExtra("receiver_name");
                receiverPic = intent.getStringExtra("receiver_pic");

                token = intent.getStringExtra("token");

                String type = intent.getStringExtra("type");
                if (type.equals("video")) {

                    saveVideoInFirebase();

                } else if (type.equals("pdf")) {
                    savePdfInFirebase();
                }

            }


        }
        return Service.START_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Uploading Video")
                .setContentText("Please wait! Video is uploading....")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, Main_F.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification.Builder notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Uploading Video")
                .setContentText("Please wait! Video is uploading....")
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        android.R.drawable.stat_sys_upload))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        startForeground(101, notification.build());
    }

    public void saveVideoInFirebase() {
        DatabaseReference dref = rootref.child("chat").child(senderId + "-" + receiverid).push();
        final String key = dref.getKey();

        StorageReference ref = reference.child("Video").child(key + ".mp4");
        ref.putFile(uri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {

            stopSelf();
            uploadVideo(uri.toString(), key);
        }));
    }

    // this method will upload the image in chhat
    public void uploadVideo(String url, String key) {
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);


        final String current_user_ref = "chat" + "/" + senderId + "-" + receiverid;
        final String chat_user_ref = "chat" + "/" + receiverid + "-" + senderId;


        HashMap message_user_map = new HashMap<>();
        message_user_map.put("receiver_id", receiverid);
        message_user_map.put("sender_id", senderId);
        message_user_map.put("sender_name", Variables.userName);
        message_user_map.put("chat_id", key);

        message_user_map.put("rec_img", "");
        message_user_map.put("pic_url", url);
        message_user_map.put("lat", "");
        message_user_map.put("long", "");

        message_user_map.put("text", "");
        message_user_map.put("type", "video");
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("timestamp", formattedDate);

        HashMap user_map = new HashMap<>();

        user_map.put(current_user_ref + "/" + key, message_user_map);
        user_map.put(chat_user_ref + "/" + key, message_user_map);

        rootref.updateChildren(user_map, (databaseError, databaseReference) -> {
            String inbox_sender_ref = "Inbox" + "/" + senderId + "/" + receiverid;
            String inbox_receiver_ref = "Inbox" + "/" + receiverid + "/" + senderId;


            HashMap<String, String> sendermap = new HashMap<>();
            sendermap.put("rid", senderId);
            sendermap.put("name", senderName);
            sendermap.put("msg", "send a video");
            sendermap.put("pic", senderPic);
            sendermap.put("timestamp", formattedDate);
            sendermap.put("date", formattedDate);

            sendermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
            sendermap.put("status", "0");
            sendermap.put("block", "0");
            sendermap.put("read", "0");

            HashMap<String, String> receivermap = new HashMap<>();
            receivermap.put("rid", receiverid);
            receivermap.put("name", receiverName);
            receivermap.put("msg", "send a video");
            receivermap.put("pic", receiverPic);
            receivermap.put("timestamp", formattedDate);
            receivermap.put("date", formattedDate);

            receivermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
            receivermap.put("status", "0");
            receivermap.put("block", "0");
            receivermap.put("read", "0");


            HashMap both_user_map = new HashMap<>();
            both_user_map.put(inbox_sender_ref, receivermap);
            both_user_map.put(inbox_receiver_ref, sendermap);

            rootref.updateChildren(both_user_map).addOnCompleteListener((OnCompleteListener<Void>) task -> SendPushNotification("Send a video file"));
        });
    }


    public void savePdfInFirebase() {
        DatabaseReference dref = rootref.child("chat").child(senderId + "-" + receiverid).push();
        final String key = dref.getKey();

        reference.child("Document").child(key + ".pdf").putFile(uri).addOnSuccessListener(taskSnapshot -> {
            stopSelf();
            uploadPdf(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(), key);
        });
    }

    // this method will upload the image in chhat
    public void uploadPdf(String url, String key) {
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);


        final String current_user_ref = "chat" + "/" + senderId + "-" + receiverid;
        final String chat_user_ref = "chat" + "/" + receiverid + "-" + senderId;


        HashMap message_user_map = new HashMap<>();
        message_user_map.put("receiver_id", receiverid);
        message_user_map.put("sender_id", senderId);
        message_user_map.put("chat_id", key);
        message_user_map.put("text", "");
        message_user_map.put("type", "pdf");
        message_user_map.put("pic_url", url);
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("sender_name", Variables.userName);
        message_user_map.put("timestamp", formattedDate);

        HashMap user_map = new HashMap<>();

        user_map.put(current_user_ref + "/" + key, message_user_map);
        user_map.put(chat_user_ref + "/" + key, message_user_map);

        rootref.updateChildren(user_map, (databaseError, databaseReference) -> {
            String inbox_sender_ref = "Inbox" + "/" + senderId + "/" + receiverid;
            String inbox_receiver_ref = "Inbox" + "/" + receiverid + "/" + senderId;

            HashMap<String, String> sendermap = new HashMap<>();
            sendermap.put("id", senderId);
            sendermap.put("name", senderName);
            sendermap.put("message", "send a video");
            sendermap.put("pic", senderPic);
            sendermap.put("status", "0");
            sendermap.put("type", "user");
            sendermap.put("timestamp", formattedDate);

            HashMap<String, String> receivermap = new HashMap<>();
            receivermap.put("id", receiverid);
            receivermap.put("name", receiverName);
            receivermap.put("message", "send a Pdf");
            receivermap.put("pic", receiverPic);
            receivermap.put("status", "0");
            receivermap.put("type", "user");
            receivermap.put("timestamp", formattedDate);

            HashMap both_user_map = new HashMap<>();
            both_user_map.put(inbox_sender_ref, receivermap);
            both_user_map.put(inbox_receiver_ref, sendermap);

            rootref.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    SendPushNotification("Send a Pdf file");
                }
            });
        });
    }


    public void SendPushNotification(String message) {
        if (!token.equals("null")) {

            Map<String, String> notimap = new HashMap<>();
            notimap.put("name", senderName);
            notimap.put("message", message);
            notimap.put("picture", senderPic);
            notimap.put("token", token);
            notimap.put("RidorGroupid", senderId);
            notimap.put("FromWhere", "user");
            rootref.child("notifications").child(senderId).push().setValue(notimap);

        }

    }


}