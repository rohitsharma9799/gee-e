package com.geee.tictokcode.Comments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.geee.Inner_VP_Package.Main_F;
import com.geee.R;
import com.geee.tictokcode.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.geee.tictokcode.SimpleClasses.API_CallBack;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Fragment_Data_Send;
import com.geee.tictokcode.SimpleClasses.Functions;
import com.geee.tictokcode.SimpleClasses.Variables;

/**
 * A simple {@link Fragment} subclass.
 */
public class Comment_F extends RootFragment {

    View view;
    Context context;

    RecyclerView recyclerView;

    Comments_Adapter adapter;

    ArrayList<Comment_Get_Set> data_list;

    String video_id;
    String user_id,token;

    EditText message_edit;
    ImageButton send_btn;
    ProgressBar send_progress;

    TextView comment_count_txt;

    FrameLayout comment_screen;

    public static int comment_count=0;
    public Comment_F() {

    }

    Fragment_Data_Send fragment_data_send;
    @SuppressLint("ValidFragment")
    public Comment_F(int count, Fragment_Data_Send fragment_data_send){
        comment_count=count;
        this.fragment_data_send=fragment_data_send;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_comment, container, false);
        context=getContext();


        comment_screen=view.findViewById(R.id.comment_screen);
        comment_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Main_F.bottombarid.setVisibility(View.VISIBLE);
                getActivity().onBackPressed();

            }
        });

        view.findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_F.bottombarid.setVisibility(View.VISIBLE);
                FragmentManager mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentManager fragmentManager;
                if (mFragmentManager.getBackStackEntryCount() > 0)
                    mFragmentManager.popBackStackImmediate();
                else Comment_F.super.onBackPressed();
            }
        });


        Bundle bundle=getArguments();
        if(bundle!=null){
            video_id=bundle.getString("video_id");
            user_id=bundle.getString("user_id");
            token=bundle.getString("token");

        }


        comment_count_txt=view.findViewById(R.id.comment_count);

        recyclerView=view.findViewById(R.id.recylerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);


        data_list=new ArrayList<>();
        adapter=new Comments_Adapter(context, data_list, new Comments_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, Comment_Get_Set item, View view) {


            }
        });

        recyclerView.setAdapter(adapter);


        message_edit=view.findViewById(R.id.message_edit);


        send_progress=view.findViewById(R.id.send_progress);
        send_btn=view.findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=message_edit.getText().toString();
                if(!TextUtils.isEmpty(message)){
                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                        Send_Comments(video_id,message);
                        message_edit.setText(null);
                        send_progress.setVisibility(View.VISIBLE);
                        send_btn.setVisibility(View.GONE);
                       // sendFCMPush(token,message);
                    }
                    else {
                        Toast.makeText(context, "Please Login into the app", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });




        Get_All_Comments();


        return view;
    }


    @Override
    public void onDetach() {
        Functions.hideSoftKeyboard(getActivity());
        super.onDetach();
       /* FragmentManager mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentManager fragmentManager;
        if (mFragmentManager.getBackStackEntryCount() > 0)
            mFragmentManager.popBackStackImmediate();
        else Comment_F.super.onBackPressed();*/
    }

    // this funtion will get all the comments against post
    public void Get_All_Comments(){

        Functions.Call_Api_For_get_Comment(getActivity(), video_id, new API_CallBack() {
            @Override
            public void ArrayData(ArrayList arrayList) {
                Log.i("dfsdfsdfsdf",arrayList.toString());
                ArrayList<Comment_Get_Set> arrayList1=arrayList;
                for(Comment_Get_Set item:arrayList1){
                    data_list.add(item);
                }
                comment_count_txt.setText(data_list.size()+" comments");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void OnSuccess(String responce) {

            }

            @Override
            public void OnFail(String responce) {

            }

        });

    }




    // this function will call an api to upload your comment
    public void Send_Comments(String video_id, final String comment){

        Functions.Call_Api_For_Send_Comment(getActivity(), video_id,comment ,new API_CallBack() {
            @Override
            public void ArrayData(ArrayList arrayList) {
                send_progress.setVisibility(View.GONE);
                send_btn.setVisibility(View.VISIBLE);

                ArrayList<Comment_Get_Set> arrayList1=arrayList;
                for(Comment_Get_Set item:arrayList1){
                    data_list.add(0,item);
                    comment_count++;

                    SendPushNotification(getActivity(),user_id,comment);

                    comment_count_txt.setText(comment_count+" comments");

                    if(fragment_data_send!=null)
                        fragment_data_send.onDataSent(""+comment_count);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void OnSuccess(String responce) {

            }

            @Override
            public void OnFail(String responce) {

            }
        });

    }



    public  void SendPushNotification(Activity activity,String user_id,String comment){

        JSONObject notimap= new JSONObject();
        try {
            notimap.put("title",Variables.sharedPreferences.getString(Variables.u_name,"")+" Comment on your video");
            notimap.put("message",comment);
            notimap.put("icon",Variables.sharedPreferences.getString(Variables.u_pic,""));
            notimap.put("senderid",Variables.sharedPreferences.getString(Variables.u_id,""));
            notimap.put("receiverid", user_id);
            notimap.put("action_type","comment");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context,Variables.sendPushNotification,notimap,null);

    }

    /*private void sendFCMPush(String token, String message) {
        ModelUserInfo userInfo = SessionManager.getModelUserInfo(getActivity());

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;
        // TO get device token of other user id


        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", userInfo.getFirstName());
            objData.put("title", message);
            objData.put("sound", "default");
            objData.put("icon", "https://hopeapp.in/back/storage/app/public/profileimage/splashme.png"); //   icon_name image must be there in drawable
            objData.put("tag", token);
            objData.put("priority", "high");
            dataobjData = new JSONObject();
            dataobjData.put("text", userInfo.getFirstName());
            dataobjData.put("title", message);
            obj.put("to", token);
            //obj.put("priority", "high");
            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Variables.Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }*/


}
