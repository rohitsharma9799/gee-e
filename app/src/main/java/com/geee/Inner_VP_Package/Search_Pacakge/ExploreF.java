package com.geee.Inner_VP_Package.Search_Pacakge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.DataModel.Grid_Data_Model;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Adapters.Tags_Search_adp;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Adapters.Tags_Search_adp_Explore;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Search_DataModel.Account_search_DM;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Search_thing_activity;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.geee.tictokcode.Discover.Discover_Adapter;
import com.geee.tictokcode.Discover.Discover_Get_Set;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.WatchVideos.WatchVideos_F;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExploreF extends Fragment {

    View view;
    SwipeRefreshLayout swipeRefresh;
    List<Grid_Data_Model> userPosts = new ArrayList<>();
    TextView search;
    RecyclerView latestTrendRV;
    RecyclerView recyclerView;
    ImageView icSearch,postImage;
    Explore_Adapter layoutAdapter;
    EditText edittext_id;
    String userProfileResponse;



    Context context;
    EditText search_edit;


    ArrayList<Discover_Get_Set> datalist;

    Discover_Adapter adapter;


    //------------TAG
    RecyclerView accountRv;
    Tags_Search_adp_Explore accountAdp;
    List<Account_search_DM> searchDmArrayList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = LayoutInflater.from(getContext()).inflate(R.layout.explore_f, null);

        icSearch = view.findViewById(R.id.ic_search);
        search = view.findViewById(R.id.tv_caption);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        latestTrendRV = view.findViewById(R.id.latest_trend_RV);
        postImage = view.findViewById(R.id.comment_prof_photo_id);
        edittext_id = view.findViewById(R.id.edittext_id);

        userProfileResponse = SharedPrefrence.getOffline(getContext(), SharedPrefrence.share_user_profile_pic);

        
        if (userProfileResponse != null) {
            // If user profile is not null
            try {
                JSONObject userProfile = new JSONObject(userProfileResponse);
                JSONObject userObj = userProfile.getJSONObject("msg").getJSONObject("User");
                userObj.getString("image");

                if (userObj.getString("image") != null && !userObj.getString("image").equals("")) {
                    Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("image"));
                    postImage.setImageURI(uri);
                    Log.i("ursdhsi",postImage.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        swipeRefresh.setOnRefreshListener(() -> {
            userPosts.clear();
            methodSearch();
            datalist.clear();
            Call_Api_For_get_Allvideos();
        });


        icSearch.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                Intent sendIntent = new Intent(getContext(), Search_thing_activity.class);
                sendIntent.putExtra("serchtxt",edittext_id.getText().toString());
                startActivity(sendIntent);
                Functions.hideSoftKeyboard(getActivity());
            }
            return true; // return is important...
        });

        edittext_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        setAdapter();
        methodSearch();

        //---------Discovery
        datalist=new ArrayList<>();


        recyclerView =  view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter=new Discover_Adapter(getActivity(), datalist, new Discover_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<Home_Get_Set> datalist, int postion) {

                OpenWatchVideo(postion,datalist);

            }
        });



        recyclerView.setAdapter(adapter);
        Call_Api_For_get_Allvideos();

        //-------------TAG
        String searchKey = SharedPrefrence.getOffline(getContext(), SharedPrefrence.share_search_key);

        accountRv = view.findViewById(R.id.Accounts_RV_id);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(),3,GridLayoutManager.HORIZONTAL,false);
        accountRv.setLayoutManager(gridLayoutManager3);
        accountRv.setHasFixedSize(false);

        searchDmArrayList.clear();

        searchTags(searchKey);
        return view;
    }

    private void performSearch() {
        edittext_id.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(edittext_id.getWindowToken(), 0);
        Intent sendIntent = new Intent(getContext(), Search_thing_activity.class);
        sendIntent.putExtra("serchtxt",edittext_id.getText().toString());
        startActivity(sendIntent);
        Functions.hideSoftKeyboard(getActivity());
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    methodSearch();
                }
            }, 75);
        }
    }

    public void setAdapter() {
    //    GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        latestTrendRV.setLayoutManager(staggeredGridLayoutManager);
        layoutAdapter = new Explore_Adapter(getContext(), userPosts);
        latestTrendRV.setAdapter(layoutAdapter);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
//        latestTrendRV.setLayoutManager(staggeredGridLayoutManager);
//        layoutAdapter = new Explore_Adapter(getContext(), userPosts);
//        latestTrendRV.setAdapter(layoutAdapter);
    }

    public void methodSearch() {
        initVolleyCallback();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {

            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getActivity()));
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Home_Search_POSTS, sendObj);

        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "" + v.toString());
        }


    }

    // Init Call Backs
    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                swipeRefresh.setRefreshing(false);
                try {
                    JSONArray msgArr = response.getJSONArray("msg");
                    userPosts.clear();
                    for (int i = 0; i < msgArr.length(); i++) {

                        JSONObject obj = msgArr.getJSONObject(i);
                        JSONObject postObj = obj.getJSONObject("Post");
                        JSONObject user = obj.getJSONObject("User");

                        Grid_Data_Model gridDataModel = new Grid_Data_Model();

                        gridDataModel.id = "" + postObj.getString("id");
                        gridDataModel.caption = "" + postObj.getString("caption");
                        gridDataModel.attachment = "" + postObj.getString("attachment");
                        gridDataModel.location_string = "" + postObj.getString("location_string");
                        gridDataModel.lat = "" + postObj.getString("lat");
                        gridDataModel.long_location = "" + postObj.getString("long");
                        gridDataModel.active = "" + postObj.getString("active");
                        gridDataModel.user_id = "" + postObj.getString("user_id");
                        gridDataModel.created = "" + postObj.getString("created");
                        gridDataModel.is_bookmark = "" + postObj.getString("PostBookmark");
                        gridDataModel.is_like = "" + postObj.getString("PostLike");
                       // gridDataModel.commentCount = "" + postObj.getString("comment_count");
                        gridDataModel.user_name = "" + user.getString("username");
                        gridDataModel.user_img = "" + user.getString("image");
                        gridDataModel.total_likes = "" + postObj.getString("total_likes");
                     //   gridDataModel.follow = "" + user.getString("button");


                        userPosts.add(gridDataModel);
                    } // End for Loop

                    Functions.logDMsg("resp at search home :" + userPosts.size());

                    layoutAdapter.notifyDataSetChanged();
                    latestTrendRV.scheduleLayoutAnimation();


                } catch (Exception v) {
                    Functions.logDMsg("resp at search home :" + v.toString());
                    v.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }
    //-----------Exploredata
    private void Call_Api_For_get_Allvideos() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences.getString(com.geee.tictokcode.SimpleClasses.Variables.u_id,"0"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("resp",parameters.toString());

        ApiRequest.Call_Api(getActivity(), com.geee.tictokcode.SimpleClasses.Variables.discover, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
                swipeRefresh.setRefreshing(false);
            }
        });



    }


    public void Parse_data(String responce){

        datalist.clear();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                for (int d=0;d<msgArray.length();d++) {
                    Discover_Get_Set discover_get_set=new Discover_Get_Set();
                    JSONObject discover_object =msgArray.optJSONObject(d);
                    discover_get_set.title =discover_object.optString("section_name");

                    JSONArray video_array=discover_object.optJSONArray("sections_videos");

                    ArrayList<Home_Get_Set> video_list = new ArrayList<>();
                    for (int i = 0; i < video_array.length(); i++) {
                        JSONObject itemdata = video_array.optJSONObject(i);
                        Home_Get_Set item = new Home_Get_Set();


                        JSONObject user_info = itemdata.optJSONObject("user_info");
                        item.fb_id = user_info.optString("fb_id");
                        item.username = user_info.optString("username");
                        item.first_name = user_info.optString("first_name");
                        item.last_name = user_info.optString("last_name");
                        item.profile_pic = user_info.optString("profile_pic");
                        item.verified =user_info.optString("verified");

                        JSONObject count = itemdata.optJSONObject("count");
                        item.like_count = count.optString("like_count");
                        item.video_comment_count = count.optString("video_comment_count");


                        JSONObject sound_data=itemdata.optJSONObject("sound");
                        item.sound_id=sound_data.optString("id");
                        item.sound_name=sound_data.optString("sound_name");
                        item.sound_pic=sound_data.optString("thum");
                        if(sound_data!=null) {
                            JSONObject audio_path = sound_data.optJSONObject("audio_path");
                            item.sound_url_mp3 = audio_path.optString("mp3");
                            item.sound_url_acc = audio_path.optString("acc");
                        }


                        item.video_id = itemdata.optString("id");
                        item.liked = itemdata.optString("liked");


                        item.video_url=itemdata.optString("video");


                        item.thum = itemdata.optString("thum");
                        item.gif =itemdata.optString("gif");
                        item.created_date = itemdata.optString("created");
                        item.video_description=itemdata.optString("description");

                        video_list.add(item);
                    }

                    discover_get_set.arrayList=video_list;

                    datalist.add(discover_get_set);

                }

                adapter.notifyDataSetChanged();

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    // When you click on any Video a new activity is open which will play the Clicked video
    private void OpenWatchVideo(int postion,ArrayList<Home_Get_Set> data_list) {

        Intent intent=new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", data_list);
        intent.putExtra("position",postion);
        startActivity(intent);

    }

    public void searchTags(String searchKey) {
        initVolleyCallbacktag();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getContext()));
            sendObj.put("keyword", searchKey);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Search_Tag_Post, sendObj);
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "" + v.toString());
        }
    }


    // Initialize Interface Call Backs
    void initVolleyCallbacktag() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Log.i("gcgcvg",response.toString());
                try {
                    JSONObject  jsonObject = new JSONObject(String.valueOf(response));


                    JSONArray msgArr = response.getJSONArray("msg");
                    // Save Response for Offline Showing Data

                    for (int i = 0; i < msgArr.length(); i++) {
                        JSONObject obj = msgArr.getJSONObject(i);
                        JSONObject userObj = obj.getJSONObject("User");
                        JSONObject postObj = obj.getJSONObject("Post");

                        Account_search_DM account = new Account_search_DM();
                        account.id = postObj.getString("id");
                        account.username = "" + userObj.getString("username");
                        account.full_name = "" + userObj.getString("full_name");
                        account.email = "" + userObj.getString("email");
                        account.website = "" + postObj.getString("caption");
                        account.bio = "" + userObj.getString("bio");
                        account.phone = "" + userObj.getString("phone");
                        account.gender = "" + userObj.getString("gender");
                        account.profile = "" + userObj.getString("image");

                        searchDmArrayList.add(account);
                    }  // End for Loop
                    // Setting up adapter
                    accountAdp = new Tags_Search_adp_Explore(getContext(), searchDmArrayList);
                    accountRv.setAdapter(accountAdp);

                    // End setting up adapter
                } catch (Exception b) {
                    b.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {


                Functions.toastMsg(getContext(), "Err " + error.toString());

            }
        };
    }
}
