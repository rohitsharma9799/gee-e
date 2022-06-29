package com.geee.View_Tag;

import android.content.Intent;
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

public class View_Tag_Detail extends AppCompatActivity {
    RecyclerView recyclerView;
    View_Tag_Adp tagAdp;
    List<Data_Model_Home> tagPosts = new ArrayList<>();
    ImageView icBack;
    String search;
    TextView tagName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tag_detail);

        icBack = findViewById(R.id.ic_back);
        // Getting user id from previous section
        Intent intent = getIntent();
        search = intent.getStringExtra("search");
        tagName = findViewById(R.id.tag_name);

        icBack.setOnClickListener(v -> {
            finish();
        });

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


        tagName.setText("" + search);

        recyclerView = findViewById(R.id.prof_grid_rv_id);

        getUserPostsMedia(SharedPrefrence.getUserIdFromJson(View_Tag_Detail.this));

    }


    public void getUserPostsMedia(String userId) {

        initVolleyCallback();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, View_Tag_Detail.this);

        Functions.showLoader(View_Tag_Detail.this, false, false);

        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", userId);
            sendObj.put("keyword", search);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Search_Tag_Post, sendObj);
            Functions.logDMsg("sendObj at viewtag detail : " + sendObj.toString());
        } catch (Exception v) {
            v.printStackTrace();

        }
    }

    // Init Call Backs

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                Functions.logDMsg("sendObj at viewtag detail response : " + response.toString());
                Log.i("fdfgdfgdg",response.toString());
                try {
                    JSONObject  jsonObject = new JSONObject(String.valueOf(response));

                    JSONArray msgArr = jsonObject.getJSONArray("msg");

                    for (int i = 0; i < msgArr.length(); i++) {
                        JSONObject obj = msgArr.getJSONObject(i);
                        JSONObject postObj = obj.getJSONObject("Post");
                        JSONObject user = obj.getJSONObject("User");
                        Data_Model_Home dataModelHome = new Data_Model_Home();
                        dataModelHome.id = "" + postObj.getString("id");
                        dataModelHome.caption = "" + postObj.getString("caption");
                        dataModelHome.attachment = "" + postObj.getString("attachment");
                      //  postObj.isNull("location_string");
                         //   dataModelHome.location_string = "" + postObj.getString("location_string");


                        dataModelHome.lat = "" + postObj.getString("lat");
                        dataModelHome.long_location = "" + postObj.getString("long");
                        dataModelHome.active = "" + postObj.getString("active");
                        dataModelHome.user_id = "" + postObj.getString("user_id");
                        dataModelHome.created = "" + postObj.getString("created");
                      //  dataModelHome.is_bookmark = "" + postObj.getString("PostBookmark");
                     //   dataModelHome.is_like = "" + postObj.getString("PostLike");
                        dataModelHome.user_name = "" + user.getString("username");
                        dataModelHome.user_img = "" + user.getString("image");
                        //dataModelHome.total_likes = "" + postObj.getString("total_likes");
                        dataModelHome.full_name = "" + user.getString("full_name");
                       // dataModelHome.commentCount = "" + postObj.getString("comment_count");
                        tagPosts.add(dataModelHome);
                    } // End for Loop

                    tagAdp = new View_Tag_Adp(View_Tag_Detail.this, tagPosts, new View_Tag_Adp.onclick() {
                        @Override
                        public void onitem(int pos, Object model, View view) {
                            Bundle bundle = new Bundle();
                            bundle.putString("post_id", "" + tagPosts.get(pos).getId());
                            bundle.putString("user_id", "" + SharedPrefrence.getUserImageFromJson(getApplicationContext()));
                            bundle.putString("type", "showAllPosts");
                            bundle.putString("fragment", "myPost");
                            bundle.putSerializable("list", (Serializable) tagPosts);
                            Item_Details f = new Item_Details();
                            f.setArguments(bundle);
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.addToBackStack(null);
                            ft.replace(R.id.tagContainer, f).commit();
                        }
                    });

                    recyclerView.setLayoutManager(new GridLayoutManager(View_Tag_Detail.this, 3));
                    recyclerView.setHasFixedSize(false);

                    recyclerView.setAdapter(tagAdp);

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


}
