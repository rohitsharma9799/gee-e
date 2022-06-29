package com.geee.Inner_VP_Package.Home_Package.Share_Bottom_Sheet;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.geee.CodeClasses.Variables;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SharePostUtil {
    DatabaseReference rootref;
    private DatabaseReference Adduser_to_inbox;

    public SharePostUtil() {
        // intialize the database refer
        rootref = FirebaseDatabase.getInstance().getReference();
        Adduser_to_inbox = FirebaseDatabase.getInstance().getReference();

    }

    public void sendMessage(String username, final String message, String senderId, final String Receiverid, final String Receiver_name, final String Receiver_pic) {

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        final String currentUserRef = "chat" + "/" + senderId + "-" + Receiverid;
        final String chatUserRef = "chat" + "/" + Receiverid + "-" + senderId;

        DatabaseReference reference = rootref.child("chat").child(senderId + "-" + Receiverid).push();
        final String pushid = reference.getKey();
        final HashMap messageUserMap = new HashMap<>();
        messageUserMap.put("chat_id", pushid);
        messageUserMap.put("sender_id", senderId);
        messageUserMap.put("receiver_id", Receiverid);
        messageUserMap.put("sender_name", username);

        messageUserMap.put("rec_img", "" + Receiver_pic);
        messageUserMap.put("pic_url", "" + message);
        messageUserMap.put("lat", "");
        messageUserMap.put("long", "");

        messageUserMap.put("text", "Share a Post");
        messageUserMap.put("type", "Post");
        messageUserMap.put("status", "0");
        messageUserMap.put("time", "");
        messageUserMap.put("timestamp", formattedDate);

        final HashMap user_map = new HashMap<>();
        user_map.put(currentUserRef + "/" + pushid, messageUserMap);
        user_map.put(chatUserRef + "/" + pushid, messageUserMap);

        rootref.updateChildren(user_map, (databaseError, databaseReference) -> {
            //if first message then set the visibility of whoops layout gone
            String inbox_sender_ref = "Inbox" + "/" + senderId + "/" + Receiverid;
            String inbox_receiver_ref = "Inbox" + "/" + Receiverid + "/" + senderId;

            HashMap<String, String> sendermap = new HashMap<>();
            sendermap.put("rid", senderId);
            sendermap.put("name", username);
            sendermap.put("msg", "Share a Post");
            sendermap.put("pic", Variables.userPic);
            sendermap.put("timestamp", formattedDate);
            sendermap.put("date", formattedDate);

            sendermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
            sendermap.put("status", "0");
            sendermap.put("block", "0");
            sendermap.put("read", "0");

            HashMap<String, String> receivermap = new HashMap<>();
            receivermap.put("rid", Receiverid);
            receivermap.put("name", Receiver_name);
            receivermap.put("msg", "Share a Post");
            receivermap.put("pic", Receiver_pic);
            receivermap.put("timestamp", formattedDate);
            receivermap.put("date", formattedDate);

            receivermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
            receivermap.put("status", "0");
            receivermap.put("block", "0");
            receivermap.put("read", "0");


            HashMap both_user_map = new HashMap<>();
            both_user_map.put(inbox_sender_ref, receivermap);
            both_user_map.put(inbox_receiver_ref, sendermap);

            Adduser_to_inbox.updateChildren(both_user_map).addOnCompleteListener((OnCompleteListener<Void>) task -> {

            });
        });
    }

}
