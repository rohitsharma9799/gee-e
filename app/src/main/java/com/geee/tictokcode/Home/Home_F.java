package com.geee.tictokcode.Home;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.geee.Inner_VP_Package.Home_Package.Home;
import com.geee.Location.Utils;
import com.geee.Main_VP_Package.Main;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.Utils.MyApplication;
import com.geee.tictokcode.Taged.Taged_Videos_F;
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
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.geee.R;
import com.geee.tictokcode.Comments.Comment_F;
import com.geee.tictokcode.Main_Menu.MainMenuActivity;
import com.geee.tictokcode.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.geee.tictokcode.Services.Upload_Service;
import com.geee.tictokcode.SimpleClasses.API_CallBack;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.SimpleClasses.Fragment_Callback;
import com.geee.tictokcode.SimpleClasses.Fragment_Data_Send;
import com.geee.tictokcode.SimpleClasses.Functions;
import com.geee.tictokcode.SimpleClasses.Variables;
import com.geee.tictokcode.SoundLists.VideoSound_A;
import com.geee.tictokcode.VideoAction.VideoAction_F;
import com.geee.tictokcode.Video_Recording.Video_Recoder_A;
import com.geee.tictokcode.Video_Recording.Video_Recoder_Duet_A;

/**
 * A simple {@link Fragment} subclass.
 */

// this is the main view which is show all  the video in list
public class Home_F extends RootFragment implements Player.EventListener, Fragment_Data_Send, View.OnClickListener {
    SimpleExoPlayer player;
    View view;
    Context context;
    int currentvolume;
    RecyclerView recyclerView;
    ArrayList<Home_Get_Set> data_list;
    int currentPage=-1;
    LinearLayoutManager layoutManager;
    ProgressBar p_bar;
    SwipeRefreshLayout swiperefresh;
    boolean is_user_stop_video=false;

    TextView following_btn,related_btn;
    String type="related";

    public Home_F() {
        // Required empty public constructor
    }

    int swipe_count=0;

    RelativeLayout upload_video_layout;
    ImageView uploading_thumb;
    ImageView uploading_icon;
    UploadingVideoBroadCast mReceiver;
    private class UploadingVideoBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Upload_Service mService = new Upload_Service();
//            if (Functions.isMyServiceRunning(context,mService.getClass())) {
//                upload_video_layout.setVisibility(View.VISIBLE);
//                Bitmap bitmap=Functions.Base64_to_bitmap(Variables.sharedPreferences.getString(Variables.uploading_video_thumb,""));
//                if(bitmap!=null)
//                uploading_thumb.setImageBitmap(bitmap);
//
//            }
//            else {
//                upload_video_layout.setVisibility(View.GONE);
//            }

        }
    }


    Boolean muteof = false;

    ImageView cxti;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        context=getActivity();

        p_bar=view.findViewById(R.id.p_bar);

        following_btn=view.findViewById(R.id.following_btn);
        related_btn=view.findViewById(R.id.related_btn);
       /* cxti=view.findViewById(R.id.cxti);
        cxti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Video_Recoder_A.class);
                startActivity(intent);

            }
        });*/
        following_btn.setOnClickListener(this);
        related_btn.setOnClickListener(this);


        recyclerView=view.findViewById(R.id.recylerview);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

         SnapHelper snapHelper =  new PagerSnapHelper();
         snapHelper.attachToRecyclerView(recyclerView);



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

                    Release_Privious_Player();
                    Set_Player(currentPage);

                    muteof = true;
                }
            }
        });



        swiperefresh=view.findViewById(R.id.swiperefresh);
        swiperefresh.setProgressViewOffset(false, 0, 200);

        swiperefresh.setColorSchemeResources(R.color.black);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call_Api_For_get_Allvideos();
            }
        });

        Call_Api_For_get_Allvideos();

        if(!Variables.is_remove_ads)
        Load_add();

        upload_video_layout=view.findViewById(R.id.upload_video_layout);
        uploading_thumb=view.findViewById(R.id.uploading_thumb);
        uploading_icon=view.findViewById(R.id.uploading_icon);

        mReceiver = new UploadingVideoBroadCast();
        getActivity().registerReceiver(mReceiver, new IntentFilter("uploadVideo"));

        Upload_Service mService = new Upload_Service();
//        if (Functions.isMyServiceRunning(context,mService.getClass())) {
//            upload_video_layout.setVisibility(View.VISIBLE);
//            Bitmap bitmap=Functions.Base64_to_bitmap(Variables.sharedPreferences.getString(Variables.uploading_video_thumb,""));
//            if(bitmap!=null)
//            uploading_thumb.setImageBitmap(bitmap);
//        }


        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.following_btn:

                if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                    type = "following";
                    swiperefresh.setRefreshing(true);
                    related_btn.setTextColor(context.getResources().getColor(R.color.graycolor2));
                    following_btn.setTextColor(context.getResources().getColor(R.color.white));
                    Call_Api_For_get_Allvideos();
                }
                else {
                    Open_Login();
                }
                break;

            case R.id.related_btn:
                type="related";
                swiperefresh.setRefreshing(true);
                related_btn.setTextColor(context.getResources().getColor(R.color.white));
                following_btn.setTextColor(context.getResources().getColor(R.color.graycolor2));
                Call_Api_For_get_Allvideos();
                break;
        }

    }

    public void Load_add() {



    }


    boolean is_add_show=false;
    Home_Adapter adapter;
    public void Set_Adapter(){

         adapter=new Home_Adapter(context, data_list, new Home_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, final Home_Get_Set item, View view) {
                switch(view.getId()) {

                    case R.id.user_pic:
                        onPause();
                        OpenProfile(item,false);
                        break;

                    case R.id.username:
                        onPause();
                        OpenProfile(item,false);
                        break;

                    case R.id.like_layout:
                        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                        Like_Video(postion, item,item.token);

                        }else {
                            Toast.makeText(context, "Please Login.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.comment_layout:
                        OpenComment(item);
                        //sendFCMPush();
                        break;

                    case R.id.shared_layout:
                        is_add_show = false;
                        final VideoAction_F fragment = new VideoAction_F(item.video_id, new Fragment_Callback() {
                            @Override
                            public void Responce(Bundle bundle) {

                                if (bundle.getString("action").equals("save")) {
                                    Save_Video(item);
                                }
                                else if(bundle.getString("action").equals("duet")){

                                    Duet_video(item);
                                }
                                else if (bundle.getString("action").equals("delete")) {
                                    Functions.Show_loader(context, false, false);
                                    Functions.Call_Api_For_Delete_Video(getActivity(), item.video_id, new API_CallBack() {
                                        @Override
                                        public void ArrayData(ArrayList arrayList) {

                                        }

                                        @Override
                                        public void OnSuccess(String responce) {
                                            data_list.remove(currentPage);
                                            adapter.notifyDataSetChanged();

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
                        bundle.putSerializable("data",item);
                        fragment.setArguments(bundle);
                        fragment.show(getChildFragmentManager(), "");

                        break;


                    case R.id.sound_image_layout:
                        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                            if(check_permissions()) {
                                Intent intent = new Intent(getActivity(), VideoSound_A.class);
                                intent.putExtra("data", item);
                                startActivity(intent);
                            }
                        }else {
                            Toast.makeText(context, "Please Login.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                   /* case R.id.mute:

                        float currentVolume = player.getVolume();
                        if (currentVolume == 0f) {
                            player.setVolume(1f);
                        } else {
                            player.setVolume(0f);
                        }
                        break;*/
                }

            }
        });

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

    }



    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_Allvideos() {


       // Log.d(Variables.tag, MainMenuActivity.token);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            parameters.put("token",MainMenuActivity.token);
            parameters.put("type",type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.showAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                swiperefresh.setRefreshing(false);
                Parse_data(resp);
            }
        });



    }

    public void Parse_data(String responce){

        Log.i("resdpsd",responce);

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");

                ArrayList<Home_Get_Set> temp_list=new ArrayList();
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject itemdata = msgArray.optJSONObject(i);
                    Home_Get_Set item=new Home_Get_Set();
                    item.fb_id=itemdata.optString("fb_id");

                    JSONObject user_info=itemdata.optJSONObject("user_info");

                    item.username=user_info.optString("username");
                    item.first_name=user_info.optString("first_name",context.getResources().getString(R.string.app_name));
                    item.last_name=user_info.optString("last_name","User");
                    item.profile_pic=user_info.optString("profile_pic","null");
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
                        item.artilike= "0";
                    }else {
                        item.artilike=arcount.optString("likes");
                    }

                    item.privacy_type=itemdata.optString("privacy_type");
                    item.allow_comments=itemdata.optString("allow_comments");
                    item.allow_duet=itemdata.optString("allow_duet");
                    item.video_id=itemdata.optString("id");
                    item.liked=itemdata.optString("is_liked");
                    item.video_url=itemdata.optString("video");
                    itemdata.optString("video");


                    item.video_description=itemdata.optString("description");

                    item.thum=itemdata.optString("thum");
                    item.created_date=itemdata.optString("created");

                    if(Variables.is_demo_app) {
                        if(i<Variables.demo_app_videos_count)
                        temp_list.add(item);
                    }else {
                        temp_list.add(item);
                    }

                }

                if(!temp_list.isEmpty()) {
                    currentPage=-1;
                    data_list=new ArrayList<>();
                    data_list.addAll(temp_list);
                    Set_Adapter();
                }

                else if(type.equalsIgnoreCase("related")) {
                    type = "following";
                    related_btn.setTextColor(context.getResources().getColor(R.color.graycolor2));
                    following_btn.setTextColor(context.getResources().getColor(R.color.white));
                }

                else if(type.equalsIgnoreCase("following")){
                    Toast.makeText(context, "Follow an account to see there videos here.", Toast.LENGTH_SHORT).show();
                    type="related";
                    related_btn.setTextColor(context.getResources().getColor(R.color.white));
                    following_btn.setTextColor(context.getResources().getColor(R.color.graycolor2));
                }

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void Call_Api_For_Singlevideos(final int postion) {


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            parameters.put("token",Variables.sharedPreferences.getString(Variables.device_token,"Null"));
            parameters.put("video_id",data_list.get(postion).video_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("allapi", Variables.showAllVideos+parameters.toString());

        ApiRequest.Call_Api(context, Variables.showAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                swiperefresh.setRefreshing(false);
                Singal_Video_Parse_data(postion,resp);
            }
        });


    }

    public void Singal_Video_Parse_data(int pos, String responce){

        Log.i("allapi2df",responce);
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
                    item.profile_pic=user_info.optString("profile_pic","null");
                    item.verified=user_info.optString("verified");
                    item.hopeuserid=user_info.optString("hopeuserid");
                    item.token=user_info.optString("token");
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

                    item.privacy_type=itemdata.optString("privacy_type");
                    item.allow_comments=itemdata.optString("allow_comments");
                    item.allow_duet=itemdata.optString("allow_duet");
                    item.video_id=itemdata.optString("id");
                    item.liked=itemdata.optString("is_liked");
                    item.video_url=itemdata.optString("video");
                    item.video_description=itemdata.optString("description");

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





    // this will call when swipe for another video and
    // this function will set the player to the current video
    public void Set_Player(final int currentPage){



        final Home_Get_Set item= data_list.get(currentPage);

        Call_cache();

        Uri uri = Uri.parse(item.video_url);
        // Create a data source factory.
/*        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
// Create a progressive media source pointing to a stream uri.
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
// Create a player instance.
        ExoPlayer player = new ExoPlayer.Builder(context).build();
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
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector,loadControl);

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

        //player.setVolume(0f);
        //currentvolume = (int) player.getVolume();
        if (muteof==true){
            player.setPlayWhenReady(true);
        }
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
                        is_user_stop_video=false;
                        privious_player.setPlayWhenReady(true);
                    }else{
                        is_user_stop_video=true;
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
                        is_user_stop_video=false;
                        privious_player.setPlayWhenReady(true);
                    }


                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                        Show_heart_on_DoubleTap(item, mainlayout, e);
                        Like_Video(currentPage, item, "0");
                    }else {
                        Toast.makeText(context, "Please Login into app", Toast.LENGTH_SHORT).show();
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

                onPause();
                OpenHashtag(hashTag);

            }
        }).handle(desc_txt);



        LinearLayout soundimage = (LinearLayout)layout.findViewById(R.id.sound_image_layout);
        Animation sound_animation = AnimationUtils.loadAnimation(context,R.anim.d_clockwise_rotation);
        soundimage.startAnimation(sound_animation);

        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false))
        Functions.Call_Api_For_update_view(getActivity(),item.video_id);


        swipe_count++;
        if(swipe_count>6){
            Show_add();
            swipe_count=0;
        }


        if(Variables.is_demo_app && currentPage==data_list.size()-1){
            Toast.makeText(context, "Only allow "+data_list.size()+" Videos in demo app", Toast.LENGTH_LONG).show();
        }


        Call_Api_For_Singlevideos(currentPage);

    }

    SimpleExoPlayer cache_player;
    /*public void Call_cache(){
        if(currentPage+1<data_list.size()){

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


    }*/
    public void Call_cache(){
        if(currentPage+1<data_list.size()){

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
           /* cache_player = ExoPlayerFactory.newSimpleInstance(context, trackSelector,loadControl);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(proxyUrl));
            cache_player.prepare(videoSource);*/

        }



    }


    public void Show_heart_on_DoubleTap(Home_Get_Set item, final RelativeLayout mainlayout, MotionEvent e){

        int x = (int) e.getX()-100;
        int y = (int) e.getY()-100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(getActivity());
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



    public void Show_add(){

    }


    @Override
    public void onDataSent(String yourData) {
        int comment_count = Integer.parseInt(yourData);
        Home_Get_Set item=data_list.get(currentPage);
        item.video_comment_count=""+comment_count;
        data_list.remove(currentPage);
        data_list.add(currentPage,item);
        adapter.notifyDataSetChanged();
    }



    // this will call when go to the home tab From other tab.
    // this is very importent when for video play and pause when the focus is changes
    boolean is_visible_to_user;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user=isVisibleToUser;

        if(privious_player!=null && (isVisibleToUser && !is_user_stop_video)){
            privious_player.setPlayWhenReady(true);
        }
        else if(privious_player!=null && !isVisibleToUser){
            privious_player.setPlayWhenReady(false);
        }

    }



   // when we swipe for another video this will relaese the privious player
    SimpleExoPlayer privious_player;
    public void Release_Privious_Player(){
        if(privious_player!=null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }




    // this function will call for like the video and Call an Api for like the video
    public void Like_Video(final int position, final Home_Get_Set home_get_set, String token){
        String action=home_get_set.liked;


        if(action.equals("1")){
            action="0";
            home_get_set.like_count=""+(Integer.parseInt(home_get_set.like_count) -1);
        }else {
            action="1";
            home_get_set.like_count=""+(Integer.parseInt(home_get_set.like_count) +1);
           // sendFCMPush(home_get_set.token);

        }

       // Log.i("dfgdfgdg",token);

        data_list.remove(position);
        home_get_set.liked=action;
        data_list.add(position,home_get_set);
        adapter.notifyDataSetChanged();
       // Log.i("Fgdgdfgdf",home_get_set.token);
        Functions.Call_Api_For_like_video(getActivity(), home_get_set.video_id, action,new API_CallBack() {

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



    // this will open the comment screen
    private void OpenComment(Home_Get_Set item) {

        int comment_counnt= Integer.parseInt(item.video_comment_count);
        Fragment_Data_Send fragment_data_send=this;
        Comment_F comment_f = new Comment_F(comment_counnt,fragment_data_send);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id",item.video_id);
        args.putString("user_id",item.fb_id);
        args.putString("token",item.token);
        comment_f.setArguments(args);
        transaction.addToBackStack("commentfrag");
        transaction.add(R.id.mainrsd, comment_f).commit();

    }


    public void Open_Login(){
       /* Intent intent = new Intent(getActivity(), Login_A.class);
        startActivity(intent);*/
       // getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
    }


    // this will open the profile of user which have uploaded the currenlty running video
    private void OpenProfile(Home_Get_Set item,boolean from_right_to_left) {
        /*if(Variables.sharedPreferences.getString(Variables.u_id,"0").equals(item.fb_id)){

            TabLayout.Tab profile= MainMenuFragment.tabLayout.getTabAt(4);
            profile.select();

        }else {
            Profile_F profile_f = new Profile_F(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {

                    Call_Api_For_Singlevideos(currentPage);
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
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
            transaction.replace(R.id.MainMenuFragment, profile_f).commit();
        }*/

    }


    // this will open the profile of user which have uploaded the currenlty running video
    private void OpenHashtag(String tag) {

            Taged_Videos_F taged_videos_f = new Taged_Videos_F();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
            Bundle args = new Bundle();
            args.putString("tag", tag);
            taged_videos_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.mainrsd, taged_videos_f).commit();


    }



    private void Show_video_option(final Home_Get_Set home_get_set) {

        final CharSequence[] options = { "Save Video","Cancel" };
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setTitle(null);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Save Video")) {
                    if(Functions.Checkstoragepermision(getActivity()))
                    Save_Video(home_get_set);

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

            Log.i("dfsfsdfsdfs",params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.Show_loader(context,false,false);
        ApiRequest.Call_Api(context, Variables.downloadFile, params, new Callback() {
            @Override
            public void Responce(String resp) {
                Log.i("sdfdsfsdfsf",resp.toString());
                Functions.cancel_loader();
                try {
                    JSONObject responce=new JSONObject(resp);
                    String code=responce.optString("code");
                    if(code.equals("200")){
                        JSONArray msg=responce.optJSONArray("msg");
                        JSONObject jsonObject=msg.optJSONObject(0);
                        String download_url=jsonObject.getString("download_url");

                        if(download_url!=null){
                            String dirPath =  Environment.DIRECTORY_DOWNLOADS;

                            Functions.Show_determinent_loader(context,false,false);
                            PRDownloader.initialize(getActivity().getApplicationContext());
                            DownloadRequest prDownloader= PRDownloader.download(download_url, dirPath, item.video_id+".mp4")
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
                                    Scan_file(item,dirPath);
                                }

                                @Override
                                public void onError(Error error) {

                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    Functions.cancel_determinent_loader();
                                }


                            });

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });






    }



    public void Scan_file(Home_Get_Set item, String dirPath){

        MediaScannerConnection.scanFile(getActivity(),
                new String[] { dirPath +item.video_id+".mp4" },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {

                        Toast.makeText(context, "Video Downloaded"+path, Toast.LENGTH_SHORT).show();
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);

                        String type = "video/*";

                        // Create the new Intent using the 'Send' action.
                        Intent share = new Intent(Intent.ACTION_SEND);

                        // Set the MIME type
                        share.setType(type);


                        final File filesDir = getActivity().getFilesDir();

                        String yy = filesDir + item.video_id +".mp4";
                        Log.i("dfdf",yy.toString());

                        // Create the URI from the media
                     /*   Uri urid = FileProvider.getUriForFile(getActivity(), getString(R.string.authority), file);
                        share.setPackage("com.whatsapp");
                        // Add the URI to the Intent.
                        share.putExtra(Intent.EXTRA_STREAM, urid);
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        PackageManager packageManager = getActivity().getPackageManager();
                        if (share.resolveActivity(packageManager) != null) {
                            startActivity(share);
                            startActivity(Intent.createChooser(share, "Share to"));

                        } else {
                          //  alertForApp(getString(R.string.install_whatsapp), "com.whatsapp");
                        }
*/
                    }
                });
    }


    public void Duet_video(final Home_Get_Set item){

        Log.d(Variables.tag,item.video_url);
        if(item.video_url!=null){

            Functions.Show_determinent_loader(context,false,false);
            PRDownloader.initialize(getActivity().getApplicationContext());
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

        Intent intent=new Intent(getActivity(), Video_Recoder_Duet_A.class);
        intent.putExtra("data",item);
        startActivity(intent);

    }


    public boolean is_fragment_exits(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if(fm.getBackStackEntryCount()==0){
            return false;
        }else {
            return true;
        }

    }



    // this is lifecyle of the Activity which is importent for play,pause video or relaese the player
    @Override
    public void onResume() {
        super.onResume();
        if((privious_player!=null && (is_visible_to_user && !is_user_stop_video)) && !is_fragment_exits() ){
            privious_player.setPlayWhenReady(true);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(privious_player!=null){
            privious_player.release();
        }

        if(mReceiver!=null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

      //  Toast.makeText(getActivity(), "dfdf", Toast.LENGTH_SHORT).show();
    }

    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, 2);
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



    // Bottom all the function and the Call back listener of the Expo player

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }


    @Override
    public void onLoadingChanged(boolean isLoading) {

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if(playbackState== Player.STATE_BUFFERING){
            p_bar.setVisibility(View.VISIBLE);
        }
        else if(playbackState== Player.STATE_READY){
             p_bar.setVisibility(View.GONE);
        }


    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }


    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }


    @Override
    public void onSeekProcessed() {

    }

 /*   private void sendFCMPush(String token) {
       ModelUserInfo userInfo = SessionManager.getModelUserInfo(getActivity());

            JSONObject obj = null;
            JSONObject objData = null;
            JSONObject dataobjData = null;
            // TO get device token of other user id


            try {
                obj = new JSONObject();
                objData = new JSONObject();

                objData.put("body", "Hope");
                objData.put("title", userInfo.getFirstName()+"like your post");
                objData.put("sound", "default");
                objData.put("icon", "http://hopeapp.in/back/storage/app/public/profileimage/splashme.png"); //   icon_name image must be there in drawable
                objData.put("tag", token);
                objData.put("priority", "high");
                dataobjData = new JSONObject();
                dataobjData.put("text", "Hope");
                dataobjData.put("title", userInfo.getFirstName()+" like your post");
                dataobjData.put("icon", "http://hopeapp.in/back/storage/app/public/profileimage/splashme.png"); //   icon_name image must be there in drawable

                obj.put("to", token);
                obj.put("priority", "high");
                obj.put("notification", objData);
                obj.put("data", dataobjData);
                Log.e("!_@rj@_@@_PASS:>", obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("!_@@_SUCESS", response + "");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("!_@@_Errors--", error + "");
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "key=" + Variables.Legacy_SERVER_KEY);
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            int socketTimeout = 1000 * 60;// 60 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);
        }*/

}
