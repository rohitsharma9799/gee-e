package com.geee.tictokcode.WatchVideos;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.danikula.videocache.HttpProxyCacheServer;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.geee.Utils.MyApplication;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.geee.R;
import com.geee.tictokcode.Comments.Comment_F;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.geee.tictokcode.KeyBoard.KeyboardHeightObserver;
import com.geee.tictokcode.KeyBoard.KeyboardHeightProvider;
import com.geee.tictokcode.Main_Menu.MainMenuActivity;
import com.geee.tictokcode.SimpleClasses.API_CallBack;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.SimpleClasses.Fragment_Callback;
import com.geee.tictokcode.SimpleClasses.Fragment_Data_Send;
import com.geee.tictokcode.SimpleClasses.Functions;
import com.geee.tictokcode.SimpleClasses.Variables;
import com.geee.tictokcode.SoundLists.VideoSound_A;
import com.geee.tictokcode.Taged.Taged_Videos_F;
import com.geee.tictokcode.VideoAction.VideoAction_F;
import com.geee.tictokcode.Video_Recording.Video_Recoder_Duet_A;


/**
 * A simple {@link Fragment} subclass.
 */

public class WatchVideos_F extends AppCompatActivity implements Player.EventListener,
        KeyboardHeightObserver,View.OnClickListener, Fragment_Data_Send {

    Context context;

    RecyclerView recyclerView;
    ArrayList<Home_Get_Set> data_list;
    int position=0;
    int currentPage=-1;
    LinearLayoutManager layoutManager;

    Watch_Videos_Adapter adapter;

    ProgressBar p_bar;

    private KeyboardHeightProvider keyboardHeightProvider;

    RelativeLayout write_layout;

    EditText message_edit;
    ImageButton send_btn;
    ProgressBar send_progress;


    String video_id;
    String link;

    public WatchVideos_F() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_watchvideo);
        context=this;
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        haveStoragePermission();
        if(Variables.sharedPreferences==null){
            Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,Context.MODE_PRIVATE);
        }

        p_bar=findViewById(R.id.p_bar);



        Intent bundle = getIntent();
        if(bundle!=null){

            Uri appLinkData = bundle.getData();
            video_id=bundle.getStringExtra("video_id");

            if(video_id!=null){
                Call_Api_For_get_Allvideos(video_id);
            }
            else if(appLinkData==null){
                data_list = (ArrayList<Home_Get_Set>) bundle.getSerializableExtra("arraylist");
                position=bundle.getIntExtra("position",0);
                Set_Adapter();
            }
            else {
                link=appLinkData.toString();
                String[] parts = link.split("=");
                video_id=parts[1];
                Call_Api_For_get_Allvideos(parts[1]);
            }

        }




        findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });


        write_layout=findViewById(R.id.write_layout);
        message_edit=findViewById(R.id.message_edit);
        send_btn=findViewById(R.id.send_btn);
        send_btn.setOnClickListener(this);


        send_progress=findViewById(R.id.send_progress);

        keyboardHeightProvider = new KeyboardHeightProvider(this);


        findViewById(R.id.WatchVideo_F).post(new Runnable() {
            public void run() {
                keyboardHeightProvider.start();
            }
        });



    }


    @Override
    public void onBackPressed() {

        if(video_id!=null && link!=null){
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }else {
            super.onBackPressed();
        }

    }

    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_Allvideos(String id) {


        if(MainMenuActivity.token==null)
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            MainMenuActivity.token = token;
                            // Log and toast
                        }
                    });

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            parameters.put("token",MainMenuActivity.token);
            parameters.put("video_id",id);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        ApiRequest.Call_Api(context, Variables.showAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Log.i("DFsdfsdddffsdf",resp);
                Parse_data(resp);
            }
        });


    }

    public void Parse_data(String responce){

        data_list=new ArrayList<>();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject itemdata = msgArray.optJSONObject(i);

                    Home_Get_Set item=new Home_Get_Set();
                    item.fb_id=itemdata.optString("fb_id");

                    JSONObject user_info=itemdata.optJSONObject("user_info");
                    item.username=user_info.optString("username");
                    item.first_name=user_info.optString("first_name",context.getResources().getString(R.string.app_name));
                    item.last_name=user_info.optString("last_name","User");
                  //  item.profile_pic=user_info.optString("profile_pic","null");
                    item.profile_pic = user_info.optString("profile_pic" );
                    item.verified=user_info.optString("verified");

                    JSONObject sound_data=itemdata.optJSONObject("sound");
                    item.sound_id=sound_data.optString("id");
                    item.sound_name=sound_data.optString("sound_name");
                    item.sound_pic=sound_data.optString("thum");
                    if(sound_data!=null) {
                        JSONObject audio_path = sound_data.optJSONObject("audio_path");
                        item.sound_url_mp3 = audio_path.optString("mp3");
                        item.sound_url_acc = audio_path.optString("acc");
                    }


                    JSONObject count=itemdata.optJSONObject("count");
                    item.like_count=count.optString("like_count");
                    item.video_comment_count=count.optString("video_comment_count");


                    item.video_id=itemdata.optString("id");
                    item.liked=itemdata.optString("is_liked");


                    item.video_url=itemdata.optString("video");


                    item.video_description=itemdata.optString("description");

                    item.thum=itemdata.optString("thum");
                    item.created_date=itemdata.optString("created");

                    data_list.add(item);
                }

                Set_Adapter();

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

    }



    private void Call_Api_For_Singlevideos(final int postion) {

        try {
            JSONObject parameters = new JSONObject();

            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            parameters.put("token",Variables.sharedPreferences.getString(Variables.device_token,"Null"));
            parameters.put("video_id",data_list.get(postion).video_id);
            Log.i("paramsdsd", Variables.showAllVideos+parameters.toString());


            ApiRequest.Call_Api(context, Variables.showAllVideos, parameters, new Callback() {
                @Override
                public void Responce(String resp) {

                    Singal_Video_Parse_data(postion,resp);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){

        }
    }

    public void Singal_Video_Parse_data(int pos,String responce){
        Log.d("dfsfsfsfsfsf",responce);
        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject itemdata = msgArray.optJSONObject(i);
                    Home_Get_Set item=new Home_Get_Set();
                    item.fb_id=itemdata.optString("fb_id");

                    JSONObject user_info=itemdata.optJSONObject("user_info");

                    item.username=user_info.optString("username");
                    item.hopeuserid=itemdata.optString("fb_id");
                    item.first_name=user_info.optString("first_name",context.getResources().getString(R.string.app_name));
                    item.last_name=user_info.optString("last_name","User");
                    item.profile_pic=user_info.optString("profile_pic");
                    item.verified=user_info.optString("verified");

                    JSONObject sound_data=itemdata.optJSONObject("sound");
                    item.sound_id=sound_data.optString("id");
                    item.sound_name=sound_data.optString("sound_name");
                    item.sound_pic=sound_data.optString("thum");
                    if(sound_data!=null) {
                        JSONObject audio_path = sound_data.optJSONObject("audio_path");
                        item.sound_url_mp3 = audio_path.optString("mp3");
                        item.sound_url_acc = audio_path.optString("acc");
                    }


                    JSONObject count=itemdata.optJSONObject("count");
                    item.like_count=count.optString("like_count");
                    item.video_comment_count=count.optString("video_comment_count");

                    JSONObject arcount=itemdata.optJSONObject("liked");
                    if (arcount.isNull("likes")){
                        item.artilike = "0";
                    }else {
                        item.artilike=arcount.optString("likes");
                    }
                    item.video_id=itemdata.optString("id");
                    item.liked=itemdata.optString("is_liked");
                    item.privacy_type=itemdata.optString("privacy_type");
                    item.allow_comments=itemdata.optString("allow_comments");
                    item.allow_duet=itemdata.optString("allow_duet");

                    item.video_url=itemdata.optString("video");

                    item.video_description=itemdata.optString("description");
                    Log.d(Variables.tag,item.video_description);

                    item.thum=itemdata.optString("thum");
                    item.created_date=itemdata.optString("created");

                    data_list.remove(pos);
                    data_list.add(pos,item);
                    adapter.notifyDataSetChanged();
                }



            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

    }


    public void Set_Adapter(){
        recyclerView=findViewById(R.id.recylerview);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        SnapHelper snapHelper =  new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        adapter=new Watch_Videos_Adapter(context, data_list, new Watch_Videos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion,final Home_Get_Set item, View view) {

                switch(view.getId()){

                    case R.id.user_pic:
                        onPause();

                       // OpenProfile(item,false);
                  /*      Intent intentsd = new Intent(context, HomeActivity.class);
                        intentsd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentsd.putExtra(Constant.SHOW_FRAGMENT, Constant.OTHER_PROFILE);
                        intentsd.putExtra(Constant.ARGUMENT_ID, item.hopeuserid);
                        context.startActivity(intentsd);*/
                        break;

                    case R.id.like_layout:
                        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                            Like_Video(postion, item);
                        }else {
                            Toast.makeText(context, "Please Login.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.comment_layout:
                        OpenComment(item);
                        break;

                    case R.id.shared_layout:

                        final VideoAction_F fragment = new VideoAction_F(item.video_id, new Fragment_Callback() {
                            @Override
                            public void Responce(Bundle bundle) {

                                if (bundle.getString("action").equals("save")) {
                                    Save_Video(item);
                                }
                                else if(bundle.getString("action").equals("duet")){
                                    Duet_video(item);
                                }
                                if (bundle.getString("action").equals("delete")) {

                                    Functions.Show_loader(WatchVideos_F.this, false, false);
                                    Functions.Call_Api_For_Delete_Video(WatchVideos_F.this, item.video_id, new API_CallBack() {
                                        @Override
                                        public void ArrayData(ArrayList arrayList) {

                                        }

                                        @Override
                                        public void OnSuccess(String responce) {

                                            Functions.cancel_loader();
                                            finish();

                                        }

                                        @Override
                                        public void OnFail(String responce) {

                                        }
                                    });

                                }
                            }
                        });

                        Bundle bundle = new Bundle();
                        bundle.putString("video_id", item.video_id);
                        bundle.putString("user_id", item.fb_id);
                        bundle.putString("video_url", item.video_url);
                        bundle.putSerializable("data",item);
                        fragment.setArguments(bundle);

                        fragment.show(getSupportFragmentManager(), "");

                        break;


                    case R.id.sound_image_layout:

                        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                            if(check_permissions()) {
                                Intent intent = new Intent(WatchVideos_F.this, VideoSound_A.class);
                                intent.putExtra("data", item);
                                startActivity(intent);
                            }
                        }else {
                            Toast.makeText(context, "Please Login.", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }

            }
        });

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);


        // this is the scroll listener of recycler view which will tell the current item number
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no=scrollOffset / height;

                if(page_no!=currentPage ){
                    currentPage=page_no;

                    Privious_Player();
                    Set_Player(currentPage);
                }

            }
        });

        recyclerView.scrollToPosition(position);

    }


    @Override
    public void onResume() {
        super.onResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
    }



    int privious_height=0;
    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

        Log.d(Variables.tag,""+height);
        if(height<0) {
            privious_height= Math.abs(height);;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(write_layout.getWidth(), write_layout.getHeight());
        params.bottomMargin = height+privious_height;
        write_layout.setLayoutParams(params);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_btn:
                if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {

                    String comment_txt = message_edit.getText().toString();
                    if (!TextUtils.isEmpty(comment_txt)) {
                        Send_Comments(data_list.get(currentPage).fb_id,data_list.get(currentPage).video_id, comment_txt);
                    }



                }
                else {
                    Toast.makeText(context, "Please Login into app", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




    @Override
    public void onDataSent(String yourData) {
        int comment_count =Integer.parseInt(yourData);
        Home_Get_Set item=data_list.get(currentPage);
        item.video_comment_count=""+comment_count;
        data_list.add(currentPage,item);
        adapter.notifyDataSetChanged();
    }



    public void Set_Player(final int currentPage){

        final Home_Get_Set item= data_list.get(currentPage);

        Call_cache();


        Uri uri = Uri.parse(String.valueOf(item.video_url));
    /*    // Create a data source factory.
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
// Create a progressive media source pointing to a stream uri.
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
// Create a player instance.
        ExoPlayer player = new ExoPlayer.Builder(this).build();
// Set the media source to be played.
        player.setMediaSource(mediaSource);
// Prepare the player.
        player.prepare();*/
        HttpProxyCacheServer proxy = MyApplication.getProxy(context);
        String proxyUrl = proxy.getProxyUrl(item.video_url);
        LoadControl loadControl = new DefaultLoadControl.Builder()
                .setAllocator(new DefaultAllocator(true, 16))
                .setBufferDurationsMs(1*1024, 1*1024, 500, 1024)
                .setTargetBufferBytes(-1)
                .setPrioritizeTimeOverSizeThresholds(true)
                .createDefaultLoadControl();

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector,loadControl);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));


        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(proxyUrl));


        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);



        View layout=layoutManager.findViewByPosition(currentPage);
        PlayerView playerView=layout.findViewById(R.id.playerview);
        playerView.setPlayer(player);


        player.setPlayWhenReady(true);
        privious_player= (SimpleExoPlayer) player;



        final RelativeLayout mainlayout = layout.findViewById(R.id.mainlayout);
        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    float deltaXAbs = Math.abs(deltaX);
                    // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                    if((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                        if(deltaX > 0)
                        {
                            OpenProfile(item,true);
                        }
                    }


                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);
                    if(!player.getPlayWhenReady()){
                        privious_player.setPlayWhenReady(true);
                    }else{
                        privious_player.setPlayWhenReady(false);
                    }


                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    Show_video_option(item);

                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if(!player.getPlayWhenReady()){
                        privious_player.setPlayWhenReady(true);
                    }

                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {

                        Show_heart_on_DoubleTap(item, mainlayout, e);
                        Like_Video(currentPage, item);

                    }else {
                        Toast.makeText(context, "Please Login into ", Toast.LENGTH_SHORT).show();
                    }
                    return super.onDoubleTap(e);

                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });



        TextView desc_txt=layout.findViewById(R.id.desc_txt);
        HashTagHelper.Creator.create(context.getResources().getColor(R.color.maincolor), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {

                OpenHashtag(hashTag);

            }
        }).handle(desc_txt);



        LinearLayout soundimage = (LinearLayout)layout.findViewById(R.id.sound_image_layout);
        Animation aniRotate = AnimationUtils.loadAnimation(context,R.anim.d_clockwise_rotation);
        soundimage.startAnimation(aniRotate);

        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false))
            Functions.Call_Api_For_update_view(WatchVideos_F.this,item.video_id);


        if(item.allow_comments!=null && item.allow_comments.equalsIgnoreCase("false"))
        {
            write_layout.setVisibility(View.INVISIBLE);
        }
        else {
            write_layout.setVisibility(View.VISIBLE);
        }



        Call_Api_For_Singlevideos(currentPage);
    }


    SimpleExoPlayer cache_player;
    public void Call_cache(){
       /* if(currentPage+1<data_list.size()){

            if(cache_player!=null)
                cache_player.release();

            HttpProxyCacheServer proxy = MyApplication.getProxy(context);
            String proxyUrl = proxy.getProxyUrl((data_list.get(currentPage+1).video_url));

            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setAllocator(new DefaultAllocator(true, 16))
                    .setBufferDurationsMs(1*1024, 1*1024, 500, 1024)
                    .setTargetBufferBytes(-1)
                    .setPrioritizeTimeOverSizeThresholds(true)
                    .createDefaultLoadControl();

            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            cache_player = ExoPlayerFactory.newSimpleInstance(context, trackSelector,loadControl);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(proxyUrl));
            cache_player.prepare(videoSource);

        }

*/

    }

    // when we swipe for another video this will relaese the privious player
    SimpleExoPlayer privious_player;
    public void Privious_Player(){
        if(privious_player!=null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }




    public void Show_heart_on_DoubleTap(Home_Get_Set item,final RelativeLayout mainlayout,MotionEvent e){

        int x = (int) e.getX()-100;
        int y = (int) e.getY()-100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(getApplicationContext());
        lp.setMargins(x, y, 0, 0);
        iv.setLayoutParams(lp);
        if(item.liked.equals("1"))
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.heart_off));
        else
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.heart_on));

        mainlayout.addView(iv);
        Animation fadeoutani = AnimationUtils.loadAnimation(context,R.anim.fade_out);

        fadeoutani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainlayout.removeView(iv);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(fadeoutani);

    }



    // this function will call for like the video and Call an Api for like the video
    public void Like_Video(final int position, final Home_Get_Set home_get_set){

        String action=home_get_set.liked;

        if(action.equals("1")){
            action="0";
            home_get_set.like_count=""+(Integer.parseInt(home_get_set.like_count) -1);
        }else {
            action="1";
            home_get_set.like_count=""+(Integer.parseInt(home_get_set.like_count) +1);
        }


        data_list.remove(position);
        home_get_set.liked=action;
        data_list.add(position,home_get_set);
        adapter.notifyDataSetChanged();


        Functions.Call_Api_For_like_video(this, home_get_set.video_id,action ,new API_CallBack() {

            @Override
            public void ArrayData(ArrayList arrayList) {

            }

            @Override
            public void OnSuccess(String responce) {

            }

            @Override
            public void OnFail(String responce) {

            }
        });
    }





    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this,PERMISSIONS, 2);
        }else {

            return true;
        }

        return false;
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




    // this will open the comment screen
    public void OpenComment(Home_Get_Set item) {

        int comment_count=Integer.parseInt(item.video_comment_count);
        Fragment_Data_Send fragment_data_send=this;

        Comment_F comment_f = new Comment_F(comment_count,fragment_data_send);
        String backStateName = comment_f.getClass().getName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id",item.video_id);
        args.putString("user_id",item.fb_id);
        comment_f.setArguments(args);
        transaction.replace(R.id.WatchVideo_F, comment_f).commit();
        transaction.addToBackStack(backStateName);

    }



    // this will open the profile of user which have uploaded the currenlty running video
    private void OpenProfile(Home_Get_Set item,boolean from_right_to_left) {

    /*    Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constant.SHOW_FRAGMENT, Constant.OTHER_PROFILE);
        intent.putExtra(Constant.ARGUMENT_ID, item.hopeuserid);
        context.startActivity(intent);*/
       /* if(Variables.sharedPreferences.getString(Variables.u_id,"0").equals(item.fb_id)){

            TabLayout.Tab profile= MainMenuFragment.tabLayout.getTabAt(4);
            profile.select();

        }else {

            Profile_F profile_f = new Profile_F(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {

                    Call_Api_For_Singlevideos(currentPage);

                }
            });
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if(from_right_to_left)
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            else
                transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);

            Bundle args = new Bundle();
            args.putString("user_id", item.fb_id);
            args.putString("user_name",item.first_name+" "+item.last_name);
            args.putString("user_pic",item.profile_pic);
            profile_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.WatchVideo_F, profile_f).commit();

        }*/


    }



    public void Send_Comments(final String user_id, String video_id, final String comment){

        send_progress.setVisibility(View.VISIBLE);
        send_btn.setVisibility(View.GONE);

        Functions.Call_Api_For_Send_Comment(this, video_id,comment ,new API_CallBack() {
            @Override
            public void ArrayData(ArrayList arrayList) {

                message_edit.setText(null);
                send_progress.setVisibility(View.GONE);
                send_btn.setVisibility(View.VISIBLE);

                int comment_count=Integer.parseInt(data_list.get(currentPage).video_comment_count);
                comment_count++;
                onDataSent(""+comment_count);


            }

            @Override
            public void OnSuccess(String responce) {

            }

            @Override
            public void OnFail(String responce) {

            }
        });

        SendPushNotification(user_id,comment);
    }


    public void Duet_video(final Home_Get_Set item){

        Log.d(Variables.tag,item.video_url);
        if(item.video_url!=null){

            Functions.Show_determinent_loader(context,false,false);
            PRDownloader.initialize(getApplicationContext());
            DownloadRequest prDownloader= PRDownloader.download(item.video_url, Variables.app_showing_folder, item.video_id+".mp4")
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {

                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                            int prog=(int)((progress.currentBytes*100)/progress.totalBytes);
                            Functions.Show_loading_progress(prog);

                        }
                    });


            prDownloader.start(new OnDownloadListener() {
                @Override
                public void onDownloadComplete() {
                    Functions.cancel_determinent_loader();
                    Open_duet_Recording(item);
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    Functions.cancel_determinent_loader();

                }
            });

        }

    }

    public void Open_duet_Recording(Home_Get_Set item){
        Intent intent=new Intent(WatchVideos_F.this, Video_Recoder_Duet_A.class);
        intent.putExtra("data",item);
        startActivity(intent);
    }


    // this will open the profile of user which have uploaded the currenlty running video
    private void OpenHashtag(String tag) {

        Taged_Videos_F taged_videos_f = new Taged_Videos_F();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("tag", tag);
        taged_videos_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.WatchVideo_F, taged_videos_f).commit();

    }


    CharSequence[] options;
    private void Show_video_option(final Home_Get_Set home_get_set) {

        options = new CharSequence[]{ "Save Video","Cancel" };

        if(home_get_set.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id,"")))
            options = new CharSequence[]{"Save Video", "Delete Video", "Cancel"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context,R.style.AlertDialogCustom);

        builder.setTitle(null);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Save Video"))
                {
                    if(Functions.Checkstoragepermision(WatchVideos_F.this))
                        Save_Video(home_get_set);

                }

                else if(options[item].equals("Delete Video")){
                    if(Variables.is_secure_info){
                        Toast.makeText(context, getString(R.string.delete_function_not_available_in_demo), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Functions.Show_loader(WatchVideos_F.this, false, false);
                        Functions.Call_Api_For_Delete_Video(WatchVideos_F.this, home_get_set.video_id, new API_CallBack() {
                            @Override
                            public void ArrayData(ArrayList arrayList) {

                            }

                            @Override
                            public void OnSuccess(String responce) {

                                Functions.cancel_loader();
                                finish();

                            }

                            @Override
                            public void OnFail(String responce) {

                            }
                        });
                    }
                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }




    public void Save_Video(final Home_Get_Set item){

        JSONObject params=new JSONObject();
        try {
            params.put("video_id",item.video_id);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.Show_loader(context,false,false);
        ApiRequest.Call_Api(context, Variables.downloadFile, params, new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                Log.i("sdfdsfsdfsf",resp.toString());

                try {
                    JSONObject responce=new JSONObject(resp);
                    String code=responce.optString("code");
                    if(code.equals("200")){
                        JSONArray msg=responce.optJSONArray("msg");
                        JSONObject jsonObject=msg.optJSONObject(0);


                        Toast.makeText(WatchVideos_F.this, "Please wait your downloading video is in progress`", Toast.LENGTH_SHORT).show();
                        String download_url=jsonObject.getString("download_url");

                        beginDownload(download_url);
                    }

                } catch (JSONException | URISyntaxException e) {
                    e.printStackTrace();
                }


            }
        });






    }



    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //you have the permission now.
           /* DownloadManager.Request request = new DownloadManager.Request(Uri.parse(myurl));
            request.setTitle("Vertretungsplan");
            request.setDescription("wird heruntergeladen");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            String filename = URLUtil.guessFileName(myurl, null, MimeTypeMap.getFileExtensionFromUrl(myurl));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
            DownloadManager manager = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);*/
        }
    }
    public void Scan_file(Home_Get_Set item){
        MediaScannerConnection.scanFile(WatchVideos_F.this,
                new String[] { Variables.app_showing_folder +item.video_id+".mp4" },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {

                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }


    public  void SendPushNotification( String user_id, String comment){

        JSONObject notimap= new JSONObject();
        try {
            notimap.put("title",Variables.sharedPreferences.getString(Variables.u_name,"")+" Comment on your video");
            notimap.put("message",comment);
            notimap.put("icon",Variables.sharedPreferences.getString(Variables.u_pic,""));
            notimap.put("senderid",Variables.sharedPreferences.getString(Variables.u_id,""));
            notimap.put("receiverid", user_id);
            notimap.put("action_type","comment");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context,Variables.sendPushNotification,notimap,null);

    }


    // this is lifecyle of the Activity which is importent for play,pause video or relaese the player
    @Override
    public void onPause() {
        super.onPause();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
        keyboardHeightProvider.setKeyboardHeightObserver(null);
    }


    @Override
    public void onStop() {
        super.onStop();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    private long downloadID;

    // using broadcast method
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(WatchVideos_F.this, "Download Completed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            }
        }
    };


    // Bottom all the function and the Call back listener of the Expo player
   /* @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }*/

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if(playbackState==Player.STATE_BUFFERING){
            p_bar.setVisibility(View.VISIBLE);
        }
        else if(playbackState==Player.STATE_READY){
            p_bar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

/*    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }*/

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }


    @Override
    public void onSeekProcessed() {


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
     private void beginDownload(String download_url) throws URISyntaxException {

        String fileName = download_url.substring(download_url.lastIndexOf('/') + 1);

        Log.i("dfdffftfd",fileName);
        //                // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(download_url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

// set title and description
        request.setTitle("Geee Vido Download");
        request.setDescription("Open in media player");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
        request.setMimeType("*/*");
        downloadID = downloadManager.enqueue(request);


        // using query method
        boolean finishDownload = false;
        int progress;
        while (!finishDownload) {
            Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadID));
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_FAILED: {
                        finishDownload = true;
                        break;
                    }
                    case DownloadManager.STATUS_PAUSED:
                        break;
                    case DownloadManager.STATUS_PENDING:
                        break;
                    case DownloadManager.STATUS_RUNNING: {
                        @SuppressLint("Range") final long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (total >= 0) {
                            @SuppressLint("Range") final long downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            progress = (int) ((downloaded * 100L) / total);
                            // if you use downloadmanger in async task, here you can use like this to display progress.
                            // Don't forget to do the division in long to get more digits rather than double.
                            //  publishProgress((int) ((downloaded * 100L) / total));
                        }
                        break;
                    }
                    case DownloadManager.STATUS_SUCCESSFUL: {
                        progress = 100;
                        // if you use aysnc task
                        // publishProgress(100);
                        finishDownload = true;
                        Toast.makeText(WatchVideos_F.this, "Download Completed", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }
}
