package com.geee.Inner_VP_Package.Notification_Pacakge.You_Package;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.geee.BackStack.RootFragment;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel.FollowReqModel;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.Interface.Fragment_CallBack;
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

public class Follow_Requests extends RootFragment {

    View view;
    ImageView ivRequest;
    RecyclerView follwersReq;
    Follow_Req_Adp followReqAdp;
    List<FollowReqModel> followReqLists = new ArrayList<>();
    Fragment_CallBack fragmentCallBack;
    RelativeLayout noDataLayout;
    Bundle bundle = new Bundle();

    String userId;

    public Follow_Requests(Fragment_CallBack fragmentCallBack) {
        this.fragmentCallBack = fragmentCallBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.follow_request, container, false);

        follwersReq = view.findViewById(R.id.follwers_request);
        noDataLayout = view.findViewById(R.id.no_data_layout);
        userId = SharedPrefrence.getUserIdFromJson(getContext());

        ivRequest = view.findViewById(R.id.iv_request);
        ImageView icBack = view.findViewById(R.id.ic_back);

        icBack.setOnClickListener(v -> getFragmentManager().popBackStack());

        methoSetUpAdapter();

        getUserFollwerReqWithUserId();

        return view;
    }

    // Method to get All Follw Request

    public void getUserFollwerReqWithUserId() {

        initVolleyCallback();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());

        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", userId);

            Functions.showLoader(getActivity(), false, false);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Show_Follow_Request, sendObj);

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
                Functions.cancelLoader();
                try {
                    JSONArray msgArr = response.getJSONArray("msg");
                    // Save Response for Offline Showing Data

                    if (msgArr.length() > 0) {
                        followReqLists.clear();
                        noDataLayout.setVisibility(View.GONE);
                        for (int i = 0; i < msgArr.length(); i++) {

                            JSONObject obj = msgArr.getJSONObject(i);
                            JSONObject followingReqListObj = obj.getJSONObject("FollowRequest");
                            JSONObject senderUserObj = obj.getJSONObject("Sender");
                            FollowReqModel reqModel = new FollowReqModel();

                            reqModel.request_id = "" + followingReqListObj.getString("id");
                            reqModel.user_name_follwer = "" + senderUserObj.getString("username");
                            reqModel.user_image_follwer = "" + senderUserObj.getString("image");
                          //  reqModel.user_full_name = "" + senderUserObj.getString("full_name");
                            reqModel.sender_id = "" + followingReqListObj.getString("sender_id");
                            reqModel.recever_id = "" + followingReqListObj.getString("receiver_id");
                           // reqModel.website = "" + senderUserObj.getString("website");
                           // reqModel.bio = "" + senderUserObj.getString("bio");

                            followReqLists.add(reqModel);
                        } // End for Loop
                    } else {
                        noDataLayout.setVisibility(View.VISIBLE);
                        followReqLists.clear();
                    }

                    followReqAdp.notifyDataSetChanged();

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

    private void methoSetUpAdapter() {
        followReqAdp = new Follow_Req_Adp(getContext(), followReqLists, (postion, model, view) -> {
            FollowReqModel followReqModel = (FollowReqModel) model;
            switch (view.getId()) {
                case R.id.delete_request:
                    methodCallApiForDeleteRequest(followReqModel);
                    break;

                case R.id.btn_id:
                    methodCallApiForFollowRequest(followReqModel);

                    break;

                case R.id.RL_main:
                    Intent myIntent = new Intent(getActivity(), View_user_Profile.class);
                    myIntent.putExtra("tictok",  "0"); //Optional parameters
                    myIntent.putExtra("user_id", followReqModel.getSender_id()); //Optional parameters
                    myIntent.putExtra("name", followReqModel.getUser_full_name()); //Optional parameters
                    myIntent.putExtra("username", followReqModel.getUser_name_follwer()); //Optional parameters
                    myIntent.putExtra("image", followReqModel.getUser_image_follwer()); //Optional parameters
                    myIntent.putExtra("bio", followReqModel.getWebsite()); //Optional parameters
                    myIntent.putExtra("website", followReqModel.getBio()); //Optional parameters
                    getActivity().startActivity(myIntent);
                    break;

                default:
                    break;
            }
        });

        follwersReq.setLayoutManager(new LinearLayoutManager(getContext()));
        follwersReq.setHasFixedSize(true);
        follwersReq.setAdapter(followReqAdp);
    }

    private void methodCallApiForFollowRequest(FollowReqModel followReqModel) {
        initVolleyCallbackFollowRequest();
        Variables.mVolleyService = new VolleyService(mResultCallback, getActivity());
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("sender_id", followReqModel.getSender_id());
            sendObj.put("receiver_id", followReqModel.getRecever_id());
            sendObj.put("status", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.logDMsg("params at followRequest : " + sendObj);

        Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Confirm_Follow_Request, sendObj);
    }

    private void initVolleyCallbackFollowRequest() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.logDMsg("response at followRequest : " + response);
                if (response.optString("code").equals("200")) {
                    getUserFollwerReqWithUserId();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    private void methodCallApiForDeleteRequest(FollowReqModel followReqModel) {
        initVolleyCallbackDelete();
        Variables.mVolleyService = new VolleyService(mResultCallback, getActivity());
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("id", followReqModel.getRequest_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.logDMsg("params at getProfile other : " + sendObj);

        Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_deleteFollowRequest, sendObj);

    }


    // Initialize Interface Call Backs
    void initVolleyCallbackDelete() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.logDMsg("response at getProfile other : " + response);
                if (response.optString("code").equals("200")) {
                    getUserFollwerReqWithUserId();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (fragmentCallBack != null) {
            bundle.putString("action", "done");
            fragmentCallBack.onItemClick(bundle);
        }
    }
}
