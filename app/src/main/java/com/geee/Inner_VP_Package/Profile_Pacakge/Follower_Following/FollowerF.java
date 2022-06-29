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

public class FollowerF extends Fragment {

    View view;
    RecyclerView rv;
    Follower_Adp adp;
    EditText et;
    ImageView btn;
    List<FollwerDataModel> follwerLists = new ArrayList<>();
    String userId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.follower, null);

        userId = getArguments().getString("user_id");

        rv = view.findViewById(R.id.rv_id);
        et = view.findViewById(R.id.et_search_product);
        btn = view.findViewById(R.id.iv_cross);

        btn.setVisibility(View.INVISIBLE);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //auto generated method
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                int len = et.length();
                if (len > 0) {
                    filter(s.toString());
                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(v -> {
                        et.setText("");
                        filter("");
                    });
                } else {
                    btn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //auto generated method
            }
        });
        methodSetUpAdapter();
        getUserFollwerWithUserId("" + userId);

        return view;
    }

    // Get follwer from User ID

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

            Functions.showLoader(getActivity(), false, false);

            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_FOLLWER_LIST, sendObj);

            Log.i("DFsdfs",API_LINKS.API_FOLLWER_LIST+sendObj);

        } catch (Exception v) {
            v.printStackTrace();
        }


    } // End method to getfollwer

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                try {
                    JSONArray msgArr = response.getJSONArray("msg");
                    // Save Response for Offline Showing Data
                    follwerLists.clear();
                    for (int i = 0; i < msgArr.length(); i++) {

                        JSONObject obj = msgArr.getJSONObject(i);
                        JSONObject followerList = obj.getJSONObject("FollowerList");

                        FollwerDataModel follwerDataModel = new FollwerDataModel();
                        follwerDataModel.user_id_follower = "" + followerList.getString("id");
                        follwerDataModel.user_name_follwer = "" + followerList.getString("username");
                        follwerDataModel.user_image_follwer = "" + followerList.getString("image");
                        follwerDataModel.user_full_name = "" + followerList.getString("full_name");
                        follwerDataModel.website = "" + followerList.getString("website");
                        follwerDataModel.bio = "" + followerList.getString("bio");
                        follwerDataModel.followstatus = "" + followerList.getString("button");

                        follwerLists.add(follwerDataModel);
                    } // End for Loop

                    if (follwerLists.isEmpty()) {
                        follwerLists.clear();
                        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                    } else {
                        view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    }
                   adp.notifyDataSetChanged();

                } catch (Exception v) {
                    Functions.cancelLoader();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.cancelLoader();
            }
        };
    }
    // ENd Data From API


    private void methodSetUpAdapter(){
        adp = new Follower_Adp(getContext(), follwerLists, (postion, model, view) -> {
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

    // End follow Request


    void filter(String text) {
        List<FollwerDataModel> temp = new ArrayList();

        for (FollwerDataModel d : follwerLists) {
            if (d.getUser_name_follwer().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        adp.updateList((ArrayList<FollwerDataModel>) temp);
    }


}
