package com.geee.Inner_VP_Package.Home_Package;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Home_Package.Adapters.HomePostAdapter;
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
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Utils.MyApplication;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*import com.gowtham.library.ui.ActVideoTrimmer;
import com.gowtham.library.utils.TrimmerConstants;*/

public class Home extends Fragment {
    private static final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
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
    ImageView location;
    public  ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar_id;
    String currentVersion="";
    TextView location1;
    private static Home instance;
    public static Home getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.home, null);

        Viewpagersetup();
        Variables.userId = SharedPrefrence.getUserIdFromJson(getContext());
        Variables.userName = SharedPrefrence.getUserNameFromJson(getContext());
        Variables.userPic = SharedPrefrence.getUserImgFromJson(getContext());

        mStorageRef = FirebaseStorage.getInstance().getReference();
        toolbar_id = view.findViewById(R.id.toolbar_id);
        chatIv = view.findViewById(R.id.chat_id);
      //  location = view.findViewById(R.id.location);
        location1 = view.findViewById(R.id.location1);
        psotRecycleview = view.findViewById(R.id.homePostsRecycler);
        recyclerRefreshLayout = view.findViewById(R.id.refresh_layout);
        storiesRecyclerview = view.findViewById(R.id.stories_recyclerview);
        recyclerRefreshLayout.setOnRefreshListener(() -> getHomePosts());
        setgm();
        final View parent = (View) chatIv.getParent();  // button: the view you want to enlarge hit area
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

        setUpHomeAdapter();
        setUpStoriesAdapter();

     /*   location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//* Intent myIntent = new Intent(getActivity(), LocationActivity.class);
                getActivity().startActivity(myIntent);*//*
                Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });*/

        return view;
    }

    private void setgm() {
       ImageView  sun = view.findViewById(R.id.sun);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            location1.setText( "Good Morning "+SharedPrefrence.getUserNameFromJson(getContext()));
            Picasso.get().load(R.drawable.sunrise).into(sun);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            location1.setText( "Good Afternoon "+SharedPrefrence.getUserNameFromJson(getContext()));
            Picasso.get().load(R.drawable.sun).into(sun);
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            location1.setText( "Good Evening "+SharedPrefrence.getUserNameFromJson(getContext()));
            Picasso.get().load(R.drawable.sunset).into(sun);
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            location1.setText( "Good Night "+SharedPrefrence.getUserNameFromJson(getContext()));
            Picasso.get().load(R.drawable.moon).into(sun);
        }
    }

    private void Viewpagersetup() {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("G feed"));
      //  tabLayout.addTab(tabLayout.newTab().setText("G star"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final HomeAdapter adapter = new HomeAdapter(getActivity(), getChildFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
               /* if (tab.getPosition()==1){
               *//*     toolbar_id.animate().translationY(-112).setDuration(600L)

                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    tabLayout.setVisibility(View.GONE);
                                }
                            }).start();
                    Main_F.bottombarid.animate().translationY(0).setDuration(600L)

                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    Main_F.bottombarid.setVisibility(View.GONE);
                                }
                            }).start();

*//*
                }else {
                //    tabLayout.setVisibility(View.GONE);
                   *//* toolbar_id.animate().translationY(0).setDuration(600L).start();
                    toolbar_id.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    Main_F.bottombarid.animate().translationY(0).setDuration(600L).start();
                    Main_F.bottombarid.setVisibility(View.VISIBLE);*//*
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

    }

    private void setUpStoriesAdapter() {
        storyAdapter = new StoryAdapter(getContext(), storyDmList, new StoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view) {
                ShowStoryDM storyDm = dmArrayList.get(postion);
                switch (view.getId()) {
                    case R.id.rv_id:
                        if (storyDm.getType().equals("first")) {
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

    }

    private void setUpHomeAdapter() {
        adapter = new HomePostAdapter(getActivity(), homeArrayList, (postion, model, view) -> {
            Data_Model_Home dataModelHome = (Data_Model_Home) model;
            switch (view.getId()) {
                case R.id.comment_id:
                    Intent myIntent = new Intent(getActivity(), Comment_Chat.class);
                    myIntent.putExtra("post_id", dataModelHome.getId());
                    myIntent.putExtra("attachment", dataModelHome.getAttachment());
                    myIntent.putExtra("device_token", dataModelHome.getdevice_token());
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

        String localPost = SharedPrefrence.getOffline(MyApplication.getAppContext(), "" + SharedPrefrence.share_post_key);

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
            SharedPrefrence.saveInfoShare(MyApplication.getAppContext(), "", "" + SharedPrefrence.share_post_key);

        }

    }  //init for Home Posts

    public void getStories() {

        initVolleyCallbackStories();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, MyApplication.getAppContext());
        try {

            JSONObject sendObj = new JSONObject();
           // sendObj.put("user_id", "30");
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(MyApplication.getAppContext()));
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_SHOW_USER_himself_STORY, sendObj);
            Log.e("dfsfsdd",API_LINKS.API_SHOW_USER_himself_STORY+sendObj.toString());

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


                Log.e("DAtata201",":2:"+response.toString());

             Functions.logDMsg("initVolleyCallbackStories : " + response.toString());
                try {
                   // JSONObject jsonObject = response.getJSONObject("msg");
                    //JSONArray userStory = jsonObject.getJSONArray("UserStory");
                  JSONObject  jsonObject = new JSONObject(String.valueOf(response));
                    if (jsonObject.getInt("code")==200) {
                        storyDmList.clear();
                        JSONArray userStory = response.getJSONArray("msg");

                        List<ShowStoryDM> userStoryList = new ArrayList<>();
                        if (userStory.length() > 0) {
                            for (int i = 0; i < userStory.length(); i++) {
                                if (i == 0) {
                                    JSONObject obj = userStory.getJSONObject(i);
                                    JSONObject storyObj = obj.getJSONObject("Story");
                                    JSONObject userObj = obj.getJSONObject("User");


                                    ShowStoryDM showStoryDM = new ShowStoryDM();
                                    if (storyObj.isNull("attachment")) {
                                        //showStoryDM.attachment = "" + storyObj.getString("attachment");
                                    }else {
                                        showStoryDM.attachment = "" + storyObj.getString("attachment");

                                    }

                                    if (storyObj.isNull("user_id")) {
                                        //showStoryDM.userId = "" + storyObj.getString("user_id");
                                    }else {
                                        showStoryDM.userId = "" + storyObj.getString("user_id");

                                    }

                                    if (storyObj.isNull("type")) {
                                        showStoryDM.type = "first";
                                    }else {
                                        showStoryDM.type = "" + storyObj.getString("type");

                                    }

                                    if (userObj.isNull("image")) {
                                        showStoryDM.userImg = "" + userObj.getString("image");
                                    }else {

                                        showStoryDM.userImg = "" + userObj.getString("image");

                                    }
                                    if (userObj.isNull("username")) {
                                        showStoryDM.userName = "" + userObj.getString("username");
                                    }else {
                                        showStoryDM.userName = "" + userObj.getString("username");

                                    }


                                    Log.i("mimimimicd",userStoryList.toString());

                                    userStoryList.add(showStoryDM);

                                } else
                                    break;
                            }
                        }


                        //storyDmList.clear();
                        storyDmList =  new ArrayList<>();

                        Log.e("rohitlele",":step0:"+storyDmList.size());

                        storyDmList.addAll(userStoryList);

                        Log.e("rohitlele",":step1:"+storyDmList.size());


                        List<ShowStoryDM> tempList = new ArrayList<>();
                        JSONArray friendStory = response.getJSONArray("FriendStory");
                        Log.e("rohitlele","::"+friendStory.length());
                        for (int x = 0; x < friendStory.length(); x++) {
                           // if (x == 0)
                           // {
                            JSONObject obj = friendStory.getJSONObject(x);
                            JSONObject storyObj = obj.getJSONObject("Story");
                            JSONObject userObj = obj.getJSONObject("User");

                            Log.e("rohitlele",":"+x+":"+storyObj.get("user_id"));
                            Log.e("rohitlele",":"+x+":"+userObj.get("username"));

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


                            tempList.add(showStoryDM);

                            Log.e("rohitlele",":"+x+":tempsize:"+tempList.size());
                                Log.i("rfrfrfrfgg",tempList.toString());
                          //  } else
                           //     break;
                        }



                        storyDmList.addAll(tempList);

                        Log.e("rohitlele",":step2:"+storyDmList.size());

                        storyAdapter = new StoryAdapter(getContext(), storyDmList, new StoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view) {
                                ShowStoryDM storyDm = dmArrayList.get(postion);
                                switch (view.getId()) {
                                    case R.id.rv_id:
                                        if (storyDm.getType().equals("first")) {
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

                            Log.i("x ",SharedPrefrence.getUserImageFromJson(getActivity()));
                        storyDmList.clear();
                        storyDmList.addAll(userStoryList);
                       // storyDmList.addAll(tempList);
                        storyAdapter = new StoryAdapter(getContext(), storyDmList, new StoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view) {
                                ShowStoryDM storyDm = dmArrayList.get(postion);
                                switch (view.getId()) {
                                    case R.id.rv_id:
                                        if (storyDm.getType().equals("first")) {
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
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback,MyApplication.getAppContext());
        try {
            JSONObject sendObj = new JSONObject();

            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(MyApplication.getAppContext()));
            sendObj.put("device_token", token);

            if (homeArrayList.isEmpty() && isVisible) {
                Functions.showLoader(MyApplication.getAppContext(), false, false);
                Functions.cancelLoader();
            }

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


                    Log.i("dfsdfsdf",response.toString());
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
                     //   Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                       // View view = findViewById(android.R.id.content);
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
        myIntent.putExtra("tictok",  "0"); //Optional parameters

        myIntent.putExtra("user_id", dataModelHome.getUser_id());
        myIntent.putExtra("post_id", dataModelHome.getId());
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
                startActivityForResult(pictureIntent, 222);
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
                        .start(getActivity());
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Functions.logDMsg("resultUri : " + resultUri);
                uploadImageFirebase(resultUri, "Name");
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Functions.logDMsg("resultUri : " + error.toString());
            }
        } else if (requestCode == 222) {
            Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
           // CropImage.activity(selectedImage).start(getActivity());
            uploadImageFirebase(selectedImage, "Name");
        } else if (requestCode == com.geee.tictokcode.SimpleClasses.Variables.Pick_video_from_gallery) {
            Uri uri = data.getData();
            try {


/*                Intent intent=new Intent(getActivity(), ActVideoTrimmer.class);
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
        }*/
    }


    public void uploadImageFirebase(Uri uri, String filename) {
        //Toast.makeText(getActivity(), "aj", Toast.LENGTH_SHORT).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        StorageReference storageReference = mStorageRef.child("images/Story_" + timeStamp + ".jpg");
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            // Get a URL to the uploaded content

            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 ->
                    callingStoryAPI(uri1.toString()));
            Toast.makeText(getActivity(), "dff", Toast.LENGTH_SHORT).show();

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

           /* Intent intent=new Intent(getActivity(), GallerySelectedVideo_A.class);
            intent.putExtra("video_path", com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
            startActivity(intent);*/

        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(com.geee.tictokcode.SimpleClasses.Variables.tag,e.toString());
        }
    }
    public void uploadvideoFirebase(Uri uri, String filename) {
       // Toast.makeText(getActivity(), "viddeo", Toast.LENGTH_SHORT).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        StorageReference storageReference = mStorageRef.child("Video/Story_" + timeStamp + ".mp4");
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 ->
                    callingvideoStoryAPI(uri1.toString()));
           // Toast.makeText(getActivity(), "videouplaod", Toast.LENGTH_SHORT).show();
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



}
