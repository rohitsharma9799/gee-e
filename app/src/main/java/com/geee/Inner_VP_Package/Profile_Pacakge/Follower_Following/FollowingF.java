package com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.geee.CodeClasses.API_LINKS;
import com.geee.Interface.AdapterClickListener;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel.FollwerDataModel;
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

public class FollowingF extends Fragment {

    View view;
    RecyclerView rv;
    Following_Adp adp;
    EditText etSearch;
    ImageView ivCross;
    List<FollwerDataModel> followingLists = new ArrayList<>();
    String userId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.following, null);

        userId = getArguments().getString("user_id");
        Log.i("fdfgfdgd",userId);
        rv =  view.findViewById(R.id.rv_id);
        etSearch =  view.findViewById(R.id.et_search_product);
        ivCross =  view.findViewById(R.id.iv_cross);
        ivCross.setVisibility(View.INVISIBLE);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //auto generated method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = etSearch.length();
                if (len > 0) {
                    filter(s.toString());
                    ivCross.setVisibility(View.VISIBLE);
                    ivCross.setOnClickListener(v -> {
                        etSearch.setText("");
                        filter("");
                    });
                } else {
                    filter("");
                    ivCross.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //auto generated method
            }
        });

        setUpAdapter();
        getUserFollwerWithUserId("" + userId);


        return view;
    }

    public void getUserFollwerWithUserId(String userId) {

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
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Show_following_list, sendObj);

            Log.i("fdgdg",API_LINKS.API_Show_following_list+sendObj);
            Functions.logDMsg("params at following request : "+sendObj);
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "" + v.toString());
        }


    } // End method to getfollwer

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    JSONArray msgArr = response.getJSONArray("msg");
                    followingLists.clear();
                    for (int i = 0; i < msgArr.length(); i++) {

                        JSONObject obj = msgArr.getJSONObject(i);
                        JSONObject followingList = obj.getJSONObject("FollowingList");

                        FollwerDataModel follwerDataModel = new FollwerDataModel();
                        follwerDataModel.user_id_follower = "" + followingList.getString("id");
                        follwerDataModel.user_name_follwer = "" + followingList.getString("username");
                        follwerDataModel.user_image_follwer = "" + followingList.getString("image");
                        follwerDataModel.user_full_name = "" + followingList.getString("full_name");
                        follwerDataModel.website = "" + followingList.getString("website");
                        follwerDataModel.bio = "" + followingList.getString("bio");
                        follwerDataModel.followstatus = "" + followingList.getString("button");

                        followingLists.add(follwerDataModel);
                    } // End for Loop

                    if (followingLists.isEmpty()) {
                        followingLists.clear();
                        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                    } else {
                        view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    }

                    adp.notifyDataSetChanged();


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

    private void setUpAdapter(){
        adp = new Following_Adp(getContext(), followingLists, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {
                FollwerDataModel dataModel = (FollwerDataModel) model;
                switch (view.getId()) {
                    case R.id.profile_rlt:
                        Intent myIntent = new Intent(getActivity(), View_user_Profile.class);
                        myIntent.putExtra("tictok",  "0"); //Optional parameters

                        myIntent.putExtra("user_id", dataModel.getUser_id_follower());
                        myIntent.putExtra("full_name", dataModel.getUser_full_name());
                        myIntent.putExtra("username", dataModel.getUser_name_follwer());
                        myIntent.putExtra("image", dataModel.getUser_image_follwer());
                        myIntent.putExtra("website", dataModel.website);
                        myIntent.putExtra("bio", dataModel.bio);
                        getActivity().startActivity(myIntent);
                        break;

                    case R.id.btn_id:
                        if (dataModel.followstatus.equalsIgnoreCase("follow")) {
                            followRequest(SharedPrefrence.getUserIdFromJson(getActivity()), dataModel.getUser_id_follower());
                        }

                        break;

                    default:
                        break;
                }
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);

        rv.setAdapter(adp);
    }


    // Follow request
    public void followRequest(String userSenderId, String userReceiverId) {
        initVolleyCallbackForFollow();
        Variables.mVolleyService = new VolleyService(mResultCallback, getActivity());
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("sender_id", userSenderId);
            sendObj.put("receiver_id", userReceiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.logDMsg("params at follow bottom : " + sendObj);
        Functions.showLoader(getActivity(),false,false);
        Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Send_Follow_Request, sendObj);
    }

    // Initialize Interface Call Backs
    void initVolleyCallbackForFollow() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.logDMsg("params at follow bottom : " + response);

                Functions.cancelLoader();
                try {
                    if (response.getString("code").equals("200")) {
                        getUserFollwerWithUserId(userId);
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

    void filter(String text) {
        List<FollwerDataModel> temp = new ArrayList();
        if (!text.equals("")) {
            for (FollwerDataModel d : followingLists) {
                if (d.getUser_name_follwer().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
        } else {
            temp.addAll(followingLists);
        }
        //update recyclerview
        adp.updateList((ArrayList<FollwerDataModel>) temp);
    }

}
