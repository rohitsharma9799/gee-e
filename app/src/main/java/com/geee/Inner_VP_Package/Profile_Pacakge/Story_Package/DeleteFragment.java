package com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Home_Package.Adapters.Comment_Adp;
import com.geee.Inner_VP_Package.Home_Package.Comment_Chat;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Comment_Data_Model;
import com.geee.Inner_VP_Package.Home_Package.Home;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Utils.MyApplication;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.geee.tictokcode.Comments.Comment_Get_Set;
import com.geee.tictokcode.Comments.Comments_Adapter;
import com.geee.tictokcode.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.geee.tictokcode.SimpleClasses.API_CallBack;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Fragment_Data_Send;
import com.geee.tictokcode.SimpleClasses.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteFragment extends RootFragment {

    DeleteFragment deleteFragment;
    View view;
    Context context;

    RecyclerView recyclerView;
    public DeleteFragment() {

    }
    Fragment_Data_Send fragment_data_send;
    @SuppressLint("ValidFragment")
    public DeleteFragment(int count, Fragment_Data_Send fragment_data_send){
    }
    ArrayList<Story_DataModel> userStory = new ArrayList<>();

    RecyclerView delerecycler;
    ImageView ic_back;
    DeleteStoryAdapter deleteStoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.deletefragment, container, false);
        context=getActivity();
        delerecycler = view.findViewById(R.id.delerecycler);
        ic_back = view.findViewById(R.id.ic_back);

        delerecycler.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(),1,GridLayoutManager.VERTICAL,false);
        delerecycler.setLayoutManager(gridLayoutManager3);
        delerecycler.setItemAnimator(new DefaultItemAnimator());
        fetchplan();
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back();
            }
        });
        return view;
    }
    public void back(){
        Intent sendIntent = new Intent(context, MainActivity.class);
        context.startActivity(sendIntent);
        /*Home geeeFeed = new Home();
        geeeFeed.getStories();
        ((MainActivity)getActivity()).onBackPressed();*/
    }
    private void fetchplan() {
     //   SharedPreferences sharedpreferences = getSharedPreferences(RootURL.PREFACCOUNT, MODE_PRIVATE);
        final JSONObject json1 = new JSONObject();
        try {
            json1.put("user_id", SharedPrefrence.getUserIdFromJson(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url = API_LINKS.API_SHOW_USER_STORY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, url, json1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("gfgfgfgfgfg", response.toString());
                            Log.i("gdfgvgbnn", url+json1.toString());
                            JSONArray msgArr = response.getJSONArray("msg");

                            for (int i = 0; i < msgArr.length(); i++) {

                                JSONObject obj = msgArr.getJSONObject(i);
                                JSONObject storyObj = obj.getJSONObject("Story");
                                Story_DataModel post = new Story_DataModel();
                                post.image_url = "" + storyObj.getString("attachment");
                                post.user_id = "" + storyObj.getString("id");
                                post.created = "" + storyObj.getString("created");
                                post.type = "" + storyObj.getString("type");
                                userStory.add(post);
                            }
                            DeleteStoryAdapter donationAdapter=new DeleteStoryAdapter(getActivity(),userStory,DeleteFragment.this);
                            donationAdapter.notifyDataSetChanged();
                            delerecycler.setAdapter(donationAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                Log.i("Dfsdfsdf",error.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //imageEncoded=getStringImage(bitmap);
                //Log.i("image", encodedImageList.toString());
                Map<String, String> params = new Hashtable<String, String>();
                //params.put("imagefile", encodedImageList.toString());
                // image = getStringImage(bitmap);
                //  System.out.print("Check" + image);
                //params.put("mobile", "9799569458");
                // params.put("",json1.toString());
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }
    @Override
    public void onDetach() {
        Functions.hideSoftKeyboard(getActivity());
        super.onDetach();
    }

    private void methodSetUpAdapter() {

    }
}
