package com.geee.Inner_VP_Package.Profile_Pacakge.Linear_Layout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.geee.BackStack.RootFragment;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.Comment_Chat;
import com.geee.Inner_VP_Package.OptionsBottomSheet;
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.DataModel.Grid_Data_Model;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPostLinear extends RootFragment {

    View view;
    RecyclerView recyclerView;
    Linear_Adapter linearAdapter;
    List<Grid_Data_Model> postsList = new ArrayList<>();
    String userId, fromWhere;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.prof_linear_posts, null);
        recyclerView = view.findViewById(R.id.prof_linear_rv_id);
        userId = getArguments().getString("user_id");
        fromWhere = getArguments().getString("fromWhere");
        setUpAdapter();
        getUserPosts();
        return view;
    }

    public void getUserPosts() {
        initVolleyCallback();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {
            JSONObject sendObj = new JSONObject();

            if (userId.equals(SharedPrefrence.getUserIdFromJson(getActivity()))) {
                sendObj.put("user_id", userId);
            } else {
                sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getActivity()));
                sendObj.put("other_user_id", userId);
            }

            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_User_POSTS, sendObj);
            Functions.logDMsg("userId at user post :" + sendObj);

        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "Linear " + v.toString());
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getUserPosts();
                }
            }, 200);

        }
    }

    // Init Call Backs
    // Initialize Interface Call Backs
    void initVolleyCallback() {
        ArrayList<Grid_Data_Model> tempList = new ArrayList<>();
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.logDMsg("response at user post :" + response);
                try {

                    if (response.optString("code").equals("200")) {
                        view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);

                        JSONArray msgArr = response.getJSONArray("msg");

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
                          //  gridDataModel.follow = "" + user.getString("button");
                            gridDataModel.devicetoken = "" + user.getString("device_token");
                            tempList.add(gridDataModel);
                        } // End for Loop

                        if (tempList.isEmpty()) {
                            postsList.clear();
                            view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                            postsList.clear();
                            postsList.addAll(tempList);
                        }
                        linearAdapter.notifyDataSetChanged();
                    } else {
                        postsList.clear();
                        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                    }

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

    void setUpAdapter() {
        linearAdapter = new Linear_Adapter(postsList, getContext(), (postion, model, view) -> {
            Grid_Data_Model modelUserPosts = (Grid_Data_Model) model;
            switch (view.getId()) {
                case R.id.view_comment_id:
                    Intent myIntent = new Intent(getActivity(), Comment_Chat.class);
                    myIntent.putExtra("post_id", modelUserPosts.getId());
                    myIntent.putExtra("device_token", modelUserPosts.getdevice_token());
                    myIntent.putExtra("attachment", Constants.BASE_URL +modelUserPosts.getAttachment());
                    getActivity().startActivity(myIntent);
                    break;

                case R.id.comment_id:
                    Intent intent = new Intent(getActivity(), Comment_Chat.class);
                    intent.putExtra("post_id", modelUserPosts.getId());
                    intent.putExtra("device_token", modelUserPosts.getdevice_token());
                    intent.putExtra("attachment", Constants.BASE_URL +modelUserPosts.getAttachment());
                    getActivity().startActivity(intent);
                    break;

                case R.id.tvViewComment:
                    Intent intent1 = new Intent(getActivity(), Comment_Chat.class);
                    intent1.putExtra("post_id", modelUserPosts.getId());
                    getActivity().startActivityForResult(intent1, 1);
                    break;


                case R.id.menu_id:
                    OptionsBottomSheet bottomSheet = new OptionsBottomSheet("fromProfile", bundle -> {
                        if (bundle.getString("option").equals("done")) {
                            getUserPosts();
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("post_id", modelUserPosts.getId());
                    bundle.putString("userId", userId);
                    bundle.putString("button", modelUserPosts.getFollow());
                    bundle.putString("attachment", modelUserPosts.getAttachment());
                    bundle.putString("caption", modelUserPosts.getCaption());
                    bundle.putString("created", modelUserPosts.getCreated());
                    bundle.putString("username", modelUserPosts.getUser_name());
                    bundle.putString("profileImage", modelUserPosts.getUser_img());
                    bundle.putString("fromWhichActivity", fromWhere);
                    bottomSheet.setArguments(bundle);
                    bottomSheet.show((getActivity()).getSupportFragmentManager(), bottomSheet.getTag());

                    break;

                default:
                    break;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(linearAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserPosts();
    }
}