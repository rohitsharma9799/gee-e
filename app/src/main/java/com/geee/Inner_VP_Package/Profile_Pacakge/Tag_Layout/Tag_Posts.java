package com.geee.Inner_VP_Package.Profile_Pacakge.Tag_Layout;

import android.os.Bundle;
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
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.Item_Details;
import com.geee.R;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tag_Posts extends Fragment {

    View view;
    RecyclerView recyclerView;
    Tag_Adapter adp;
    List<Data_Model_Home> postsList = new ArrayList<>();
    String userId , fromWhere;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.prof_tag_posts, null);
        userId = getArguments().getString("user_id");
        fromWhere = getArguments().getString("fromWhere");
        recyclerView =  view.findViewById(R.id.prof_tag_rv_id);
        methodSetupAdapter();
        getUserPostsMedia(userId);

        return view;
    }


    public void getUserPostsMedia(String userId) {
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

    // Init Call Backs

    //User Posts Media ==> Class Name Grid_Posts.java ;-)

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

                            postObj.getString("id");
                            postObj.getString("caption");
                            postObj.getString("attachment");
                            postObj.getString("location_string");
                            postObj.getString("lat");
                            postObj.getString("long");
                            postObj.getString("active");
                            postObj.getString("user_id");
                            postObj.getString("created");


                            Data_Model_Home gridDataModel = new Data_Model_Home();

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
                            gridDataModel.commentCount = "" + postObj.getString("comment_count");
                            gridDataModel.is_like = "" + postObj.getString("PostLike");
                            gridDataModel.user_name = "" + user.getString("username");
                            gridDataModel.user_img = "" + user.getString("image");
                            gridDataModel.total_likes = "" + postObj.getString("total_likes");

                            postsList.add(gridDataModel);
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
                    Functions.cancelLoader();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    public void methodSetupAdapter() {
        adp = new Tag_Adapter(pos -> {
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
            if (fromWhere.equals("profile"))
                ft.replace(R.id.main_container, f).commit();
            else
                ft.replace(R.id.prof_fl_id,f).commit();
        }, postsList, getContext());

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adp);
    }

}