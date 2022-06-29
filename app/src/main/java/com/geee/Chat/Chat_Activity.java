package com.geee.Chat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geee.Constants;
/*import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;*/
import com.geee.Utils.MyApplication;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.geee.Chat.Videos.Chat_Send_file_Service;
import com.geee.Chat.Videos.Play_Video_F;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.Item_Details;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Chat_Activity extends Fragment {

    public static String token = "null";
    public static String uploadingImageId = "none";
    public static SharedPreferences downloadPref;
    final ArrayList<String> urlList = new ArrayList<>();
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    DatabaseReference rootref;
    String receiverid = "";
    String receiverName = "username";
    String receiverPic = "Null";
    EditText message;
    RecyclerView chatrecyclerview;
    TextView userName;
    ChatAdapter mAdapter;
    ProgressBar progressBar;
    Query queryGetchat;
    ImageView profileimage;
    Context context;
    IOSDialog loddingView;
    View view;
    LinearLayout gifLayout;
    ImageButton uploadStikerBtn, uploadGifBtn;
    TextView sendbtn;
    File direct;
 //   MediaType mediaType;
    ValueEventListener valueEventListener;
    ChildEventListener eventListener;
    String imageFilePath;
    // receive the type indication to show that your friend is typing or not
    LinearLayout mainlayout;
    // this is related with the list of Gifs that is show in the list below
    Gif_Adapter gifAdapter;
    RecyclerView gipsList;
   // GPHApi client;
    private DatabaseReference adduserToInbox;
    private DatabaseReference mchatrefReteriving;
    private DatabaseReference sendTypingIndication;
    private DatabaseReference receiveTypingIndication;
    private List<Chat_GetSet> mChats = new ArrayList<>();
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_chats, container, false);

        context = getContext();


        Variables.userId = SharedPrefrence.getUserIdFromJson(getContext());

        Variables.userName = SharedPrefrence.getUserNameFromJson(getContext());
        Variables.userPic = SharedPrefrence.getUserImgFromJson(getContext());

        downloadPref = context.getSharedPreferences(Variables.downloadPref, Context.MODE_PRIVATE);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);

        direct = new File(Environment.getExternalStorageDirectory() + "/Chatbuzz/");

        // intialize the database refer
        rootref = FirebaseDatabase.getInstance().getReference();
        adduserToInbox = FirebaseDatabase.getInstance().getReference();

        message = (EditText) view.findViewById(R.id.msgedittext);

        userName = view.findViewById(R.id.username);
        profileimage = view.findViewById(R.id.profileimage);

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideSoftKeyboard(getActivity());
                getActivity().onBackPressed();


            }
        });

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                //text = mEdit.toString();

                if (message.getText().toString().length() == 0) {
                    showSendingView();
                } else if (message.getText().toString().length() > 0) {
                    hideSendingView();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        // the send id and reciever id from the back activity in which we come from
        Bundle bundle = getArguments();
        if (bundle != null) {
            receiverid = bundle.getString("receiver_id");
            receiverPic = bundle.getString("receiver_pic");
            receiverName = bundle.getString("receiver_name");


            token = SharedPrefrence.getToken(getActivity());
            userName.setText(receiverName);

            Picasso.get().load(receiverPic)
                    .placeholder(R.drawable.ic_profile_gray)
                    .error(R.drawable.ic_profile_gray)
                    .into(profileimage);
            //Uri uri = Uri.parse(receiverPic);
            //profileimage.setImageURI(uri);

        }


        progressBar = view.findViewById(R.id.progress_bar);
        // this is the black color loader that we see whan we click on save button
        loddingView = new IOSDialog.Builder(context)
                .setCancelable(false)
                .setSpinnerClockwise(false)
                .setMessageContentGravity(Gravity.END)
                .build();

        //set layout manager to chat recycler view and get all the privous chat of th user which spacifc user
        chatrecyclerview = (RecyclerView) view.findViewById(R.id.chatlist);
        final LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setStackFromEnd(true);
        chatrecyclerview.setLayoutManager(layout);
        chatrecyclerview.setHasFixedSize(false);
        OverScrollDecoratorHelper.setUpOverScroll(chatrecyclerview, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mAdapter = new ChatAdapter(mChats, Variables.userId, context, new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chat_GetSet item, View v) {

                if (item.getType().equals("image"))
                    openfullsizeImage(item);

                if (item.getType().equals("video")) {

                    File fullpath = new File(Environment.getExternalStorageDirectory() + "/Chatbuzz/" + item.chat_id + ".mp4");
                    if (fullpath.exists()) {
                        openVideo(fullpath.getAbsolutePath());
                    } else {
                        if (downloadPref.getString(item.getChat_id(), "").equals("")) {
                            long downlodid = Functions.downloadDataForChat(context, item);
                            downloadPref.edit().putString(item.getChat_id(), "" + downlodid).commit();
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                } else if (item.getType().equals("Post")) {

                    Bundle bundle_linear_posts = new Bundle();
                    bundle_linear_posts.putString("post_id", "" + item.getPic_url());
                    bundle_linear_posts.putString("user_id", "" + SharedPrefrence.getUserIdFromJson(getContext()));
                    bundle_linear_posts.putString("type", "showAllPosts");

                    // set Fragmentclass Arguments
                    Item_Details f = new Item_Details();
                    bundle_linear_posts.putString("fragment", "other");
                    f.setArguments(bundle_linear_posts);
                    FragmentManager fm = getChildFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.Chat_F, f).commit();

                }


            }


        }, new ChatAdapter.OnLongClickListener() {
            @Override
            public void onLongclick(Chat_GetSet item, View view) {


            }
        });


        chatrecyclerview.setAdapter(mAdapter);


        chatrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean userScrolled;
            int scrollOutitems;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollOutitems = layout.findFirstCompletelyVisibleItemPosition();

                if (userScrolled && (scrollOutitems == 0 && mChats.size() > 9)) {
                    userScrolled = false;
                    loddingView.show();
                    rootref.child("chat").child(Variables.userId + "-" + receiverid).orderByChild("chat_id")
                            .endAt(mChats.get(0).getChat_id()).limitToLast(20)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<Chat_GetSet> arrayList = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Chat_GetSet item = snapshot.getValue(Chat_GetSet.class);
                                        arrayList.add(item);
                                    }
                                    for (int i = arrayList.size() - 2; i >= 0; i--) {
                                        mChats.add(0, arrayList.get(i));
                                    }

                                    mAdapter.notifyDataSetChanged();
                                    loddingView.cancel();

                                    if (arrayList.size() > 8) {
                                        chatrecyclerview.scrollToPosition(arrayList.size());
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }
        });


        gifLayout = view.findViewById(R.id.gif_layout);

        // this the send btn action in that mehtod we will check message field is empty or not
        // if not then we call a method and pass the message
        sendbtn = view.findViewById(R.id.sendbtn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(message.getText().toString())) {
                    if (gifLayout.getVisibility() == View.VISIBLE) {
                        //searchGif(message.getText().toString());
                    } else {
                        sendMessage(message.getText().toString());
                        message.setText(null);
                    }

                }
            }
        });


        view.findViewById(R.id.uploadimagebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectfile();

            }
        });


        uploadGifBtn = view.findViewById(R.id.upload_gif_btn);
        uploadGifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message.setHint("Search Gifs");
              //  mediaType = MediaType.gif;
                if (gifLayout.getVisibility() != View.VISIBLE) {
                    slideUp();
                }
            }
        });


        uploadStikerBtn = view.findViewById(R.id.upload_stiker_btn);
        uploadStikerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setHint("Search Sticker");
              //  mediaType = MediaType.sticker;
                if (gifLayout.getVisibility() != View.VISIBLE) {
                    slideUp();
                }
               // getGipy();
            }
        });
        final ImageView back = view.findViewById(R.id.Goback);

        final View parent = (View) back.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                back.getHitRect(rect);
                rect.top -= Variables.clickAreaTop100;    // increase top hit area
                rect.left -= Variables.clickAreaLeft100;   // increase left hit area
                rect.bottom += Variables.clickAreaBottom100; // increase bottom hit area
                rect.right += 300;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, back));
            }
        });

        view.findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideSoftKeyboard(getActivity());
                getActivity().onBackPressed();
            }
        });


        message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sendTypingIndicator(false);
                }
            }
        });


        // this is the message field event lister which tells the second user either the user is typing or not
        // most importent to show type indicator to second user
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    sendTypingIndicator(true);
                    sendbtn.setVisibility(View.VISIBLE);
                } else {
                    sendbtn.setVisibility(View.GONE);
                    sendTypingIndicator(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // this method receiver the type indicator of second user to tell that his friend is typing or not
        receivetypeIndication();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Variables.openedChatId = Variables.userId;

        mChats.clear();
        mchatrefReteriving = FirebaseDatabase.getInstance().getReference();
        queryGetchat = mchatrefReteriving.child("chat").child(Variables.userId + "-" + receiverid);


        // this will get all the messages between two users
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Log.d("resp", dataSnapshot.toString());
                    Chat_GetSet model = dataSnapshot.getValue(Chat_GetSet.class);
                    mChats.add(model);
                    mAdapter.notifyDataSetChanged();
                    chatrecyclerview.scrollToPosition(mChats.size() - 1);
                } catch (Exception ex) {
                    Log.e("", ex.getMessage());
                }
                ChangeStatus();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    try {
                        Chat_GetSet model = dataSnapshot.getValue(Chat_GetSet.class);

                        for (int i = mChats.size() - 1; i >= 0; i--) {
                            if (mChats.get(i).getTimestamp().equals(dataSnapshot.child("timestamp").getValue())) {
                                mChats.remove(i);
                                mChats.add(i, model);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", databaseError.getMessage());
            }
        };

        // this will check the two user are do chat before or not
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(Variables.userId + "-" + receiverid)) {
                    progressBar.setVisibility(View.GONE);
                    queryGetchat.removeEventListener(valueEventListener);
                } else {
                    progressBar.setVisibility(View.GONE);
                    queryGetchat.removeEventListener(valueEventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        queryGetchat.limitToLast(20).addChildEventListener(eventListener);

        mchatrefReteriving.child("chat").addValueEventListener(valueEventListener);
    }

    // this method will change the status to ensure that
    // user is seen all the message or not (in both chat node and Chatinbox node)
    public void ChangeStatus() {
        final Date c = Calendar.getInstance().getTime();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query query1 = reference.child("chat").child(receiverid + "-" + Variables.userId).orderByChild("status").equalTo("0");
        final Query query2 = reference.child("chat").child(Variables.userId + "-" + receiverid).orderByChild("status").equalTo("0");

        final DatabaseReference inboxChangeStatus1 = reference.child("Inbox").child(Variables.userId + "/" + receiverid);
        final DatabaseReference inboxChangeStatus2 = reference.child("Inbox").child(receiverid + "/" + Variables.userId);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    if (!nodeDataSnapshot.child("sender_id").getValue().equals(Variables.userId)) {
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "chat" + "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        result.put("time", Variables.df2.format(c));
                        reference.child(path).updateChildren(result);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    if (!nodeDataSnapshot.child("sender_id").getValue().equals(Variables.userId)) {
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "chat" + "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        result.put("time", Variables.df2.format(c));
                        reference.child(path).updateChildren(result);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inboxChangeStatus1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("rid").getValue().equals(receiverid)) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        inboxChangeStatus1.updateChildren(result);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inboxChangeStatus2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("rid").getValue().equals(receiverid)) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        inboxChangeStatus2.updateChildren(result);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    // this will add the new message in chat node and update the ChatInbox by new message by present date
    public void sendMessage(final String message) {

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        final String currentUserRef = "chat" + "/" + Variables.userId + "-" + receiverid;
        final String chatUserRef = "chat" + "/" + receiverid + "-" + Variables.userId;

        DatabaseReference reference = rootref.child("chat").child(Variables.userId + "-" + receiverid).push();
        final String pushid = reference.getKey();
        final HashMap messageUserMap = new HashMap<>();
        messageUserMap.put("chat_id", pushid);
        messageUserMap.put("sender_id", Variables.userId);
        messageUserMap.put("receiver_id", receiverid);
        messageUserMap.put("sender_name", Variables.userName);

        messageUserMap.put("rec_img", "");
        messageUserMap.put("pic_url", "");
        messageUserMap.put("lat", "");
        messageUserMap.put("long", "");

        messageUserMap.put("text", message);
        messageUserMap.put("type", "text");
        messageUserMap.put("status", "0");
        messageUserMap.put("time", "");
        messageUserMap.put("timestamp", formattedDate);

        final HashMap user_map = new HashMap<>();
        user_map.put(currentUserRef + "/" + pushid, messageUserMap);
        user_map.put(chatUserRef + "/" + pushid, messageUserMap);

        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //if first message then set the visibility of whoops layout gone
                String inbox_sender_ref = "Inbox" + "/" + Variables.userId + "/" + receiverid;
                String inbox_receiver_ref = "Inbox" + "/" + receiverid + "/" + Variables.userId;

                HashMap<String, String> sendermap = new HashMap<>();
                sendermap.put("rid", Variables.userId);
                sendermap.put("name", Variables.userName);
                sendermap.put("msg", message);
                sendermap.put("pic", Variables.userPic);
                sendermap.put("timestamp", formattedDate);
                sendermap.put("date", formattedDate);

                sendermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
                sendermap.put("status", "0");
                sendermap.put("block", "0");
                sendermap.put("read", "0");

                HashMap<String, String> receivermap = new HashMap<>();
                receivermap.put("rid", receiverid);
                receivermap.put("name", receiverName);
                receivermap.put("msg", message);
                receivermap.put("pic", receiverPic);
                receivermap.put("timestamp", formattedDate);
                receivermap.put("date", formattedDate);

                receivermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
                receivermap.put("status", "0");
                receivermap.put("block", "0");
                receivermap.put("read", "0");


                HashMap bothUserMap = new HashMap<>();
                bothUserMap.put(inbox_sender_ref, receivermap);
                bothUserMap.put(inbox_receiver_ref, sendermap);

                adduserToInbox.updateChildren(bothUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        sendPushNotification(message);

                    }
                });
            }
        });
    }

    // this method will upload the image in chhat
    public void uploadImage(ByteArrayOutputStream byteArrayOutputStream) {
        byte[] data = byteArrayOutputStream.toByteArray();
        Log.i("buiuiiju",receiverid);

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        DatabaseReference dref = rootref.child("chat").child(Variables.userId + "-" + receiverid).push();
        final String key = dref.getKey();
        uploadingImageId = key;
        final String current_user_ref = "chat" + "/" + Variables.userId + "-" + receiverid;
        final String chat_user_ref = "chat" + "/" + receiverid + "-" + Variables.userId;

        HashMap my_dummi_pic_map = new HashMap<>();
        my_dummi_pic_map.put("receiver_id", receiverid);
        my_dummi_pic_map.put("sender_id", Variables.userId);
        my_dummi_pic_map.put("sender_name", Variables.userName);
        my_dummi_pic_map.put("chat_id", key);

        my_dummi_pic_map.put("rec_img", "");
        my_dummi_pic_map.put("pic_url", "none");
        my_dummi_pic_map.put("lat", "");
        my_dummi_pic_map.put("long", "");

        my_dummi_pic_map.put("text", "");
        my_dummi_pic_map.put("type", "image");
        my_dummi_pic_map.put("status", "0");
        my_dummi_pic_map.put("time", "");
        my_dummi_pic_map.put("timestamp", formattedDate);

        HashMap dummy_push = new HashMap<>();
        dummy_push.put(current_user_ref + "/" + key, my_dummi_pic_map);
        rootref.updateChildren(dummy_push);

        reference.child("images").child(key + ".jpg").putBytes(data).addOnSuccessListener(taskSnapshot -> {


            reference.child("images").child(key + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {

                uploadingImageId = "none";

                HashMap message_user_map = new HashMap<>();
                message_user_map.put("receiver_id", receiverid);
                message_user_map.put("sender_id", Variables.userId);
                message_user_map.put("sender_name", Variables.userName);
                message_user_map.put("chat_id", key);

                message_user_map.put("rec_img", "");
                message_user_map.put("pic_url", uri.toString());
                message_user_map.put("lat", "");
                message_user_map.put("long", "");

                message_user_map.put("text", "");
                message_user_map.put("type", "image");
                message_user_map.put("status", "0");
                message_user_map.put("time", "");
                message_user_map.put("timestamp", formattedDate);

                HashMap user_map = new HashMap<>();

                user_map.put(current_user_ref + "/" + key, message_user_map);
                user_map.put(chat_user_ref + "/" + key, message_user_map);


                rootref.updateChildren(user_map, (databaseError, databaseReference) -> {
                    String inbox_sender_ref = "Inbox" + "/" + Variables.userId + "/" + receiverid;
                    String inbox_receiver_ref = "Inbox" + "/" + receiverid + "/" + Variables.userId;


                    HashMap<String, String> sendermap = new HashMap<>();
                    sendermap.put("rid", Variables.userId);
                    sendermap.put("name", Variables.userName);
                    sendermap.put("msg", "Send an Image");
                    sendermap.put("pic", Variables.userPic);
                    sendermap.put("timestamp", formattedDate);
                    sendermap.put("date", formattedDate);

                    sendermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
                    sendermap.put("status", "0");
                    sendermap.put("block", "0");
                    sendermap.put("read", "0");

                    HashMap<String, String> receivermap = new HashMap<>();
                    receivermap.put("rid", receiverid);
                    receivermap.put("name", receiverName);
                    receivermap.put("msg", "Send an Image");
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

                    adduserToInbox.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            sendPushNotification("Send an image");

                        }
                    });
                });


            });


        });
    }

    // this method will upload the image in chhat
    public void UploadImage(ByteArrayOutputStream byteArrayOutputStream) {
        byte[] data = byteArrayOutputStream.toByteArray();

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        DatabaseReference dref = rootref.child("chat").child(Variables.userId + "-" + receiverid).push();
        final String key = dref.getKey();
        uploadingImageId = key;
        final String current_user_ref = "chat" + "/" + Variables.userId + "-" + receiverid;
        final String chat_user_ref = "chat" + "/" + receiverid + "-" + Variables.userId;

        HashMap my_dummi_pic_map = new HashMap<>();
        my_dummi_pic_map.put("receiver_id", receiverid);
        my_dummi_pic_map.put("sender_id", Variables.userId);
        my_dummi_pic_map.put("sender_name", Variables.userName);
        my_dummi_pic_map.put("chat_id", key);

        my_dummi_pic_map.put("rec_img", "");
        my_dummi_pic_map.put("pic_url", "none");
        my_dummi_pic_map.put("lat", "");
        my_dummi_pic_map.put("long", "");

        my_dummi_pic_map.put("text", "");
        my_dummi_pic_map.put("type", "image");
        my_dummi_pic_map.put("status", "0");
        my_dummi_pic_map.put("time", "");
        my_dummi_pic_map.put("timestamp", formattedDate);

        HashMap dummy_push = new HashMap<>();
        dummy_push.put(current_user_ref + "/" + key, my_dummi_pic_map);
        rootref.updateChildren(dummy_push);

        reference.child("images").child(key + ".jpg").putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploadingImageId = "none";

                HashMap message_user_map = new HashMap<>();
                message_user_map.put("receiver_id", receiverid);
                message_user_map.put("sender_id", Variables.userId);
                message_user_map.put("sender_name", Variables.userName);
                message_user_map.put("chat_id", key);

                message_user_map.put("rec_img", "");
                message_user_map.put("pic_url", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                message_user_map.put("lat", "");
                message_user_map.put("long", "");

                message_user_map.put("text", "");
                message_user_map.put("type", "image");
                message_user_map.put("status", "0");
                message_user_map.put("time", "");
                message_user_map.put("timestamp", formattedDate);

                HashMap user_map = new HashMap<>();

                user_map.put(current_user_ref + "/" + key, message_user_map);
                user_map.put(chat_user_ref + "/" + key, message_user_map);

                rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        String inbox_sender_ref = "Inbox" + "/" + Variables.userId + "/" + receiverid;
                        String inbox_receiver_ref = "Inbox" + "/" + receiverid + "/" + Variables.userId;


                        HashMap<String, String> sendermap = new HashMap<>();
                        sendermap.put("rid", Variables.userId);
                        sendermap.put("name", Variables.userName);
                        sendermap.put("msg", "Send an Image");
                        sendermap.put("pic", Variables.userPic);
                        sendermap.put("timestamp", formattedDate);
                        sendermap.put("date", formattedDate);

                        sendermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
                        sendermap.put("status", "0");
                        sendermap.put("block", "0");
                        sendermap.put("read", "0");

                        HashMap<String, String> receivermap = new HashMap<>();
                        receivermap.put("rid", receiverid);
                        receivermap.put("name", receiverName);
                        receivermap.put("msg", "Send an Image");
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

                        adduserToInbox.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                sendPushNotification("Send an image");


                            }
                        });
                    }
                });
            }
        });
    }

    // this method will upload the image in chhat
    public void SendGif(String url) {
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);


        DatabaseReference dref = rootref.child("chat").child(Variables.userId + "-" + receiverid).push();
        final String key = dref.getKey();

        String currentUserRef = "chat" + "/" + Variables.userId + "-" + receiverid;
        String chatUserRef = "chat" + "/" + receiverid + "-" + Variables.userId;

        HashMap messageUserMap = new HashMap<>();
        messageUserMap.put("receiver_id", receiverid);
        messageUserMap.put("sender_id", Variables.userId);
        messageUserMap.put("sender_name", Variables.userName);
        messageUserMap.put("chat_id", key);


        messageUserMap.put("rec_img", "");
        messageUserMap.put("pic_url", url);
        messageUserMap.put("lat", "");
        messageUserMap.put("long", "");

        messageUserMap.put("text", "");
        messageUserMap.put("type", "gif");
        messageUserMap.put("status", "0");
        messageUserMap.put("time", "");
        messageUserMap.put("timestamp", formattedDate);

        HashMap userMap = new HashMap<>();

        userMap.put(currentUserRef + "/" + key, messageUserMap);
        userMap.put(chatUserRef + "/" + key, messageUserMap);

        rootref.updateChildren(userMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String inbox_sender_ref = "Inbox" + "/" + Variables.userId + "/" + receiverid;
                String inbox_receiver_ref = "Inbox" + "/" + receiverid + "/" + Variables.userId;


                HashMap<String, String> sendermap = new HashMap<>();
                sendermap.put("rid", Variables.userId);
                sendermap.put("name", Variables.userName);
                sendermap.put("msg", "send a gif");
                sendermap.put("pic", Variables.userPic);
                sendermap.put("timestamp", formattedDate);
                sendermap.put("date", formattedDate);

                sendermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
                sendermap.put("status", "0");
                sendermap.put("block", "0");
                sendermap.put("read", "0");

                HashMap<String, String> receivermap = new HashMap<>();
                receivermap.put("rid", receiverid);
                receivermap.put("name", receiverName);
                receivermap.put("msg", "send a gif");
                receivermap.put("pic", receiverPic);
                receivermap.put("timestamp", formattedDate);
                receivermap.put("date", formattedDate);

                receivermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
                receivermap.put("status", "0");
                receivermap.put("block", "0");
                receivermap.put("read", "0");


                HashMap bothUserMap = new HashMap<>();
                bothUserMap.put(inbox_sender_ref, receivermap);
                bothUserMap.put(inbox_receiver_ref, sendermap);

                adduserToInbox.updateChildren(bothUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        sendPushNotification("Send an Gif");

                    }
                });

            }
        });
    }

    //this method will get the big size of image in private chat
    public void openVideo(String path) {
        Play_Video_F play_video_f = new Play_Video_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("path", path);
        play_video_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Chat_F, play_video_f).commit();

    }

    // this method will show the dialog of selete the either take a picture form camera or pick the image from gallary
    private void selectfile() {

        final CharSequence[] options = {"Take Photo", "Choose Photo from Gallery", "Select video", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

        builder.setTitle("Select");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (checkCamrapermission() && checkWriteStoragepermission())
                        openCameraIntent();

                } else if (options[item].equals("Choose Photo from Gallery")) {

                    if (checkReadStoragepermission()) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                } else if (options[item].equals("Select video")) {
                    if (checkReadStoragepermission()) {
                        chooseVideo();
                    }
                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    private boolean checkCamrapermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA}, Variables.permissionCameraCode);
        }
        return false;
    }

    private boolean checkReadStoragepermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Variables.permissionReadData);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    private boolean checkWriteStoragepermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Variables.permissionWriteData);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Variables.permissionCameraCode) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap again", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();

            }
        }

        if (requestCode == Variables.permissionReadData) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap again", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == Variables.permissionWriteData) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap Again", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == Variables.permissionRecordingAudio) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap Again", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Functions.logDMsg("" + ex.toString());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getActivity().getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, PICK_IMAGE_CAMERA);
            }
        }


    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    private void chooseVideo() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, Variables.vedioRequestCode);
    }

    //on image select activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Matrix matrix = Functions.getOrentation(imageFilePath);
                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));

                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                uploadImage(baos);

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path = getPath(selectedImage);

                Matrix matrix = Functions.getOrentation(path);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                uploadImage(baos);
            } else if (requestCode == Variables.vedioRequestCode) {
                Uri selectedImageUri = data.getData();
                Chat_Send_file_Service mService = new Chat_Send_file_Service();
                if (!Functions.isMyServiceRunning(context, mService.getClass())) {
                    Intent mServiceIntent = new Intent(context.getApplicationContext(), mService.getClass());
                    mServiceIntent.setAction("startservice");
                    mServiceIntent.putExtra("uri", "" + selectedImageUri);
                    mServiceIntent.putExtra("type", "video");

                    mServiceIntent.putExtra("sender_id", Variables.userId);
                    mServiceIntent.putExtra("sender_name", Variables.userName);
                    mServiceIntent.putExtra("sender_pic", Variables.userPic);

                    mServiceIntent.putExtra("receiver_id", receiverid);
                    mServiceIntent.putExtra("receiver_name", receiverName);
                    mServiceIntent.putExtra("receiver_pic", receiverPic);

                    mServiceIntent.putExtra("token", token);

                    context.startService(mServiceIntent);
                } else {
                    Toast.makeText(context, "Please wait video already in uploading progress", Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    // send the type indicator if the user is typing message
    public void sendTypingIndicator(boolean indicate) {
        // if the type incator is present then we remove it if not then we create the typing indicator
        if (indicate) {
            final HashMap messageUserMap = new HashMap<>();
            messageUserMap.put("receiver_id", receiverid);
            messageUserMap.put("sender_id", Variables.userId);

            sendTypingIndication = FirebaseDatabase.getInstance().getReference().child("typing_indicator");
            sendTypingIndication.child(Variables.userId + "-" + receiverid).setValue(messageUserMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    sendTypingIndication.child(receiverid + "-" + Variables.userId).setValue(messageUserMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
            });
        } else {
            sendTypingIndication = FirebaseDatabase.getInstance().getReference().child("typing_indicator");

            sendTypingIndication.child(Variables.userId + "-" + receiverid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    sendTypingIndication.child(receiverid + "-" + Variables.userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                }
            });

        }

    }

    public void receivetypeIndication() {
        mainlayout = view.findViewById(R.id.typeindicator);

        receiveTypingIndication = FirebaseDatabase.getInstance().getReference().child("typing_indicator");
        receiveTypingIndication.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(receiverid + "-" + Variables.userId).exists()) {
                    String receiver = String.valueOf(dataSnapshot.child(receiverid + "-" + Variables.userId).child("sender_id").getValue());
                    if (receiver.equals(receiverid)) {
                        mainlayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    mainlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // on destory delete the typing indicator
    @Override
    public void onDestroy() {
        super.onDestroy();
        uploadingImageId = "none";
        sendTypingIndicator(false);
        queryGetchat.removeEventListener(eventListener);

    }

    // on stop delete the typing indicator and remove the value event listener
    @Override
    public void onStop() {
        super.onStop();
        sendTypingIndicator(false);
        queryGetchat.removeEventListener(eventListener);
        Variables.openedChatId = "null";
    }

    //this method will get the big size of image in private chat
    public void openfullsizeImage(Chat_GetSet item) {
        See_Full_Image_F see_image_f = new See_Full_Image_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        Bundle args = new Bundle();
        args.putSerializable("image_url", item.getPic_url());
        args.putSerializable("chat_id", item.getChat_id());
        see_image_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Chat_F, see_image_f).commit();

    }
/*

    public void getGipy() {
        if (client == null)
            client = new GPHApiClient(context.getResources().getString(R.string.gif_api_key));

        urlList.clear();
        gipsList = view.findViewById(R.id.gif_recylerview);

        ImageButton hideGifLayout = view.findViewById(R.id.hide_gif_layout);
        hideGifLayout.setOnClickListener(v -> slideDown());


        gipsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        gifAdapter = new Gif_Adapter(context, urlList, new Gif_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                SendGif(item);
                slideDown();
            }
        });
        gipsList.setAdapter(gifAdapter);

        client.trending(mediaType, null, null, null, (result, e) -> {
            if (result == null) {
                // Do what you want to do with the error
            } else {
                if (result.getData() != null) {
                    for (Media gif : result.getData()) {


                        urlList.add(gif.getId());
                    }
                    gifAdapter.notifyDataSetChanged();

                } else {
                    Log.e("giphy error", "No results found");
                }
            }
        });
    }



    public void searchGif(String search) {
        /// Gif Search
        client.search(search, mediaType, null, null, null, null, (result, e) -> {
            if (result == null) {
                // Do what you want to do with the error
            } else {
                if (result.getData() != null) {
                    urlList.clear();
                    for (Media gif : result.getData()) {
                        urlList.add(gif.getId());
                        gifAdapter.notifyDataSetChanged();
                    }
                    gipsList.smoothScrollToPosition(0);

                } else {
                    Log.e("giphy error", "No results found");
                }
            }
        });
    }
*/

    // slide the view from below itself to the current position
    public void slideUp() {
        uploadGifBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gif));
        gifLayout.setVisibility(View.VISIBLE);
        //sendbtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_search));
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(700);
        gifLayout.startAnimation(animate);
    }


    // slide the view from its current position to below itself
    public void slideDown() {
        message.setHint("Type your message here...");
        message.setText("");
        uploadGifBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gif));
        gifLayout.setVisibility(View.GONE);

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(700);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gifLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        gifLayout.startAnimation(animate);

    }


    public void sendPushNotification(String message) {
        if (!token.equals("null")) {

            Map<String, String> notimap = new HashMap<>();
            notimap.put("name", Variables.userName);
            notimap.put("message", message);
            notimap.put("picture", Variables.userPic);
            notimap.put("token", token);
            notimap.put("RidorGroupid", Variables.userId);
            notimap.put("FromWhere", "user");
            rootref.child("notifications").child(SharedPrefrence.getUserIdFromJson(MyApplication.getAppContext())).push().setValue(notimap);
        }
    }


    // Hide View

    public void hideSendingView() {
        uploadGifBtn.setVisibility(View.GONE);
        uploadStikerBtn.setVisibility(View.GONE);
        ImageView uploadimagebtn = view.findViewById(R.id.uploadimagebtn);
        uploadimagebtn.setVisibility(View.GONE);
    }

    // Display sending view

    public void showSendingView() {

        uploadGifBtn.setVisibility(View.VISIBLE);
        uploadStikerBtn.setVisibility(View.VISIBLE);
        ImageView uploadimagebtn = view.findViewById(R.id.uploadimagebtn);
        uploadimagebtn.setVisibility(View.VISIBLE);


    }


}
