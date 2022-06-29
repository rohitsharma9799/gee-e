package com.geee.Inner_VP_Package.Profile_Pacakge.SavedPackage;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity {
    RecyclerView savedRVId;
    Saved_Adapter savedAdapter;
    ImageView icBack;
    TextView savedText;
    List<Data_Model_Home> arrayList = new ArrayList<>();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        savedText = findViewById(R.id.saved_text);
        userId = SharedPrefrence.getUserIdFromJson(SavedActivity.this);
        icBack = findViewById(R.id.profileImage);

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

        savedText.setOnClickListener(v -> finish());

        icBack.setOnClickListener(v -> finish());


        savedRVId = findViewById(R.id.saved_RV_id);
        savedRVId.setLayoutManager(new GridLayoutManager(SavedActivity.this, 3));
        savedRVId.setHasFixedSize(false);

        methodSetUpAdapter();
        getSavedPosts();

    }

    public void getSavedPosts() {
        initVolleyCallback();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, SavedActivity.this);
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", userId);

            Log.i("dfsfsf",API_LINKS.API_Show_Book_Marked_POSTs+sendObj);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Show_Book_Marked_POSTs, sendObj);
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(SavedActivity.this, "" + v.toString());
        }
    }


    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {

                    Log.i("Fdgdg",response.toString());
                    if (response.getString("code").equals("200")) {
                        JSONArray msgArr = response.getJSONArray("msg");
                        // Save Response for Offline Showing Data

                        if (msgArr.length() == 0) {
                            findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                            arrayList.clear();
                            for (int i = 0; i < msgArr.length(); i++) {

                                JSONObject obj = msgArr.getJSONObject(i);
                                JSONObject postObj = obj.getJSONObject("Post");
                                JSONObject user = obj.getJSONObject("User");

                                Data_Model_Home dataModelHome = new Data_Model_Home();
                                dataModelHome.id = "" + postObj.getString("id");
                                if (postObj.has("caption")) {
                                    dataModelHome.caption = "" + postObj.getString("caption");
                                }
                               // dataModelHome.caption = "ok" ;
                                if (postObj.has("attachment")) {

                                    dataModelHome.attachment = "" + postObj.getString("attachment");
                                    //dataModelHome.location_string = "112.02" ;
                                }
                                dataModelHome.location_string = "" + postObj.getString("location_string");
                                dataModelHome.lat = "" + postObj.getString("lat");
                                dataModelHome.long_location = "" + postObj.getString("long");
                                dataModelHome.active = "" + postObj.getString("active");
                                dataModelHome.user_id = "" + postObj.getString("user_id");
                                dataModelHome.created = "" + postObj.getString("created");
                                dataModelHome.total_likes = "" + postObj.getString("total_likes");
                                dataModelHome.is_bookmark = "" + postObj.getString("PostBookmark");
                                dataModelHome.is_like = "" + postObj.getString("PostLike");
                                dataModelHome.user_name = "" + user.getString("username");
                                dataModelHome.user_img = "" + user.getString("image");
                                dataModelHome.full_name = "" + user.getString("full_name");
                                dataModelHome.commentCount = "" + postObj.getString("comment_count");

                                arrayList.add(dataModelHome);
                            } // End for Loop

                            savedAdapter.notifyDataSetChanged();
                        } // End else of checking array
                    } else {
                        arrayList.clear();
                        findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                    }
                } catch (Exception v) {
                    Functions.cancelLoader();
                    Functions.logDMsg("exception at saved activity :" + v.toString());
                }


            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.toastMsg(SavedActivity.this, "Err " + error.toString());
            }
        };
    }
    // ENd Data From API

    private void methodSetUpAdapter() {
        savedAdapter = new Saved_Adapter(arrayList, SavedActivity.this);
        savedRVId.setAdapter(savedAdapter);
    }

    public void openPostDetail(String postId, String userId) {
        Bundle bundleLinearPosts = new Bundle();
        bundleLinearPosts.putString("post_id", "" + postId);
        bundleLinearPosts.putString("user_id", "" + userId);
        bundleLinearPosts.putString("type", "showAllPosts");
        bundleLinearPosts.putString("fragment", "myPost");
        bundleLinearPosts.putSerializable("list", (Serializable) arrayList);
        Item_Details f = new Item_Details(bundle -> {
            if (bundle.getString("action").equals("delete")) {
                getSavedPosts();
            }
        });
        f.setArguments(bundleLinearPosts);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.main_container, f).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
