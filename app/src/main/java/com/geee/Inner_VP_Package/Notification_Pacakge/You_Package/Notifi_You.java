package com.geee.Inner_VP_Package.Notification_Pacakge.You_Package;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.BackStack.RootFragment;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Notification_Pacakge.DataModel.NotificationModel;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notifi_You extends RootFragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    RecyclerView recyclerView;
    Notification_Adapters notificationAdapters;
    ImageView ivRequest;
    RelativeLayout followReqRL;
    TextView tvRequestCount;
    List<NotificationModel> arrayList = new ArrayList<>();
    SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.notification_you, null);

        recyclerView = view.findViewById(R.id.rv_id);
        followReqRL = view.findViewById(R.id.rl_id);
        ivRequest = view.findViewById(R.id.iv_request);
        tvRequestCount = view.findViewById(R.id.tv_request);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        followReqRL.setOnClickListener(v -> {
            // Open All Comments
            Follow_Requests f = new Follow_Requests(bundle -> {
                if (bundle != null) {
                    if (bundle.getString("action").equals("done")) {
                        getYOUNoti();
                        getUserFollwerReqWithUserId();
                    }
                }
            });
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.main_container, f).commit();
        });

        methodSetUpAdapter();
        getYOUNoti();
        getUserFollwerReqWithUserId();

        return view;
    }


    //Calling AN API to get YOU Notifications

    public void getYOUNoti() {
        initVolleyCallback();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getActivity());
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getActivity()));
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_YOU_NOTIFICATION, sendObj);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    @Override
    public void setMenuVisibility(boolean isVisibleToUser) {
        super.setMenuVisibility(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getYOUNoti();
                    getUserFollwerReqWithUserId();
                }
            }, 200);
        }
    }

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                swipeRefresh.setRefreshing(false);
                arrayList.clear();
                try {
                 //   if (response.getJSONObject("code").equals(200)){
                        JSONObject msgObj = response.getJSONObject("msg");
                        JSONArray msgObjJSONArray = msgObj.getJSONArray("You");

                        for (int i = 0; i < msgObjJSONArray.length(); i++) {

                            JSONObject object = msgObjJSONArray.getJSONObject(i);
                            JSONObject notiObj = object.getJSONObject("Notification");
                            JSONObject user = object.getJSONObject("User");
                            JSONObject postObj = object.getJSONObject("Post");


                            NotificationModel model = new NotificationModel();

                            model.id = "" + notiObj.getString("id");
                            model.user_id = "" + notiObj.getString("user_id");
                            model.post_id = "" + notiObj.getString("post_id");
                            model.post_user_id = "" + notiObj.getString("post_user_id");
                            model.noti_msg = "" + notiObj.getString("msg");
                            model.user_name = "" + user.getString("username");
                            model.user_img = "" + user.getString("image");
                            model.created = "" + notiObj.getString("created");
                            model.attachment = "" + postObj.getString("attachment");

                            arrayList.add(model);
                        } // End for Loop
                        notificationAdapters.notifyDataSetChanged();


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


    private void methodSetUpAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);

        notificationAdapters = new Notification_Adapters(pos -> {
            NotificationModel noti_get = arrayList.get(pos);
            ((MainActivity) getActivity()).openPostDetail(noti_get.getPost_id(), "" + noti_get.getUser_id());
        }, arrayList, getActivity());
        recyclerView.setAdapter(notificationAdapters);

    }

    public void getUserFollwerReqWithUserId() {

        initVolleyCallbackRequest();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getActivity());

        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getActivity()));
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Show_Follow_Request, sendObj);

        } catch (Exception v) {
            v.printStackTrace();
            Functions.logDMsg("notification error : " + v.toString());
        }
    } // End method to getfollwer

    // Initialize Interface Call Backs
    void initVolleyCallbackRequest() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    JSONArray msgArr = response.getJSONArray("msg");
                    // Save Response for Offline Showing Data
                    if (msgArr.length() > 0) {
                        view.findViewById(R.id.empty_view_container).setVisibility(View.GONE);
                        followReqRL.setVisibility(View.VISIBLE);
                        for (int i = 0; i < msgArr.length(); i++) {
                            tvRequestCount.setText("" + msgArr.length());
                            JSONObject obj = msgArr.getJSONObject(0);
                            JSONObject senderUserObj = obj.getJSONObject("Sender");
                            String imgUrl = senderUserObj.getString("image");
                            Functions.logDMsg("response at follow imgUrl :" + imgUrl);
                            if (imgUrl != null && !imgUrl.equals(""))
                               // ivRequest.setImageURI(Uri.parse(Constants.BASE_URL + imgUrl));
                                Picasso.get().load(Constants.BASE_URL + imgUrl)
                                        .placeholder(R.drawable.profile_image_placeholder)
                                        .into(ivRequest);
                        }
                    } else {
                        followReqRL.setVisibility(View.GONE);
                        if (arrayList.isEmpty()) {
                            arrayList.clear();
                            view.findViewById(R.id.empty_view_container).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.empty_view_container).setVisibility(View.GONE);
                        }
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

    @Override
    public void onRefresh() {
        getYOUNoti();
        getUserFollwerReqWithUserId();
    }

}
