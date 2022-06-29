package com.geee.Inner_VP_Package.Home_Package;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.Adapters.Comment_Adp;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Comment_Data_Model;
import com.geee.Inner_VP_Package.Home_Package.Share_Bottom_Sheet.ShareBottomSheet;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comment_Chat extends AppCompatActivity implements View.OnClickListener {
    String userProfileResponse,attachment;
    Toolbar toolbar;
    ImageView chat;
    ImageView commentProfPhotoId;
    RecyclerView recyclerView;
    List<Comment_Data_Model> arrayList = new ArrayList<>();
    List<Comment_Data_Model> dataModelList = new ArrayList<>();
    Comment_Adp commentAdp;
    EditText commentText;
    TextView addComment;
    ImageView icBack;
    String postId,device_token;
    private List<Integer> selectedIds = new ArrayList<>();
    private ActionMode actionMode;
    private boolean isMultiSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_chat);

        toolbar = findViewById(R.id.toolbar_id);
        chat = toolbar.findViewById(R.id.postImage);
        recyclerView = findViewById(R.id.comment_RV_id);
        commentText = findViewById(R.id.edittext_id);
        addComment = findViewById(R.id.post_id);
        icBack = findViewById(R.id.ic_back);
        commentProfPhotoId = findViewById(R.id.comment_prof_photo_id);

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

//        String imgUrl = SharedPrefrence.getUserImageFromJson(Comment_Chat.this);
//        Log.i("ghgggjh",imgUrl);
//        if (imgUrl != null && !imgUrl.equals("")) {
//            Uri uri = Uri.parse(Constants.BASE_URL + imgUrl);
//            commentProfPhotoId.setImageURI(uri);
//        }
        userProfileResponse = SharedPrefrence.getOffline(Comment_Chat.this, SharedPrefrence.share_user_profile_pic);
        if (userProfileResponse != null) {
            // If user profile is not null
            try {
                JSONObject userProfile = new JSONObject(userProfileResponse);
                JSONObject userObj = userProfile.getJSONObject("msg").getJSONObject("User");
                userObj.getString("image");

                if (userObj.getString("image") != null && !userObj.getString("image").equals("")) {
                    Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("image"));
                    Log.i("vvvvvv",userObj.getString("image"));
                  //  commentProfPhotoId.setImageURI(uri);
                    //  Log.i("ursdhsi",profilePic.toString());

                    Picasso.get().load(uri)
                            .placeholder(R.drawable.profile_image_placeholder)
                            .into(commentProfPhotoId);
                }else {
                    Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("image"));
                    Log.i("vv fffgvvvv",userObj.getString("image"));
                    //  commentProfPhotoId.setImageURI(uri);
                    //  Log.i("ursdhsi",profilePic.toString());

                    Picasso.get().load(uri)
                            .placeholder(R.drawable.profile_image_placeholder)
                            .into(commentProfPhotoId);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        Intent intent = getIntent();
        postId = intent.getStringExtra("post_id"); //if it's a string you stored.
        device_token = intent.getStringExtra("device_token");
        attachment = intent.getStringExtra("attachment");
        addComment.setOnClickListener(v -> {

            String comment = commentText.getText().toString();
            commentText.getText().toString();
            if (commentText.getText().toString().length() == 0) {
                Functions.toastMsg(Comment_Chat.this, "Comment cant be empty");
            } else {

                addComment(postId, SharedPrefrence.getUserIdFromJson(Comment_Chat.this),
                        "comment", comment
                );
                commentText.setText("");
                sendFCMPush(device_token,SharedPrefrence.getUserNameFromJson(Comment_Chat.this)+" comment on your post\n"+comment);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(Comment_Chat.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(false);

        methodSetUpAdapter();
        getAllComment(postId);

        icBack.setOnClickListener(this);
        chat.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.postImage:
                Bundle bundleLinearPosts = new Bundle();
                bundleLinearPosts.putString("post_id", "" + postId);
                ShareBottomSheet signUp = new ShareBottomSheet();
                signUp.setArguments(bundleLinearPosts);
                signUp.show(getSupportFragmentManager(), signUp.getTag());
                break;
            default:
                break;
        }
    }


    public void getAllComment(String postId) {
        initVolleyCallback();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, Comment_Chat.this);
        try {

            JSONObject sendObj = new JSONObject();
            sendObj.put("post_id", postId);
            Functions.showLoader(Comment_Chat.this, false, false);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Show_Comment, sendObj);

        } catch (Exception v) {
            v.printStackTrace();
        }


    }

    //for Home Posts

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                try {
                    JSONArray msgArr = response.getJSONArray("msg");
                    // Save Response for Offline Showing Data

                    for (int i = 0; i < msgArr.length(); i++) {

                        JSONObject obj = msgArr.getJSONObject(i);
                        JSONObject postActionObj = obj.getJSONObject("PostComment");
                        JSONObject user = obj.getJSONObject("User");

                        Comment_Data_Model commentDataModel = new Comment_Data_Model();
                        commentDataModel.id = postActionObj.getInt("id");
                        commentDataModel.PA_comment = "" + postActionObj.getString("comment");
                        commentDataModel.user_name = "" + user.getString("username");
                        commentDataModel.user_id = "" + user.getString("id");
                        commentDataModel.full_name = "" + user.getString("full_name");
                        commentDataModel.user_img = "" + user.getString("image");
                        commentDataModel.time_ago = "" + Functions.getCommentsTimeAgo(postActionObj.getString("created"));


                        arrayList.add(commentDataModel);
                    } // End for Loop

                    commentAdp.notifyDataSetChanged();


                } catch (Exception v) {
                    Functions.cancelLoader();
                    v.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                Functions.cancelLoader();
            }
        };
    }

    private void methodSetUpAdapter() {
        commentAdp = new Comment_Adp(Comment_Chat.this, arrayList, new Comment_Adp.onItemLongClickListener() {
            @Override
            public void onItemClick(List<Comment_Data_Model> dmArrayList, int postion, View view) {
                Comment_Data_Model commentDataModel = dmArrayList.get(postion);
                switch (view.getId()) {
                    case R.id.main_RL_1:
                        if (commentDataModel.getUser_id().equals(SharedPrefrence.getUserIdFromJson(getApplicationContext())))
                            showPopup(commentDataModel, postion);
                        break;

                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(commentAdp);
    }
    // ENd Data From API

    // this will show the logout dialog
    private void showPopup(Comment_Data_Model commentDataModel, int postion) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Comment_Chat.this);
        alert.setMessage(R.string.are_you_sure)
                .setPositiveButton("Delete", (dialog, which) -> remove(commentDataModel.getId(), postion)).setNegativeButton("cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    public void addComment(String postId, String userId, String key, String value) {
        initVolleyCallbackAddComment();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, Comment_Chat.this);
        try {
            JSONObject params = new JSONObject();
            params.put("post_id", postId);
            params.put("user_id", userId);
            params.put(key, value);

            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_POST_COMMENT, params);

        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    // Initialize Interface Call Backs
    void initVolleyCallbackAddComment() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("msg");

                    JSONObject postActionObj = obj.getJSONObject("PostComment");
                    JSONObject user = obj.getJSONObject("User");


                    Comment_Data_Model commentDataModel = new Comment_Data_Model();
                    commentDataModel.id = postActionObj.getInt("id");
                    commentDataModel.PA_comment = "" + postActionObj.getString("comment");
                    commentDataModel.user_name = "" + user.getString("username");
                    commentDataModel.user_id = "" + user.getString("id");
                    commentDataModel.full_name = "" + user.getString("full_name");
                    commentDataModel.user_img = "" + user.getString("image");
                    commentDataModel.time_ago = "" + Functions.getCommentsTimeAgo(postActionObj.getString("created"));

                    dataModelList.add(commentDataModel);
                    addComment1(commentDataModel);
                } catch (Exception v) {
                    v.printStackTrace();
                }


            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }
    // ENd Data From API


    public void remove(int removeValue, int removeIndex) {
        arrayList.remove(removeIndex);
        commentAdp.notifyItemRemoved(removeIndex);
        commentAdp.notifyDataSetChanged();
        // Apply API
        API_Calling_Methods.commentDelete(removeValue, Comment_Chat.this);


    }

    public void addComment1(Comment_Data_Model comm) {
        arrayList.add(arrayList.size(), comm);
        commentAdp.notifyItemRemoved(arrayList.size());
        commentAdp.notifyItemRangeChanged(arrayList.size(), arrayList.size());
        recyclerView.smoothScrollToPosition(arrayList.size());
    }


    private void sendFCMPush(String token, String message) {

       Log.i("fdddfffff",attachment);
        final JSONObject[] obj = {null};
        final JSONObject[] objData = {null};
        final JSONObject[] dataobjData = {null};
        // TO get device token of other user id

                try {
                    obj[0] = new JSONObject();
                    objData[0] = new JSONObject();

                    objData[0].put("title", SharedPrefrence.getUserNameFromJson(Comment_Chat.this));
                    objData[0].put("body",message);
                    objData[0].put("sound", "default");
                    objData[0].put("image", attachment); //   icon_name image must be there in drawable
                    objData[0].put("tag", device_token);
                    objData[0].put("priority", "high");
                    objData[0].put("click_action", "OPEN_ACTIVITY_1");
                    dataobjData[0] = new JSONObject();
                    dataobjData[0].put("title", SharedPrefrence.getUserNameFromJson(Comment_Chat.this));
                    dataobjData[0].put("text",message );
                    obj[0].put("to", device_token);
                    //obj.put("priority", "high");
                    obj[0].put("notification", objData[0]);
                    obj[0].put("data", dataobjData[0]);
                    Log.e("fdfsdfdsfg", obj[0].toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj[0],
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
                        params.put("Authorization", "key=" + Constants.FIREBASEKEY);
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Comment_Chat.this);
                int socketTimeout = 0;// 60 seconds
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                requestQueue.add(jsObjRequest);
            }




    }
