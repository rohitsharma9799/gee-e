package com.geee.Inner_VP_Package.Home_Package;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.DOWNLOAD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.util.Util;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Adapter.Home_TictokAdapter;
import com.geee.Inner_VP_Package.Adapter.SubCategoryAdapter;
import com.geee.Inner_VP_Package.Home_Package.Adapters.HomePostAdapter;
import com.geee.Inner_VP_Package.Home_Package.Adapters.PostDetailAdapter;
import com.geee.Inner_VP_Package.Home_Package.Adapters.StoryAdapter;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Data_Model_Home;
import com.geee.Inner_VP_Package.Home_Package.Share_Bottom_Sheet.ShareBottomSheet;
import com.geee.Inner_VP_Package.Home_Package.Show_Post_Likes.PostLikesA;
import com.geee.Inner_VP_Package.Main_F;
import com.geee.Inner_VP_Package.OptionsBottomSheet;
import com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package.DeleteFragment;
import com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package.ShowStoryDM;
import com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package.ViewStory;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.Location.LocationActivity;
import com.geee.Location.Utils;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.Menu.Category;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Utils.MyApplication;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.geee.tictokcode.Comments.Comment_F;
import com.geee.tictokcode.Home.Home_F;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.geee.tictokcode.Main_Menu.MainMenuActivity;
import com.geee.tictokcode.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.geee.tictokcode.Profile.MyVideos_Adapter;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.SimpleClasses.Fragment_Data_Send;
import com.geee.tictokcode.Video_Recording.GallerySelectedVideo.GallerySelectedVideo_A;
import com.geee.tictokcode.WatchVideos.WatchVideos_F;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
/*import com.gowtham.library.ui.ActVideoTrimmer;
import com.gowtham.library.utils.TrimmerConstants;*/
import com.theartofdev.edmodo.cropper.CropImage;
import com.videotrimmer.library.utils.CompressOption;
import com.videotrimmer.library.utils.TrimType;
import com.videotrimmer.library.utils.TrimVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public  class GeeeFeed extends RootFragment implements View.OnClickListener, Fragment_Data_Send  {
        private static GeeeFeed instance = null;
        private RecyclerView recyclerviewplans1;
        SharedPreferences sharedpreferences;
        ProgressDialog progress;
//        private List<Offer> imageData2=new ArrayList<>();
    private static final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 508;
    View view;
    ImageView chatIv;
    RecyclerView psotRecycleview, storiesRecyclerview;
    HomePostAdapter adapter;
    List<Data_Model_Home> homeArrayList = new ArrayList<>();
    List<ShowStoryDM> storyDmList = new ArrayList<>();
    String token;
    Data_Model_Home dataModelHome;
    boolean isVisible = false;
    RecyclerRefreshLayout recyclerRefreshLayout;
    StoryAdapter storyAdapter;
    private StorageReference mStorageRef;
    private String imageFilePath;
    TextView related_btn;
    LinearLayout empty_view_container;
    ImageView location;
    private TextSwitcher textSwitcher;
    private int stringIndex = 0;
    private String[] row = {"Hiii my name is G2", "your personal Geee assistant", "May i help you", "to find your BFF", "Tap to start now!"};
    private TextView textView;

    public RecyclerView recyclerView;
    ArrayList<Home_Get_Set> data_list;
    Home_TictokAdapter home_tictokAdapter;
    Context context;

    private Button btn_download;
    private long downloadID;

    // using broadcast method
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
                getActivity().startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            }
        }
    };

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_geefeed, container, false);

            context=getActivity();
        recyclerView=view.findViewById(R.id.recylerview);

        Button btnisd=view.findViewById(R.id.btnisd);
        context.registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        btnisd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DownloadManager manager;


//
//                // Create request for android download manager
//                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
//                        DownloadManager.Request.NETWORK_MOBILE);
//
//// set title and description
//                request.setTitle("Rohit baba Download");
//                request.setDescription("Android Data download using DownloadManager.");
//
//                request.allowScanningByMediaScanner();
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
////set the local destination for download file to a path within the application's external files directory
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"downloadfileName");
//                request.setMimeType("*/*");
//                downloadManager.enqueue(request);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(false);
        data_list=new ArrayList<>();
        //methodSetUpHometictokadapter();
        home_tictokAdapter=new Home_TictokAdapter(context, data_list, new Home_TictokAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion,Home_Get_Set item, View view) {


                switch (view.getId()) {
                    case R.id.comment_id:
                        OpenComment(item);

                        //Toast.makeText(getActivity(), "commetn", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.wholecard:
                        OpenWatchVideo(postion);
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(home_tictokAdapter);
        Call_Api_For_get_Allvideos();


        Variables.userId = SharedPrefrence.getUserIdFromJson(getContext());
            Variables.userName = SharedPrefrence.getUserNameFromJson(getContext());
            Variables.userPic = SharedPrefrence.getUserImgFromJson(getContext());

            mStorageRef = FirebaseStorage.getInstance().getReference();

//            chatIv = view.findViewById(R.id.chat_id);
           // location = view.findViewById(R.id.location);
            psotRecycleview = view.findViewById(R.id.homePostsRecycler);
            recyclerRefreshLayout = view.findViewById(R.id.refresh_layout);
            storiesRecyclerview = view.findViewById(R.id.stories_recyclerview);
            related_btn = view.findViewById(R.id.related_btn);
            empty_view_container = view.findViewById(R.id.empty_view_container);
            textSwitcher = view.findViewById(R.id.textSwitcher);
            recyclerRefreshLayout.setOnRefreshListener(() -> getHomePosts());
            final Handler handler = new Handler();
            final int delay = 4500; // 1000 milliseconds == 1 second
            handler.postDelayed(new Runnable() {
                public void run() {
                   // System.out.println("myHandler: here!"); // Do your work here
                    if (stringIndex == row.length - 1) {
                        stringIndex = 0;
                        textSwitcher.setText(row[stringIndex]);
                    } else {
                        textSwitcher.setText(row[++stringIndex]);
                    }

                    handler.postDelayed(this, delay);
                }
            }, delay);
            textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    textView = new TextView(getActivity());
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(14);
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    return textView;
                }
            });
            textSwitcher.setText(row[stringIndex]);
           /* final View parent = (View) chatIv.getParent();  // button: the view you want to enlarge hit area
            parent.post((Runnable) () -> {
                final Rect rect = new Rect();
                chatIv.getHitRect(rect);
                rect.top -= 100;    // increase top hit area
                rect.left -= 100;   // increase left hit area
                rect.bottom += 100; // increase bottom hit area
                rect.right += 100;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, chatIv));
            });

            chatIv.setOnClickListener(v -> ((MainActivity) getActivity()).openInbox());
*/
            related_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Home home = new Home();
                    home.viewPager.setCurrentItem(1);
                }
            });
            setUpHomeAdapter();
            setUpStoriesAdapter();
//
//            location.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent myIntent = new Intent(getActivity(), LocationActivity.class);
//                    getActivity().startActivity(myIntent);
//                }
//            });

            empty_view_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            return view;
        }

    private void OpenComment(Home_Get_Set item) {
        Main_F.bottombarid.setVisibility(View.GONE);
        int comment_count=Integer.parseInt(item.video_comment_count);
        Fragment_Data_Send fragment_data_send=this;

        Comment_F comment_f = new Comment_F(comment_count,fragment_data_send);
        String backStateName = comment_f.getClass().getName();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id",item.video_id);
        args.putString("user_id",item.fb_id);
        comment_f.setArguments(args);
        transaction.replace(R.id.abhit, comment_f).commit();
        transaction.addToBackStack(backStateName);
    }


    public void setUpStoriesAdapter() {
        storiesRecyclerview = view.findViewById(R.id.stories_recyclerview);

        storyAdapter = new StoryAdapter(getContext(), storyDmList, new StoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view) {
                ShowStoryDM storyDm = dmArrayList.get(postion);
                switch (view.getId()) {
                    case R.id.rv_id:
                        if (storyDm.getType().equals("null")) {
                            selectImage();
                        } else {
                            Intent myIntent = new Intent(getActivity(), ViewStory.class);
                            myIntent.putExtra("user_id", storyDm.getUserId()); //Optional parameters
                            myIntent.putExtra("storyList", (Serializable) storyDmList); //Optional parameters
                            myIntent.putExtra("position", String.valueOf(postion)); //Optional parameters
                            getActivity().startActivity(myIntent);
                        }

                        break;
                    default:
                        break;
                }
            }
        }, new StoryAdapter.onItemLongClickListener() {
            @Override
            public void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view) {
                ShowStoryDM storyDm = dmArrayList.get(postion);
                switch (view.getId()) {
                    case R.id.rv_id:
                        if (storyDm.getUserId().equals(SharedPrefrence.getUserIdFromJson(getApplicationContext()))) {
                            selectImage();
                        }
                        break;

                    default:
                        break;
                }
            }
        });


        storiesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        storiesRecyclerview.setHasFixedSize(false);
        storiesRecyclerview.setAdapter(storyAdapter);
     //   getStories();


    }


    /*private void methodSetUpHometictokadapter() {
        home_tictokAdapter = new PostDetailAdapter(getContext(), data_list, (postion, model, view) -> {
            Data_Model_Home dataModelHome = (Data_Model_Home) model;

            switch (view.getId()) {
                case R.id.comment_id:
                    //methodOpenCommentScreen(dataModelHome);

                    Toast.makeText(getActivity(), "commetn", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        });
        recyclerView.setAdapter(home_tictokAdapter);

        home_tictokAdapter.notifyDataSetChanged();
    }*/


    public void setUpHomeAdapter() {
        adapter = new HomePostAdapter(getActivity(), homeArrayList, (postion, model, view) -> {
            Data_Model_Home dataModelHome = (Data_Model_Home) model;
            switch (view.getId()) {
                case R.id.comment_id:
                    Intent myIntent = new Intent(getActivity(), Comment_Chat.class);
                    myIntent.putExtra("post_id", dataModelHome.getId());
                    myIntent.putExtra("device_token", dataModelHome.getdevice_token());
                    myIntent.putExtra("attachment", Constants.BASE_URL +dataModelHome.getAttachment());
                    Log.i("ffffffff",dataModelHome.getAttachment().toString());
                    startActivity(myIntent);
                    break;

                case R.id.view_comment_id:

                    Intent intent = new Intent(getActivity(), Comment_Chat.class);
                    intent.putExtra("post_id", dataModelHome.getId());
                    intent.putExtra("attachment", dataModelHome.getAttachment());
                    intent.putExtra("device_token", dataModelHome.getdevice_token());
                    startActivity(intent);
                    break;


                case R.id.share_id:
                    Bundle bundleLinearPosts = new Bundle();
                    bundleLinearPosts.putString("post_id", "" + dataModelHome.getId());
                    ShareBottomSheet signUp = new ShareBottomSheet();
                    signUp.setArguments(bundleLinearPosts);
                    signUp.show(getActivity().getSupportFragmentManager(), signUp.getTag());
                    break;

                case R.id.likes_count_id:
                    Intent intent1 = new Intent(getActivity(), PostLikesA.class);
                    intent1.putExtra("post_id", dataModelHome.getId()); //Optional parameters
                    getActivity().startActivity(intent1);
                    break;

                case R.id.username_id:
                    methodOpenProfile(dataModelHome);
                    break;

                case R.id.post_desc_id:
                    Intent intent2 = new Intent(getActivity(), Comment_Chat.class);
                    intent2.putExtra("post_id", dataModelHome.getId());
                    startActivity(intent2);
                    break;

                case R.id.username_comment:
                    methodOpenProfile(dataModelHome);
                    break;

                case R.id.prof_photo_id:
                    methodOpenProfile(dataModelHome);
                    break;

                case R.id.menu_id:
                    OptionsBottomSheet bottomSheet = new OptionsBottomSheet("fromHome", bundle -> {

                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("post_id", dataModelHome.getId());
                    bundle.putString("userId", dataModelHome.getUser_id());
                    bundle.putString("button", "following");
                    bundle.putString("attachment", dataModelHome.getAttachment());
                    bundle.putString("caption", dataModelHome.getCaption());
                    bundle.putString("created", dataModelHome.getCreated());
                    bundle.putString("username", dataModelHome.getUser_name());
                    bundle.putString("profileImage", dataModelHome.getUser_img());
                    bottomSheet.setArguments(bundle);
                    bottomSheet.show((getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
                    break;

                default:
                    break;
            }

        });

        psotRecycleview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        psotRecycleview.setHasFixedSize(false);
        psotRecycleview.setAdapter(adapter);
    }

    public void getHomePosts() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    token = task.getResult();
                });

        getStories();

        getHomeUpload();

        String localPost = SharedPrefrence.getOffline(getContext(), "" + SharedPrefrence.share_post_key);

        Log.e("Dattta21",":"+localPost+"....");

        if (localPost!="") {
            try {
                JSONObject postObj1 = new JSONObject(localPost);
                JSONObject postMsg = postObj1.getJSONObject("msg");
                JSONObject postObj = postMsg.getJSONObject("Post");
                JSONObject userObj = postMsg.getJSONObject("User");

                Data_Model_Home dataModelHome = new Data_Model_Home();
                dataModelHome.id = "" + postObj.getString("id");
                dataModelHome.caption = "" + postObj.getString("caption");
                dataModelHome.attachment = "" + postObj.getString("attachment");
                dataModelHome.location_string = "" + postObj.getString("location_string");
                dataModelHome.lat = "" + postObj.getString("lat");
                dataModelHome.long_location = "" + postObj.getString("long");
                dataModelHome.active = "" + postObj.getString("active");
                dataModelHome.user_id = "" + postObj.getString("user_id");
                dataModelHome.created = "" + postObj.getString("created");
                dataModelHome.is_bookmark = "0";
                dataModelHome.is_like = "0";
                dataModelHome.user_name = "" + userObj.getString("username");
                dataModelHome.user_img = "" + userObj.getString("image");
                dataModelHome.total_likes = "0";
                dataModelHome.full_name = "" + userObj.getString("full_name");

                homeArrayList.add(0, dataModelHome);
                adapter.notifyItemRemoved(0);
                adapter.notifyItemRangeChanged(0, homeArrayList.size());


            } catch (Exception n) {
                n.printStackTrace();
            }
            SharedPrefrence.saveInfoShare(getContext(), "", "" + SharedPrefrence.share_post_key);

        }

    }  //init for Home Posts
    public static GeeeFeed getInstance() {
        return instance;
    }

    public void getStories() {

        initVolleyCallbackStories();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, MyApplication.getAppContext());
        try {

            JSONObject sendObj = new JSONObject();
           // sendObj.put("user_id", "30");
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(MyApplication.getAppContext()));
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_SHOW_USER_himself_STORY, sendObj);
            Log.e("dfsfsdd",sendObj.toString());

        } catch (Exception v) {
            v.printStackTrace();
        }


    } // End method to get home upload

    // Initialize Interface Call Backs
    void initVolleyCallbackStories() {
        Log.e("DAtata201",":0:");
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Log.e("DAtata201",":1:");
                Log.e("DAtata201",":2:"+response.toString());

                Functions.logDMsg("initVolleyCallbackStories : " + response.toString());
                try {
                    // JSONObject jsonObject = response.getJSONObject("msg");
                    //JSONArray userStory = jsonObject.getJSONArray("UserStory");
                    JSONObject  jsonObject = new JSONObject(String.valueOf(response));
                    if (jsonObject.getInt("code")==200) {
                        JSONArray userStory = response.getJSONArray("msg");

                        Log.e("datattarohit","::"+userStory.length());

                        List<ShowStoryDM> userStoryList = new ArrayList<>();
                        if (userStory.length() > 0) {
                            for (int i = 0; i < userStory.length(); i++) {
                                if (i == 0) {
                                    JSONObject obj = userStory.getJSONObject(i);
                                    JSONObject storyObj = obj.getJSONObject("Story");
                                    JSONObject userObj = obj.getJSONObject("User");


                                    ShowStoryDM showStoryDM = new ShowStoryDM();
                                    if (storyObj.has("attachment")) {
                                        showStoryDM.attachment = "" + storyObj.getString("attachment");
                                    }

                                    if (storyObj.has("user_id")) {
                                        showStoryDM.userId = "" + storyObj.getString("user_id");
                                    }

                                    if (storyObj.has("type")) {
                                        showStoryDM.type = "" + storyObj.getString("type");
                                    }

                                    if (userObj.has("image")) {
                                        showStoryDM.userImg = "" + userObj.getString("image");
                                    }

                                    if (userObj.has("username")) {
                                        showStoryDM.userName = "" + userObj.getString("username");
                                    }

                                    userStoryList.add(showStoryDM);
                                } else
                                    break;
                            }
                        }



                        List<ShowStoryDM> tempList = new ArrayList<>();
                        JSONArray friendStory = response.getJSONArray("FriendStory");
                        for (int x = 0; x < friendStory.length(); x++) {
                            JSONObject obj = friendStory.getJSONObject(x);
                            JSONObject storyObj = obj.getJSONObject("Story");
                            JSONObject userObj = obj.getJSONObject("User");
                           // if (x == 0)

                            String username = userObj.getString("username");
                            boolean result = false;
                            for(int i = 0; i < tempList.size(); i++){
                                result = tempList.get(i).getUserName().equals(username);
                                if(result){
                                    break; // Early exit if found
                                }
                            }

                            if (result) {
                            } else {


                            ShowStoryDM showStoryDM = new ShowStoryDM();

                            if (storyObj.has("attachment")) {
                                showStoryDM.attachment = "" + storyObj.getString("attachment");
                            }

                            if (storyObj.has("user_id")) {
                                showStoryDM.userId = "" + storyObj.getString("user_id");
                            }
                            if (storyObj.has("type")) {
                                showStoryDM.type = "" + storyObj.getString("type");
                            }
                            if (userObj.has("image")) {
                                showStoryDM.userImg = "" + userObj.getString("image");
                            }
                            if (userObj.has("username")) {
                                showStoryDM.userName = "" + userObj.getString("username");
                            }
                            if (userObj.has("cover_image")) {
                                showStoryDM.coverimaged = "" + userObj.getString("cover_image");
                            }

                            tempList.add(showStoryDM);
                            }
                            //else
                             //   break;
                        }

                        storyDmList.clear();

                        storyDmList.addAll(userStoryList);
                        storyDmList.addAll(tempList);
                        storyAdapter.notifyDataSetChanged();
                    }else {
                        List<ShowStoryDM> userStoryList = new ArrayList<>();
                        ShowStoryDM showStoryDM = new ShowStoryDM();
                        showStoryDM.attachment = "";
                        showStoryDM.userId = "" + SharedPrefrence.getUserIdFromJson(getActivity());
                        showStoryDM.type = "first";
                        showStoryDM.userImg = "" + SharedPrefrence.getUserImageFromJson(getActivity());
                        showStoryDM.userName = "" + SharedPrefrence.getUserNameFromJson(getActivity());
                        userStoryList.add(0, showStoryDM);

                        storyDmList.clear();
                        storyDmList.addAll(userStoryList);
                        // storyDmList.addAll(tempList);

                        Log.i("dffffgfsfsfs",SharedPrefrence.getUserImageFromJson(getActivity()));
                        storyAdapter.notifyDataSetChanged();

                        //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception v) {
                    v.printStackTrace();
                }

            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                Log.e("DAtata201",":1:"+error.toString());
                Log.e("DAtata201",":1:"+error.getMessage());
            }
        };
    }

    public void getHomeUpload() {
        initVolleyCallback();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {
            JSONObject sendObj = new JSONObject();

            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getContext()));
            sendObj.put("device_token", token);

            if (homeArrayList.isEmpty() && isVisible) {
                Functions.showLoader(getActivity(), false, false);
                Functions.cancelLoader();
            }

            Log.i("Dffffffff",sendObj.toString());

            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Home_POSTS, sendObj);

        } catch (Exception v) {
            v.printStackTrace();
        }
    } // End method to get home upload

    //init for Home Posts
    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();

                recyclerRefreshLayout.setRefreshing(false);
                try {
                    JSONObject  jsonObject = new JSONObject(String.valueOf(response));


                    Log.i("Ffdgdfdfd",response.toString());
                    if (jsonObject.getInt("code")==200) {

                        JSONArray msgArr = response.getJSONArray("msg");
                        if (response.length() == 0) {
                            Main_F.tabLayout.getTabAt(1).select();
                        } else {
                            homeArrayList.clear();
                            for (int i = 0; i < msgArr.length(); i++) {
                                JSONObject obj = msgArr.getJSONObject(i);
                                JSONObject postObj = obj.getJSONObject("Post");
                                JSONObject user = obj.getJSONObject("User");
                                dataModelHome = new Data_Model_Home();

                                if (postObj.has("id")) {
                                    dataModelHome.id = "" + postObj.getString("id");
                                }

                                if (postObj.has("caption")) {
                                    dataModelHome.caption = "" + postObj.getString("caption");
                                }

                                if (postObj.has("attachment")) {
                                    dataModelHome.attachment = "" + postObj.getString("attachment");
                                }

                                if (postObj.has("location_string")) {
                                    dataModelHome.location_string = "" + postObj.getString("location_string");
                                }

                                if (postObj.has("lat")) {
                                    dataModelHome.lat = "" + postObj.getString("lat");
                                }

                                if (postObj.has("long")) {
                                    dataModelHome.long_location = "" + postObj.getString("long");
                                }


                                if (postObj.has("active")) {
                                    dataModelHome.active = "" + postObj.getString("active");
                                }

                                if (postObj.has("user_id")) {
                                    dataModelHome.user_id = "" + postObj.getString("user_id");
                                }

                                if (postObj.has("created")) {
                                    dataModelHome.created = "" + postObj.getString("created");
                                }

                                if (postObj.has("PostBookmark")) {
                                    dataModelHome.is_bookmark = "" + postObj.getString("PostBookmark");
                                }

                                if (postObj.has("PostBookmark")) {
                                    dataModelHome.is_like = "" + postObj.getString("PostLike");
                                }

                                if (user.has("username")) {
                                    dataModelHome.user_name = "" + user.getString("username");
                                }

                                if (user.has("username")) {
                                    dataModelHome.user_name = "" + user.getString("username");
                                }

                                if (user.has("image")) {
                                    dataModelHome.user_img = "" + user.getString("image");
                                }
                                if (user.has("device_token")) {
                                    dataModelHome.devicetoken = "" + user.getString("device_token");
                                }
                                if (postObj.has("total_likes")) {

                                    dataModelHome.total_likes = "" + postObj.getString("total_likes");
                                }

                                if (user.has("full_name")) {


                                    dataModelHome.full_name = "" + user.getString("full_name");
                                }


                                if (postObj.has("comment_count")) {


                                    dataModelHome.commentCount = "" + postObj.getString("comment_count");
                                }


                                homeArrayList.add(dataModelHome);
                            }


                            adapter.notifyDataSetChanged();
                        }
                    }else {
                      //  Toast.makeText(getActivity(), "Nosdd data found", Toast.LENGTH_SHORT).show();
                        empty_view_container.setVisibility(View.VISIBLE);
                      //  contnet1.setVisibility(View.VISIBLE);
                        openfriendprofile();
                    }
                } catch (Exception v) {
                  /*  Functions.cancelLoader();
                    Main_F.tabLayout.getTabAt(1).select();
                    TextView emptyResponseText = view.findViewById(R.id.empty_response_text);
                    emptyResponseText.setVisibility(View.VISIBLE);*/
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                Functions.cancelLoader();
            }
        };
    }

    private void methodOpenProfile(Data_Model_Home dataModelHome) {
        Intent myIntent = new Intent(getActivity(), View_user_Profile.class);
        myIntent.putExtra("user_id", dataModelHome.getUser_id());
        myIntent.putExtra("post_id", dataModelHome.getId());
        myIntent.putExtra("tictok",  "0"); //Optional parameters

        myIntent.putExtra("full_name", dataModelHome.getFull_name());
        myIntent.putExtra("username", dataModelHome.getUser_name());
        myIntent.putExtra("image", dataModelHome.getUser_img());
        myIntent.putExtra("bio", dataModelHome.getBio());
        myIntent.putExtra("website", dataModelHome.getWebsite());
        getActivity().startActivity(myIntent);
    }

    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getContext().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getContext().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Pick from Camera", "Pick from Gallery","Pick from Videos","Delete status","Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                builder.setTitle("Add your Story");
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Pick from Camera")) {
                        dialog.dismiss();
                        openCameraIntent();
                    } else if (options[item].equals("Pick from Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    }else if (options[item].equals("Pick from Videos")) {
                        dialog.dismiss();
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("video/*");
                        startActivityForResult(intent, com.geee.tictokcode.SimpleClasses.Variables.Pick_video_from_gallery);
                    }  else if (options[item].equals("Delete status")) {
                        DeleteFragment comment_f = new DeleteFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
                        transaction.addToBackStack("commentfrag");
                        transaction.replace(R.id.mainrsd, comment_f).commit();

                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else
                Toast.makeText(getContext(), "Storage and Camera Permission required", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Storage and Camera Permission required", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 2525);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  // prefix /
                ".jpg",         // suffix /
                storageDir      // directory /
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_GALLERY) {
            if (data != null) {
                Uri selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setAspectRatio(1, 1)
                        .start(getActivity(),GeeeFeed.this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(getActivity(), "as", Toast.LENGTH_SHORT).show();

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Functions.logDMsg("resultUri : " + resultUri);
                uploadImageFirebase(resultUri, "Name");
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Functions.logDMsg("resultUri : " + error.toString());
            }
        } else if (requestCode == 2525) {
            Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
            uploadImageFirebase(selectedImage, "Name");
            //CropImage.activity(selectedImage).start(getActivity());
        }else if (requestCode == com.geee.tictokcode.SimpleClasses.Variables.Pick_video_from_gallery) {
            Uri uri = data.getData();
            try {

                TrimVideo.activity(String.valueOf(uri))
                        .setCompressOption(new CompressOption())
                        .setTrimType(TrimType.FIXED_DURATION)
                        .setFixedDuration(18l)
//         //empty constructor for default compress option
                        .setHideSeekBar(true)
                        .setDestination(com.geee.tictokcode.SimpleClasses.Variables.app_hided_folder)  //default output path /storage/emulated/0/DOWNLOADS
                        .start(this);

              /*  Intent intent=new Intent(getActivity(), ActVideoTrimmer.class);
                intent.putExtra(TrimmerConstants.TRIM_VIDEO_URI, String.valueOf(uri));
                intent.putExtra(TrimmerConstants.DESTINATION, com.geee.tictokcode.SimpleClasses.Variables.app_hided_folder);
                intent.putExtra(TrimmerConstants.TRIM_TYPE,1);
                intent.putExtra(TrimmerConstants.FIXED_GAP_DURATION,18L); //in secs
                startActivityForResult(intent,TrimmerConstants.REQ_CODE_VIDEO_TRIMMER);*/

                    /*
                    File video_file = FileUtils.getFileFromUri(this, uri);

                    if (Functions.getfileduration(this,uri) < Variables.max_recording_duration) {
                        Chnage_Video_size(video_file.getAbsolutePath(), Variables.gallery_resize_video);

                    } else {
                        Intent intent=new Intent(Video_Recoder_A.this,Trim_video_A.class);
                        intent.putExtra("path",video_file.getAbsolutePath());
                        startActivity(intent);
                    }
                    */

            } catch (Exception e) {
                e.printStackTrace();
            }


        }/*else if(requestCode ==TrimmerConstants.REQ_CODE_VIDEO_TRIMMER){
            String path= data.getStringExtra(TrimmerConstants.TRIMMED_VIDEO_PATH);
            Chnage_Video_size(path, com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
        }*/else if (requestCode == TrimVideo.VIDEO_TRIMMER_REQ_CODE && data != null) {
            Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(data));
            Log.d("fdsfs", "Trimmed path:: " + uri);
            String path = TrimVideo.getTrimmedVideoPath(data);
            Chnage_Video_size(path, com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
        }
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                    String path = TrimVideo.getTrimmedVideoPath(result.getData());
                    Chnage_Video_size(path, com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
                    Toast.makeText(getActivity(), "aj", Toast.LENGTH_SHORT).show();
                }
                //  LogMessage.v("videoTrimResultLauncher data is null");
            });

    public void uploadImageFirebase(Uri uri, String filename) {
        //Toast.makeText(getActivity(), "aj", Toast.LENGTH_SHORT).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        StorageReference storageReference = mStorageRef.child("images/Story_" + timeStamp + ".jpg");
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 ->
                    callingStoryAPI(uri1.toString()));
          //  Toast.makeText(getActivity(), "dff", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e ->
                Functions.toastMsg(getContext(), "Failed " + e.toString())

        );
    }

    public void callingStoryAPI(String downloadUrl) {
        // Toast.makeText(getActivity(), "cx", Toast.LENGTH_SHORT).show();
        API_Calling_Methods.addStoryAPI("" + SharedPrefrence.getUserIdFromJson(getContext()),
                "" + downloadUrl, "" + Variables.typeImg, getContext()
        );

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            isVisible = true;
            new Handler().postDelayed(() -> getHomePosts(), 1000);
        }
    }
    public void Chnage_Video_size(String src_path, String destination_path){

        try {
            com.geee.tictokcode.SimpleClasses.Functions.copyFile(new File(src_path),
                    new File(destination_path));

            File file=new File(src_path);
            if(file.exists())
                file.delete();

            uploadvideoFirebase(Uri.fromFile(new File(com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video)), "name");
            /*Intent intent=new Intent(getActivity(), GallerySelectedVideo_A.class);
            intent.putExtra("video_path", com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
            startActivity(intent);*/

        //    Uri.fromFile(new File(com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video));

        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(com.geee.tictokcode.SimpleClasses.Variables.tag,e.toString());
        }
    }
    public void uploadvideoFirebase(Uri uri, String filename) {
        Toast.makeText(getActivity(), "viddeo", Toast.LENGTH_SHORT).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        StorageReference storageReference = mStorageRef.child("Video/Story_" + timeStamp + ".mp4");
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 ->
                    callingvideoStoryAPI(uri1.toString()));
            Toast.makeText(getActivity(), "videouplaod", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e ->
                Functions.toastMsg(getContext(), "Failed " + e.toString())

        );
    }
    public void callingvideoStoryAPI(String downloadUrl) {
        // Toast.makeText(getActivity(), "cx", Toast.LENGTH_SHORT).show();
        API_Calling_Methods.addStoryAPI("" + SharedPrefrence.getUserIdFromJson(getContext()),
                "" + downloadUrl, "" + Variables.typeVideo, getContext()
        );

    }
    private List<Category> imageData2=new ArrayList<>();
    private void openfriendprofile() {
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
        psotRecycleview.setLayoutManager(gridLayoutManager3);
        psotRecycleview.setItemAnimator(new DefaultItemAnimator());
        JSONObject parameters = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, com.geee.tictokcode.SimpleClasses.Variables.GEEEALLUSER, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ghg", response.toString());
                            Log.i("gdfgvgbnn",com.geee.tictokcode.SimpleClasses.Variables.GEEEALLUSER+parameters );
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            imageData2.clear();
                            if (jsonObject.getString("code").equals("200")){
                                JSONArray accArr = jsonObject.getJSONArray("msg");
                                for (int i = 0; i < accArr.length(); i++) {
                                    JSONObject obj = accArr.getJSONObject(i);
                                    JSONObject userObj = obj.getJSONObject("user");
                                    Category id = new Category();
                                    id.setBannerId(userObj.getString("id"));
                                    id.setBannerSrc(userObj.getString("image"));
                                    id.setName(userObj.getString("full_name"));
                                    id.setDescription(userObj.getString("bio"));
                                    id.setCost(userObj.getString("website"));
                                    id.setDummy_cost(userObj.getString("email"));
                                    imageData2.add(id);
                                }
//                                otherUserId = intent.getStringExtra("user_id");
//                                otherUserName = intent.getStringExtra("username");
//                                otherUserImage = intent.getStringExtra("image");
//                                stFullname = intent.getStringExtra("full_name");
//                                stUserBio = intent.getStringExtra("bio");
//                                stwebsite = intent.getStringExtra("website");
                                SubCategoryAdapter donationAdapter=new SubCategoryAdapter(getActivity(),imageData2);
                                donationAdapter.notifyDataSetChanged();
                                psotRecycleview.setAdapter(donationAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                Log.i("Dfsdfsdf",error.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }

    //--------------getallovervideos tictok
    private void Call_Api_For_get_Allvideos() {
        String type="related";
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences.getString(com.geee.tictokcode.SimpleClasses.Variables.u_id,"0"));
            parameters.put("token",MainMenuActivity.token);
            parameters.put("type",type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("dfsdfs",parameters.toString());
        ApiRequest.Call_Api(context, com.geee.tictokcode.SimpleClasses.Variables.showAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
            }
        });


    }

    public void Parse_data(String responce){

        Log.d("jhbh",responce.toString());
        data_list.clear();

        try {
           /* JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                JSONObject data=msgArray.getJSONObject(0);
                JSONObject user_info=data.optJSONObject("user_info");



                JSONArray user_videos=data.getJSONArray("user_videos");
                if(!user_videos.toString().equals("["+"0"+"]")){


                    for (int i=0;i<user_videos.length();i++) {
                        JSONObject itemdata = user_videos.optJSONObject(i);

                        Home_Get_Set item=new Home_Get_Set();
                        item.fb_id=user_info.optString("fb_id");

                        item.first_name=user_info.optString("first_name");
                        item.last_name=user_info.optString("last_name");
                        item.profile_pic=user_info.optString("profile_pic");
                        item.verified=user_info.optString("verified");

                        Log.d("resp", item.fb_id+" "+item.first_name);

                        JSONObject count=itemdata.optJSONObject("count");
                        item.like_count=count.optString("like_count");
                        item.video_comment_count=count.optString("video_comment_count");
                        item.views=count.optString("view");

                        JSONObject sound_data=itemdata.optJSONObject("sound");
                        item.sound_id=sound_data.optString("id");
                        item.sound_name=sound_data.optString("sound_name");
                        item.sound_pic=sound_data.optString("thum");
                        if(sound_data!=null) {
                            JSONObject audio_path = sound_data.optJSONObject("audio_path");
                            item.sound_url_mp3 = audio_path.optString("mp3");
                            item.sound_url_acc = audio_path.optString("acc");
                        }



                        item.privacy_type=itemdata.optString("privacy_type");
                        item.allow_comments=itemdata.optString("allow_comments");
                        item.video_id=itemdata.optString("id");
                        item.liked=itemdata.optString("liked");
                        item.gif=itemdata.optString("gif");
                        item.video_url=itemdata.optString("video");
                        item.thum=itemdata.optString("thum");
                        item.created_date=itemdata.optString("created");

                        item.video_description=itemdata.optString("description");


                        data_list.add(item);
                    }


                }else {
                }




                home_tictokAdapter.notifyDataSetChanged();

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }
*/
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");

                ArrayList<Home_Get_Set> temp_list=new ArrayList();
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject itemdata = msgArray.optJSONObject(i);
                    Home_Get_Set item = new Home_Get_Set();
                    item.fb_id = itemdata.optString("fb_id");

                    JSONObject user_info = itemdata.optJSONObject("user_info");
                    item.hopeuserid=itemdata.optString("fb_id");

                    item.username = user_info.optString("username");
                    item.first_name = user_info.optString("first_name", context.getResources().getString(R.string.app_name));
                    item.last_name = user_info.optString("last_name", "User");
                    item.profile_pic = user_info.optString("profile_pic" );
                    item.verified = user_info.optString("verified");

                    JSONObject sound_data = itemdata.optJSONObject("sound");
                    item.sound_id = sound_data.optString("id");
                    item.sound_name = sound_data.optString("sound_name");
                    item.sound_pic = sound_data.optString("thum");
                    if (sound_data != null) {
                        JSONObject audio_path = sound_data.optJSONObject("audio_path");
                        item.sound_url_mp3 = audio_path.optString("mp3");
                        item.sound_url_acc = audio_path.optString("acc");
                    }


                    JSONObject count = itemdata.optJSONObject("count");
                    item.like_count = count.optString("like_count");
                    item.video_comment_count = count.optString("video_comment_count");
                    JSONObject arcount = itemdata.optJSONObject("liked");
                    if (arcount.isNull("likes")) {
                        item.artilike = "0";
                    } else {
                        item.artilike = arcount.optString("likes");
                    }

                    item.privacy_type = itemdata.optString("privacy_type");
                    item.allow_comments = itemdata.optString("allow_comments");
                    item.allow_duet = itemdata.optString("allow_duet");
                    item.video_id = itemdata.optString("id");
                    item.liked = itemdata.optString("is_liked");
                    item.video_url = itemdata.optString("video");
                    itemdata.optString("video");


                    item.video_description = itemdata.optString("description");

                    item.thum = itemdata.optString("thum");
                    item.created_date = itemdata.optString("created");
                    data_list.add(item);
                }
                home_tictokAdapter.notifyDataSetChanged();
            }
                } catch (JSONException e) {
            e.printStackTrace();
        }

    }





    private void OpenWatchVideo(int postion) {
        //Toast.makeText(context, "u", Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", data_list);
        intent.putExtra("position",postion);
        startActivity(intent);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // using broadcast method
        context.unregisterReceiver(onDownloadComplete);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void beginDownload(){
        String url = "https://gee-e.com/gee_tictok/API/abcd.mp4";


        //                // Create request for android download manager
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://gee-e.com/gee_tictok/API/abcd.mp4"));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);

// set title and description
                request.setTitle("Rohit baba Download");
                request.setDescription("Android Data download using DownloadManager.");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"downloadfileName");
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
                        Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDataSent(String yourData) {

    }
}

