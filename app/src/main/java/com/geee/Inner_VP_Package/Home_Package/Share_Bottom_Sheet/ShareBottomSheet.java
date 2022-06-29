package com.geee.Inner_VP_Package.Home_Package.Share_Bottom_Sheet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Home_Package.Adapters.SharePostAdp;
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel.FollwerDataModel;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShareBottomSheet extends BottomSheetDialogFragment {

    View view;
    String userInfoJson;
    EditText search;
    RecyclerView recyclerView;
    SharePostAdp follwersAdp;
    List<FollwerDataModel> follwerLists = new ArrayList<>();
    String postId;
    private List<Integer> selectedIds = new ArrayList<>();

    public ShareBottomSheet() {
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_sheet_share_post, container, false);

        postId = getArguments().getString("post_id");


        userInfoJson = SharedPrefrence.getOffline(getContext(), SharedPrefrence.shared_user_login_detail_key);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_id);
        search = view.findViewById(R.id.search_friends);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //auto generated
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //auto generated
            }
        });

        getUserFollwerWithUserId("" + SharedPrefrence.getUserIdFromJson(getContext()));


        return view;
    }

    void filter(String text) {
        List<FollwerDataModel> temp = new ArrayList();
        for (FollwerDataModel d : follwerLists) {
            if (d.getUser_name_follwer().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        follwersAdp.updateList((ArrayList<FollwerDataModel>) temp);

    }

    // Get follwer from User ID
    public void getUserFollwerWithUserId(String userId) {
        initVolleyCallback();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", userId);

            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_FOLLWER_LIST, sendObj);
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "" + v.toString());
        }
    }

    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                if (response.optString("code").equals("200")) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    try {
                        JSONArray msgArr = response.getJSONArray("msg");
                        // Save Response for Offline Showing Data

                        for (int i = 0; i < msgArr.length(); i++) {
                            JSONObject obj = msgArr.getJSONObject(i);
                            JSONObject followerListObj = obj.getJSONObject("FollowerList");
                            FollwerDataModel follwerDataModel = new FollwerDataModel();
                            follwerDataModel.user_id_follower = "" + followerListObj.getString("id");
                            follwerDataModel.user_name_follwer = "" + followerListObj.getString("username");
                            follwerDataModel.user_image_follwer = "" + followerListObj.getString("image");
                            follwerDataModel.user_full_name = "" + followerListObj.getString("full_name");
                            follwerLists.add(follwerDataModel);
                        } // End for Loop

                        follwersAdp = new SharePostAdp(getContext(), follwerLists, postId);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(follwersAdp);


                    } catch (Exception v) {
                        v.printStackTrace();
                    }
                } else {
                    follwerLists.clear();
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.toastMsg(getContext(), "Err " + error.toString());

            }
        };
    }

}
