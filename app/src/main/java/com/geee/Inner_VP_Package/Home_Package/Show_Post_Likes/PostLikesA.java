package com.geee.Inner_VP_Package.Home_Package.Show_Post_Likes;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.geee.Chat.Chat_Activity;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.Adapters.LikesAdp;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Like_DataModel;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.geee.CodeClasses.Variables.mResultCallback;

public class PostLikesA extends AppCompatActivity {
    String postId;
    RecyclerView verRv;
    LikesAdp likesAdp;
    EditText searchFriends;
    List<Like_DataModel> modelArrayList = new ArrayList<>();
    ImageView icBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_likes);

        Intent intent = getIntent();
        postId = intent.getStringExtra("post_id"); //if it's a string you stored.

        searchFriends = findViewById(R.id.search_friends);
        verRv = findViewById(R.id.ver_RV_id);
        icBack = findViewById(R.id.back_id);

        final View parent = (View) icBack.getParent();  // button: the view you want to enlarge hit area
        parent.post(() -> {
            final Rect rect = new Rect();
            icBack.getHitRect(rect);
            rect.top -= Variables.clickAreaTop100;    // increase top hit area
            rect.left -= Variables.clickAreaLeft100;   // increase left hit area
            rect.bottom += Variables.clickAreaBottom100; // increase bottom hit area
            rect.right += Variables.clickAreaRight200;  // increase right hit area
            parent.setTouchDelegate(new TouchDelegate(rect, icBack));
        });

        icBack.setOnClickListener(v -> finish());


        Functions.hideSoftKeyboard(PostLikesA.this);

        searchFriends.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //auto generated
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //auto generated
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
        });

        verRv.setLayoutManager(new LinearLayoutManager(PostLikesA.this, LinearLayoutManager.VERTICAL, false));
        verRv.setHasFixedSize(false);
    }

    void filter(String text) {
        List<Like_DataModel> temp = new ArrayList();
        for (Like_DataModel d : modelArrayList) {
            if (d.getFull_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        likesAdp.updateList((ArrayList<Like_DataModel>) temp);
    }

    public void getPostLikesUsers() {
        initVolleyCallback();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, PostLikesA.this);
        try {

            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(PostLikesA.this));
            sendObj.put("post_id", postId);
            Functions.showLoader(PostLikesA.this, false, false);

            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_GET_POST_LIKES, sendObj);

            Functions.logDMsg("params at getPostLike : " + sendObj);
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(PostLikesA.this, "" + v.toString());
        }


    } // End method to get home upload

    //init for Home Posts
    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                try {
                    modelArrayList.clear();
                    JSONArray msgArr = response.getJSONArray("msg");
                    for (int i = 0; i < msgArr.length(); i++) {
                        JSONObject obj = msgArr.getJSONObject(i);
                        JSONObject user = obj.getJSONObject("User");
                        JSONObject other = obj.getJSONObject("Other");
                        JSONObject postObj = obj.getJSONObject("Post");

                        Like_DataModel likeDataModel = new Like_DataModel();
                        likeDataModel.id = postObj.getString("id");
                        likeDataModel.user_id = user.getString("id");
                        likeDataModel.full_name = user.getString("full_name");
                        likeDataModel.user_name = user.getString("username");
                        likeDataModel.user_pic = user.getString("image");
                        likeDataModel.follow_unfolow = other.getString("button");

                        modelArrayList.add(likeDataModel);
                    } // End for Loop
                    // Setting up adapters
                    likesAdp = new LikesAdp(modelArrayList, PostLikesA.this, (postion, model, view) -> {
                        Like_DataModel likeDataModel = (Like_DataModel) model;
                        switch (view.getId()) {
                            case R.id.action_btn:
                                Functions.logDMsg("like follow status at postLike : " + likeDataModel.getFollow_unfolow());
                                if (likeDataModel.getFollow_unfolow().contains("Following")) {
                                    Chat_Activity chatActivity = new Chat_Activity();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    Bundle args = new Bundle();
                                    args.putString("receiver_id", likeDataModel.getUser_id());
                                    args.putString("receiver_name", likeDataModel.getFull_name());
                                    args.putString("receiver_pic", Constants.BASE_URL + "" + likeDataModel.getUser_pic());
                                    chatActivity.setArguments(args);
                                    transaction.addToBackStack(null);
                                    transaction.replace(R.id.postlikeContainer, chatActivity).commit();
                                } else if (likeDataModel.getFollow_unfolow().equalsIgnoreCase("Follow")) {
                                    followRequest("" + SharedPrefrence.getUserIdFromJson(PostLikesA.this),
                                            "" + likeDataModel.getUser_id(), postion);
                                }

                                break;

                            case R.id.RL_Main:
                                Intent myIntent = new Intent(PostLikesA.this, View_user_Profile.class);
                                myIntent.putExtra("tictok",  "0"); //Optional parameters

                                myIntent.putExtra("user_id", likeDataModel.getUser_id()); //Optional parameters
                                myIntent.putExtra("post_id", likeDataModel.getId()); //Optional parameters
                                myIntent.putExtra("full_name", likeDataModel.getFull_name()); //Optional parameters
                                myIntent.putExtra("username", likeDataModel.getUser_name()); //Optional parameters
                                myIntent.putExtra("image", likeDataModel.getUser_pic()); //Optional parameters
                                startActivity(myIntent);
                                break;
                            default:
                                break;
                        }
                    });
                    verRv.setAdapter(likesAdp);
                } catch (Exception v) {
                    Functions.cancelLoader();
                    Functions.toastMsg(PostLikesA.this, "Err " + v.toString());
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.cancelLoader();
                Functions.toastMsg(PostLikesA.this, "Err " + error.toString());

            }
        };
    }
    // ENd Data From API


    // Follow request

    public void followRequest(String userSenderId, String userReceiverId, int postion) {
        initVolleyCallbackForFollow(postion);
        Functions.showLoader(PostLikesA.this, false, false);
        Variables.mVolleyService = new VolleyService(mResultCallback, this);
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("sender_id", userSenderId);
            sendObj.put("receiver_id", userReceiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Send_Follow_Request, sendObj);
    }

    // Initialize Interface Call Backs
    void initVolleyCallbackForFollow(int postion) {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.logDMsg("response at post like follow : " + response);
                Functions.cancelLoader();
                try {
                    if (response.getString("code").equals("200")) {
                        likesAdp.notifyDataSetChanged();
                        JSONObject obj = response.getJSONObject("msg");
                        JSONObject user = obj.getJSONObject("Receiver");

                        Like_DataModel likeDataModel = new Like_DataModel();
                        likeDataModel.user_id = user.getString("id");
                        likeDataModel.full_name = user.getString("full_name");
                        likeDataModel.user_name = user.getString("username");
                        likeDataModel.user_pic = user.getString("image");
                        likeDataModel.follow_unfolow = "Requested";

                        modelArrayList.remove(postion);
                        modelArrayList.add(postion, likeDataModel);
                        likesAdp.notifyDataSetChanged();
                    }
                } catch (Exception b) {
                    b.printStackTrace();
                    Functions.cancelLoader();
                }
            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.cancelLoader();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPostLikesUsers();
    }
}
