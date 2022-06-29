package com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
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
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.Adapters.PostDetailAdapter;
import com.geee.Inner_VP_Package.Home_Package.Comment_Chat;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Data_Model_Home;
import com.geee.Inner_VP_Package.Home_Package.Share_Bottom_Sheet.ShareBottomSheet;
import com.geee.Inner_VP_Package.Home_Package.Show_Post_Likes.PostLikesA;
import com.geee.Inner_VP_Package.OptionsBottomSheet;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.Interface.Fragment_CallBack;
import com.geee.R;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Item_Details extends Fragment {

    View view;
    List<Data_Model_Home> homeList = new ArrayList<>();
    RecyclerView verRv;
    PostDetailAdapter postDetailAdapter;
    ImageView icBack;
    String fromWhere, userId, postId;
    Fragment_CallBack fragmentCallBack;

    public Item_Details() {
        //required empty constructor
    }

    public Item_Details(Fragment_CallBack fragmentCallBack) {
        this.fragmentCallBack = fragmentCallBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.item_details, null);

        userId = getArguments().getString("user_id");
        postId = getArguments().getString("post_id");
        if (getArguments().getString("fragment") != null)
            fromWhere = getArguments().getString("fragment");

        verRv = view.findViewById(R.id.ver_RV_id);
        icBack = view.findViewById(R.id.back_id);

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

        icBack.setOnClickListener(v -> getFragmentManager().popBackStack());

        verRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        verRv.setHasFixedSize(false);

        methodSetUpAdapter();

        if (fromWhere != null && !fromWhere.equals("myPost")) {
            showPostDetail();
        } else {
            homeList = (List<Data_Model_Home>) getArguments().getSerializable("list");
            Functions.logDMsg("sendObj at api home homeList homeList :" + homeList.size());
            methodSetUpAdapter();
            postDetailAdapter.notifyDataSetChanged();
            for (int i = 0; i < homeList.size(); i++) {
                if (postId.equals(homeList.get(i).getId())) {
                    verRv.scrollToPosition(i);
                }
            }
        }

        return view;
    }


    private void methodSetUpAdapter() {
        postDetailAdapter = new PostDetailAdapter(getContext(), homeList, (postion, model, view) -> {
            Data_Model_Home dataModelHome = (Data_Model_Home) model;

            switch (view.getId()) {
                case R.id.comment_id:
                    methodOpenCommentScreen(dataModelHome);

                    break;

                case R.id.view_comment_id:
                    methodOpenCommentScreen(dataModelHome);
                    break;


                case R.id.share_id:
                    Bundle bundleLinearPosts = new Bundle();
                    bundleLinearPosts.putString("post_id", "" + postId);
                    ShareBottomSheet signUp = new ShareBottomSheet();
                    signUp.setArguments(bundleLinearPosts);
                    signUp.show(getActivity().getSupportFragmentManager(), signUp.getTag());
                    break;

                case R.id.likes_count_id:
                    Intent intent1 = new Intent(getActivity(), PostLikesA.class);
                    intent1.putExtra("post_id", dataModelHome.getId()); //Optional parameters
                    getActivity().startActivity(intent1);
                    break;

                case R.id.username_id:
                    methodOpenProfile(dataModelHome);
                    break;

                case R.id.prof_photo_id:
                    methodOpenProfile(dataModelHome);
                    break;

                case R.id.post_desc_id:
                    methodOpenCommentScreen(dataModelHome);
                    break;
                case R.id.username_comment:
                    methodOpenProfile(dataModelHome);
                    break;

                case R.id.menu_id:
                    OptionsBottomSheet bottomSheet = new OptionsBottomSheet("fromItem", bundle -> {
                        if (bundle.getString("option").equals("done")) {
                            showPostDetail();
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("post_id", dataModelHome.getId());
                    bundle.putString("userId", dataModelHome.getUser_id());
                    bundle.putString("button", dataModelHome.getFollowStatus());
                    bundle.putString("attachment", dataModelHome.getAttachment());
                    bundle.putString("caption", dataModelHome.getCaption());
                    bundle.putString("created", dataModelHome.getCreated());
                    bundle.putString("username", dataModelHome.getUser_name());
                    bundle.putString("profileImage", dataModelHome.getUser_img());
                    bottomSheet.setArguments(bundle);
                    bottomSheet.show((getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
                    break;
                default:
                    break;
            }

        });
        verRv.setAdapter(postDetailAdapter);

        postDetailAdapter.notifyDataSetChanged();
    }

    private void methodOpenCommentScreen(Data_Model_Home dataModelHome) {
        Intent myIntent = new Intent(getActivity(), Comment_Chat.class);
        myIntent.putExtra("post_id", dataModelHome.getId());
        myIntent.putExtra("device_token", dataModelHome.getdevice_token());
        myIntent.putExtra("attachment", Constants.BASE_URL +dataModelHome.getAttachment());
        Log.i("ffffffff",dataModelHome.getAttachment().toString());

        startActivity(myIntent);
    }

    private void methodOpenProfile(Data_Model_Home dataModelHome) {
        Intent myIntent = new Intent(getActivity(), View_user_Profile.class);
        myIntent.putExtra("tictok",  "0"); //Optional parameters

        myIntent.putExtra("user_id", dataModelHome.getUser_id()); //Optional parameters
        myIntent.putExtra("post_id", dataModelHome.getId()); //Optional parameters
        myIntent.putExtra("full_name", dataModelHome.getFull_name()); //Optional parameters
        myIntent.putExtra("username", dataModelHome.getUser_name()); //Optional parameters
        myIntent.putExtra("image", dataModelHome.getUser_img()); //Optional parameters
        getActivity().startActivity(myIntent);
    }

    public void showPostDetail() {
        initVolleyCallback();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {

            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", userId);
            sendObj.put("post_id", postId);
            sendObj.put("related", "");

            Functions.logDMsg("sendObj at api home search post :" + sendObj);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Home_Search_POSTS, sendObj);
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "" + v.toString());
        }


    }


    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.logDMsg("response at api home search post :" + response);
                try {
                    JSONArray msgArr = response.getJSONArray("msg");
                    homeList.clear();
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
                        dataModelHome.user_name = "" + user.getString("username");
                        dataModelHome.user_img = "" + user.getString("image");
                        dataModelHome.total_likes = "" + postObj.getString("total_likes");
                        dataModelHome.full_name = "" + user.getString("full_name");
                     //   dataModelHome.followStatus = "" + user.getString("button");
                     //   dataModelHome.commentCount = "" + postObj.getString("comment_count");

                        homeList.add(dataModelHome);
                    } // End for Loop

                    postDetailAdapter.notifyDataSetChanged();
                } catch (Exception v) {
                    v.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.toastMsg(getContext(), "Err " + error.toString());
            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();
        if (fromWhere != null && !fromWhere.equals("myPost")) {
            showPostDetail();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (postDetailAdapter.isSelected) {
            if (fragmentCallBack != null) {
                Bundle bundle = new Bundle();
                bundle.putString("action", "delete");
                fragmentCallBack.onItemClick(bundle);
            }
        }
    }


}
