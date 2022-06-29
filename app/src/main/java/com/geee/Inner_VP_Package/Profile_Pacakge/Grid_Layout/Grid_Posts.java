package com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Data_Model_Home;
import com.geee.R;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Grid_Posts extends Fragment {

    View view;
    RecyclerView rv;
    Grid_Adapter adp;
    List<Data_Model_Home> postsList = new ArrayList<>();
    String userId;
    String formWhere;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.prof_grid_posts, null);
        rv = (RecyclerView) view.findViewById(R.id.prof_grid_rv_id);
        userId = getArguments().getString("user_id");
        formWhere = getArguments().getString("fromWhere");
        methodSetUpdata();
        getUserPostsMedia();
        return view;
    }

    public void getUserPostsMedia() {

        initVolleyCallback();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", userId);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_User_POSTS, sendObj);

        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "" + v.toString());
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
                    getUserPostsMedia();
                }
            }, 200);

        }
    }

    // Init Call Backs

    // init for User Posts Media ==> Class Name Grid_Posts.java ;-)

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {

                    if (response.optString("code").equals("200")) {
                        JSONArray msgArr = response.getJSONArray("msg");
                        postsList.clear();
                        for (int i = 0; i < msgArr.length(); i++) {
                            JSONObject obj = msgArr.getJSONObject(i);
                            JSONObject postObj = obj.getJSONObject("Post");
                            JSONObject user = obj.getJSONObject("User");
                            Data_Model_Home dataModelHome = new Data_Model_Home();
                            dataModelHome.id = "" + postObj.getString("id");
                            dataModelHome.caption = "" + postObj.getString("caption");
                            dataModelHome.attachment = "" + postObj.getString("attachment");
                            dataModelHome.location_string = "" + postObj.getString("location_string");
                            dataModelHome.lat = "" + postObj.getString("lat");
                            dataModelHome.long_location = "" + postObj.getString("long");
                            dataModelHome.active = "" + postObj.getString("active");
                            dataModelHome.user_id = "" + postObj.getString("user_id");
                            dataModelHome.created = "" + postObj.getString("created");
                            dataModelHome.is_bookmark = "" + postObj.getString("PostBookmark");
                            dataModelHome.is_like = "" + postObj.getString("PostLike");
                      //      dataModelHome.commentCount = "" + postObj.getString("comment_count");
                            dataModelHome.user_name = "" + user.getString("username");
                            dataModelHome.user_img = "" + user.getString("image");
                            dataModelHome.total_likes = "" + postObj.getString("total_likes");
                            dataModelHome.full_name = "" + user.getString("full_name");
                            postsList.add(dataModelHome);

                        } // End for Loop

                        if (postsList.isEmpty()) {
                            postsList.clear();
                            view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                        }

                        adp.notifyDataSetChanged();

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


    public void methodSetUpdata() {
        adp = new Grid_Adapter(pos -> {
            Bundle bundle = new Bundle();
            bundle.putString("post_id", "" + postsList.get(pos).getId());
            bundle.putString("user_id", "" + userId);
            bundle.putString("type", "showAllPosts");
            bundle.putString("fragment", "myPost");
            bundle.putSerializable("list", (Serializable) postsList);
            Item_Details f = new Item_Details();
            f.setArguments(bundle);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            if (formWhere.equals("profile"))
                ft.replace(R.id.main_container, f).commit();
            else
                ft.replace(R.id.prof_fl_id, f).commit();

        }, postsList, getContext());

        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv.setHasFixedSize(false);
        rv.setAdapter(adp);
    }


}