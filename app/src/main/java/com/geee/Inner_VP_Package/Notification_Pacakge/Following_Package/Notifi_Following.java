package com.geee.Inner_VP_Package.Notification_Pacakge.Following_Package;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Notification_Pacakge.DataModel.NotificationModel;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notifi_Following extends Fragment {

    View view;
    RecyclerView recyclerView;
    Notifi_Following_Adp notifiFollowingAdp;
    List<NotificationModel> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.notification_following, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_id);

        getFollowingNoti();

        return view;
    }


    public void getFollowingNoti() {
        initVolleyCallback();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {
            JSONObject sendObj = new JSONObject("{'user_id': '" + SharedPrefrence.getUserIdFromJson(getContext()) + "' }");
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_YOU_NOTIFICATION, sendObj);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.i("Dfdsfs",response.toString());
                try {
                    JSONObject msgObj = response.getJSONObject("msg");
                    // Following ArraY
                    JSONArray followingNotiArr = msgObj.getJSONArray("Following");
                    for (int ii = 0; ii < followingNotiArr.length(); ii++) {

                        JSONObject followingNotificationObj = followingNotiArr.getJSONObject(ii);
                        JSONObject notiObj = followingNotificationObj.getJSONObject("Notification");
                        JSONObject user = followingNotificationObj.getJSONObject("User");
                        JSONObject postObj = followingNotificationObj.getJSONObject("Post");

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
                    } // End For Loop


                    if(arrayList.isEmpty()){
                        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                    }else{
                        view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setHasFixedSize(false);
                    notifiFollowingAdp = new Notifi_Following_Adp(getContext(), pos -> {
                        NotificationModel notiGet = arrayList.get(pos);

                        ((MainActivity) getContext()).openPostDetail(notiGet.getPost_id(), notiGet.getUser_id());
                    }, arrayList);


                    recyclerView.setAdapter(notifiFollowingAdp);

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

}
