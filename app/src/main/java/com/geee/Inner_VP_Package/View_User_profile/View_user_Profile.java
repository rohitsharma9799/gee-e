package com.geee.Inner_VP_Package.View_User_profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.geee.Inner_VP_Package.Adapter.SubCategoryAdapter;
import com.geee.Location.Profile;
import com.geee.Location.TinderCard;
import com.geee.Menu.Category;
import com.geee.tictokcode.Profile.UserVideos.UserVideo_F;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.geee.Chat.Chat_Activity;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.CodeClasses.ViewPager_Adp;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Profile_Pacakge.Edit_Profile_Package.EditProfile;
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.FollowersFollowingF;
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.Grid_Posts;
import com.geee.Inner_VP_Package.Profile_Pacakge.Linear_Layout.MyPostLinear;
import com.geee.Inner_VP_Package.Profile_Pacakge.Tag_Layout.Tag_Posts;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.geee.CodeClasses.Variables.mResultCallback;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class View_user_Profile extends AppCompatActivity {
    public static RelativeLayout rl, profRl;
    TabLayout tl;
    ViewPager vp;
    ViewPager_Adp adp;
    Button messageBtn, buttonFollow;
    NestedScrollView nestedScrollView;
    LinearLayout followersLayout, followingLayout;
    String recName, requestStatus;
    String recPic;
    private List<Category> imageData2=new ArrayList<>();
    ImageView profilePic;
    VolleyService mVolleyService;
    LinearLayout rlAfterFollowing, rlRequest, editProfileLl;
    String otherUserId, otherUserName, otherUserImage, stUserBio, stFullname, stwebsite;
    ImageView backButton, btnUnfollow;
    TextView userName,usernamed, tvFullname, userBio, numOfPosts, numOfFollowers, numOfFollowing, userWebsite;
    LinearLayout sendFollowerReqL, postLayout;
    AppBarLayout mainAppbar;
    Button editProfile;
    RecyclerView recyclerView;
    KenBurnsView coverimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);


        rlAfterFollowing = findViewById(R.id.RL_after_following);
        rlRequest = findViewById(R.id.requested_rlt);
        editProfileLl = findViewById(R.id.Edit_profile_LL);
        btnUnfollow = findViewById(R.id.btn_unfollow);

        profilePic = findViewById(R.id.user_prof_img_id);
        userName = findViewById(R.id.tv_fullname);
        usernamed = findViewById(R.id.username);
        tvFullname = findViewById(R.id.tv_tag);
        userBio = findViewById(R.id.user_bio);
        numOfPosts = findViewById(R.id.num_of_posts);
        numOfFollowers = findViewById(R.id.num_of_followers);
        numOfFollowing = findViewById(R.id.num_of_following);
        mainAppbar = findViewById(R.id.mainAppbarLayout);
        postLayout = findViewById(R.id.postLayout);
        backButton = findViewById(R.id.ic_back);
        userWebsite = findViewById(R.id.user_website);
        sendFollowerReqL = findViewById(R.id.send_follower_req_L);
        coverimage = findViewById(R.id.coverimage);

        editProfile = findViewById(R.id.Edit_profile);
        editProfile.setOnClickListener(v -> {
            Intent myIntent = new Intent(View_user_Profile.this, EditProfile.class);
            myIntent.putExtra("user_id", SharedPrefrence.getUserIdFromJson(View_user_Profile.this)); //Optional parameters
            startActivity(myIntent);
        });

        //alluser
        recyclerView = findViewById(R.id.recycle_category);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(View_user_Profile.this,1,GridLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(gridLayoutManager3);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fetchplan();


        btnUnfollow.setOnClickListener(v -> methodCallUnFollow("" + SharedPrefrence.getUserIdFromJson(View_user_Profile.this),
                "" + otherUserId));

        postLayout.setOnClickListener(v -> {
            if (mainAppbar.getTop() < 0)
                mainAppbar.setExpanded(false);
            else
                mainAppbar.setExpanded(false);
        });
        // Getting user id from previous section
        Intent intent = getIntent();
        otherUserId = intent.getStringExtra("user_id");
        otherUserName = intent.getStringExtra("username");
        otherUserImage = intent.getStringExtra("image");
        stFullname = intent.getStringExtra("full_name");
        stUserBio = intent.getStringExtra("bio");
        stwebsite = intent.getStringExtra("website");
        String tictok = intent.getStringExtra("tictok");
       Log.d("dcdc",tictok);
        Log.d("fdfdfd",otherUserImage);
        userName.setText(otherUserName);
        usernamed.setText(otherUserName);
        tvFullname.setText(stFullname);

        if (otherUserImage != null && !otherUserImage.equals("")) {
            if (otherUserImage.contains("gee_tictok")){
             //   profilePic.getHierarchy().setPlaceholderImage(R.drawable.profile_image_placeholder);
             //   profilePic.setImageURI(otherUserImage);
                Picasso.get().load(otherUserImage)
                        .error(R.drawable.image_placeholder)
                        .into(profilePic);
               // Log.i("dfdfdfdf",otherUserId);
            }else {
              //  profilePic.getHierarchy().setPlaceholderImage(R.drawable.profile_image_placeholder);
              //  profilePic.setImageURI(Constants.BASE_URL+otherUserImage.replaceAll("",""));

                if (tictok.equals("1")){
                    Picasso.get().load(otherUserImage)
                            .error(R.drawable.image_placeholder)
                            .into(profilePic);
                }else {
                    Picasso.get().load(Constants.BASE_URL+otherUserImage.replaceAll("",""))
                            .error(R.drawable.image_placeholder)
                            .into(profilePic);
                }

              //  Log.d("vfvf", otherUserImage);
            }


        }

        if (stUserBio != null && !stUserBio.equals("")) {
            userBio.setVisibility(View.VISIBLE);
            userBio.setText("" + stUserBio);
        }

        if (stwebsite != null && !stwebsite.equals("")) {
           // userWebsite.setVisibility(View.VISIBLE);
            userWebsite.setText("Website: " + stwebsite);
        }

        final View parent = (View) backButton.getParent();  // button: the view you want to enlarge hit area
        parent.post(() -> {
            final Rect rect = new Rect();
            backButton.getHitRect(rect);
            rect.top -= Variables.clickAreaTop100;    // increase top hit area
            rect.left -= Variables.clickAreaLeft100;   // increase left hit area
            rect.bottom += Variables.clickAreaBottom100; // increase bottom hit area
            rect.right += Variables.clickAreaRight200;  // increase right hit area
            parent.setTouchDelegate(new TouchDelegate(rect, backButton));
        });

        backButton.setOnClickListener(v -> finish());

        buttonFollow = findViewById(R.id.button_follow);

        buttonFollow.setOnClickListener(v -> {
            followRequest("" + SharedPrefrence.getUserIdFromJson(View_user_Profile.this),
                    "" + otherUserId);
        });

        tl = findViewById(R.id.tablayout_id);
        vp = findViewById(R.id.prof_viewpager_id);
        nestedScrollView = findViewById(R.id.prof_nestedscrollview_id);
        adp = new ViewPager_Adp(getSupportFragmentManager());

        rl = findViewById(R.id.rl_id);
        profRl = findViewById(R.id.prof_tb_rl_id);
       // epRl = findViewById(R.id.ep_tb_rl_id);

        followersLayout = findViewById(R.id.followers_layout);
        followingLayout = findViewById(R.id.followingLayout);

        messageBtn = (Button) findViewById(R.id.message_btn);
        messageBtn.setOnClickListener(v -> {
            Chat_Activity chatActivity = new Chat_Activity();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            Log.i("Fdffvvvv",otherUserId+recPic);
            args.putString("receiver_id", otherUserId);
            args.putString("receiver_name", recName);
            args.putString("receiver_pic", Constants.BASE_URL + "" + recPic);
            chatActivity.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.prof_fl_id, chatActivity).commit();
        });

        followersLayout.setOnClickListener(v -> {
            Bundle bundleLinearPosts = new Bundle();
            bundleLinearPosts.putString("user_id", "" + otherUserId);
            bundleLinearPosts.putString("fromWhere", "followers");
            FollowersFollowingF f = new FollowersFollowingF();
            f.setArguments(bundleLinearPosts);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.prof_rl_id, f).commit();
        });

        followingLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("user_id", "" + otherUserId);
            bundle.putString("fromWhere", "following");
            FollowersFollowingF f = new FollowersFollowingF();
            f.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.prof_rl_id, f).commit();
        });


        // Send Parameters to Grid Layout
        Bundle bundle = new Bundle();
        bundle.putString("user_id", "" + otherUserId);
        bundle.putString("fromWhere", "" + "Otherprofile");
        Grid_Posts objGridPosts = new Grid_Posts();
        objGridPosts.setArguments(bundle);


        Bundle bundleLinearPosts = new Bundle();
        bundleLinearPosts.putString("user_id", "" + otherUserId);
        bundleLinearPosts.putString("fromWhere", "" + "Otherprofile");
        UserVideo_F objLinearPosts = new UserVideo_F();
        objLinearPosts.setArguments(bundle);

        // Send values to Tag Posts
        Bundle bundleTagsPosts = new Bundle();
        bundleTagsPosts.putString("user_id", "" + otherUserId);
        bundleTagsPosts.putString("fromWhere", "" + "Otherprofile");
        Tag_Posts objTagPosts = new Tag_Posts();
        objTagPosts.setArguments(bundle);

        nestedScrollView.setFillViewport(true);

        adp.add_fragment(objGridPosts, "Grid");
        adp.add_fragment(objLinearPosts, "Linear");
        adp.add_fragment(objTagPosts, "Tag");
        vp.setAdapter(adp);

        tl.setupWithViewPager(vp);

        final View view1 = LayoutInflater.from(View_user_Profile.this).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageview_id);
        imageView1.setColorFilter(getResources().getColor(R.color.white));
        imageView1.setImageResource(R.drawable.galleryslect);
        tl.getTabAt(0).setCustomView(view1);

        final View view2 = LayoutInflater.from(View_user_Profile.this).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView2 = (ImageView) view2.findViewById(R.id.imageview_id);
        imageView2.setColorFilter(getResources().getColor(R.color.white));

        imageView2.setImageResource(R.drawable.play);
        tl.getTabAt(1).setCustomView(view2);

        final View view3 = LayoutInflater.from(View_user_Profile.this).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView3 = (ImageView) view3.findViewById(R.id.imageview_id);
        imageView3.setColorFilter(getResources().getColor(R.color.white));
        imageView3.setImageResource(R.drawable.tag);
        tl.getTabAt(2).setCustomView(view3);

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.imageview_id);
                switch (tab.getPosition()) {
                    case 0:
                        image.setColorFilter(getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.galleryslect);
                        break;
                    case 1:
                        image.setColorFilter(getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.playselct);
                        break;
                    case 2:
                        image.setColorFilter(getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.tagselect);
                        break;

                    default:
                        break;
                }
                tab.setCustomView(view);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.imageview_id);
                switch (tab.getPosition()) {
                    case 0:
                        image.setColorFilter(getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.gallerycam);
                        break;
                    case 1:
                        image.setColorFilter(getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.play);
                        break;
                    case 2:
                        image.setColorFilter(getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.tag);
                        break;
                    default:
                        break;

                }
                tab.setCustomView(view);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //auto generated method
            }
        });


        getUserProfile();
       /* ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        coverimage.setColorFilter(new ColorMatrixColorFilter(matrix));
*/
    }


    // Calling an API to get user Profile Data

    public void getUserProfile() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, this);
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            if (otherUserId.equals(SharedPrefrence.getUserIdFromJson(View_user_Profile.this))) {
                sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(View_user_Profile.this));
            } else {
                sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(View_user_Profile.this));
                sendObj.put("other_id", otherUserId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.logDMsg("params at getProfile other : " + sendObj);

        Log.i("mimimimc", API_LINKS.API_Get_User_detail+SharedPrefrence.getUserIdFromJson(View_user_Profile.this));
        mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Get_User_detail, sendObj);


    }

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.logDMsg("response at getProfile other : " + response);

                Log.i("dfsdfsdfvvvvvv",response.toString());

                try {
                    JSONObject msgObj = response.getJSONObject("msg");
                    JSONObject userObj = msgObj.getJSONObject("User");

                    // Displaying data into Views
                    userName.setText("" + userObj.getString("username"));
                    tvFullname.setText("" + userObj.getString("full_name"));
                    String bio = userObj.getString("bio");
                    String website = userObj.getString("website");
                    if (bio != null && !bio.equals("")) {
                        userBio.setVisibility(View.VISIBLE);
                        userBio.setText("Bio: " + bio);
                    }
                    if (website != null && !website.equals("")) {
                      //  userWebsite.setVisibility(View.VISIBLE);
                        userWebsite.setText("Website: " + website);
                    }

                    numOfPosts.setText("" + userObj.getString("posts"));
                    numOfFollowers.setText("" + userObj.getString("followers"));
                    numOfFollowing.setText("" + userObj.getString("following"));

                    recName = userObj.getString("username");
                    recPic = userObj.getString("image");

                    String userIdFromLocal = SharedPrefrence.getUserIdFromJson(View_user_Profile.this);

                    if (userIdFromLocal.equals(otherUserId)) {
                        editProfileLl.setVisibility(View.VISIBLE);
                    } else {
                        JSONObject otherObj = msgObj.getJSONObject("Other");
                        requestStatus = otherObj.getString("button");
                        if (requestStatus.equalsIgnoreCase("Requested")) {
                            rlRequest.setVisibility(View.VISIBLE);
                        } else if (requestStatus.equalsIgnoreCase("Follow")) {
                            sendFollowerReqL.setVisibility(View.VISIBLE);
                        } else if (requestStatus.equalsIgnoreCase("Following")) {
                            rlAfterFollowing.setVisibility(View.VISIBLE);
                        }
                    }
                    if (userObj.getString("cover_image") != null && !userObj.getString("cover_image").equals("")) {
//                            Picasso.get().load(userObj.getString("cover_image")).into(coverimage);
                        Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("cover_image"));
                        Glide.with(View_user_Profile.this).load(uri).into(coverimage);
                    }

                } catch (Exception b) {
                    b.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }


    private void methodCallUnFollow(String userSenderId, String userReceiverId) {
        initVolleyCallbackForUnFollow();
        mVolleyService = new VolleyService(mResultCallback, this);
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("sender_id", userSenderId);
            sendObj.put("receiver_id", userReceiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.logDMsg("params at methodCallUnFollow : " + sendObj.toString());

        Log.i("Dfsfsfsf",API_LINKS.API_Send_unFollow+sendObj);
        Functions.showLoader(View_user_Profile.this, false, false);
        mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Send_unFollow, sendObj);
    }


    // Initialize Interface Call Backs
    void initVolleyCallbackForUnFollow() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.cancelLoader();

                Functions.logDMsg("params at methodCallUnFollow : " + response.toString());
                try {
                    if (response.getString("code").equals("200")) {
                        rlAfterFollowing.setVisibility(View.GONE);
                        sendFollowerReqL.setVisibility(View.VISIBLE);
                        getUserProfile();
                    }
                } catch (Exception b) {
                    b.printStackTrace();
                }
            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                Functions.cancelLoader();
            }
        };
    }


    // Follow request
    public void followRequest(String userSenderId, String userReceiverId) {
        initVolleyCallbackForFollow();
        Functions.showLoader(View_user_Profile.this, false, false);
        mVolleyService = new VolleyService(mResultCallback, this);
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("sender_id", userSenderId);
            sendObj.put("receiver_id", userReceiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Send_Follow_Request, sendObj);
        Log.d("kcmd",sendObj.toString());
    }

    // Initialize Interface Call Backs
    void initVolleyCallbackForFollow() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.cancelLoader();
                try {
                    if (response.getString("code").equals("200")) {
                        sendFollowerReqL.setVisibility(View.GONE);
                        rlRequest.setVisibility(View.VISIBLE);
                    }
                } catch (Exception b) {
                    b.printStackTrace();
                    Functions.cancelLoader();
                }
            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                Functions.cancelLoader();
            }
        };
    }

    //------------all user
    private void fetchplan() {
       /* SharedPreferences sharedpreferences = getActivity().getSharedPreferences(RootURL.PREFACCOUNT, Context.MODE_PRIVATE);
        final JSONObject json1 = new JSONObject();
        try {
            json1.put("category", categoryid);
            //json1.put("pageno","1");
            //  json1.put("user_id",sharedpreferences.getString("id",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
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
                                SubCategoryAdapter donationAdapter=new SubCategoryAdapter(View_user_Profile.this,imageData2);
                                donationAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(donationAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(View_user_Profile.this, error.toString(), Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(View_user_Profile.this).add(jsonObjectRequest);
    }
}
