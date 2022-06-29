package com.geee.Inner_VP_Package;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.GeeeFeed;
import com.geee.Location.CourseModal;
import com.geee.Location.DeckAdapter;
import com.geee.Location.LocationActivity;
import com.geee.Main_VP_Package.Main;
import com.geee.R;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.geee.tictokcode.Main_Menu.MainMenuActivity;
import com.geee.tictokcode.Splash_A;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class PostEditF extends Fragment implements View.OnClickListener {
    View view;

    ImageView tickId, icBack;
    SimpleDraweeView profPhotoId, postImage;
    TextView timeAgo, tvUsername;
    EditText tvCaption;
    String postId, userId, created, username, attachment, caption, profileImage;

    public PostEditF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_postedit, container, false);

        postId = getArguments().getString("post_id");
        userId = getArguments().getString("userId");
        created = getArguments().getString("created");
        username = getArguments().getString("username");
        attachment = getArguments().getString("attachment");
        caption = getArguments().getString("caption");
        profileImage = getArguments().getString("profileImage");


        initView();

        setUpScreenData();
        return view;
    }

    private void initView() {

        icBack = view.findViewById(R.id.ic_back);
        tickId = view.findViewById(R.id.tick_id);
        profPhotoId = view.findViewById(R.id.prof_photo_id);
        postImage = view.findViewById(R.id.item_id);
        tvUsername = view.findViewById(R.id.username_id);
        timeAgo = view.findViewById(R.id.time_ago);
        tvCaption = view.findViewById(R.id.tv_caption);
        tickId.setOnClickListener(this);
        icBack.setOnClickListener(this);
    }


    private void setUpScreenData() {
        tvUsername.setText(username);

        timeAgo.setText("" + Functions.getTimeAgoOrg(created));

        if (profileImage != null && !profileImage.equals("")) {
            profPhotoId.setImageURI(Uri.parse(Constants.BASE_URL + profileImage));

            Log.i("uri at editprofile : " , String.valueOf(Uri.parse(Constants.BASE_URL + profileImage)));
        }

        if (attachment != null && !attachment.equals("")) {
            postImage.setImageURI(Uri.parse(Constants.BASE_URL + attachment));
        }

        if (caption != null && !caption.equals("")) {
            tvCaption.setText(caption);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tick_id:
                methodCallApiForEdit();
                break;

            case R.id.ic_back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            default:
                break;
        }
    }

    private void methodCallApiForEdit() {
        Functions.showLoader(getActivity(),false,false);
        JSONObject sendObj = new JSONObject();
        try {

            sendObj.put("postId", postId);
            sendObj.put("caption", tvCaption.getText().toString());
            Log.i("Dfsdfs",sendObj.toString());
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "dsfsdfsf" + v.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,  "https://www.gee-e.com/gee/api/editPost", sendObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Functions.cancelLoader();
                        Log.i("resposdsdn" , response.toString());
                        try {
                            if (response.getString("code").equals("200")) {
                                Intent intent=new Intent(getActivity(), Main.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        } catch (Exception v) {
                            Functions.cancelLoader();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();

                Functions.cancelLoader();
                Log.i("Dfsdfsdf",error.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                Log.i("respon" , response.toString());
                try {
                    if (response.getString("code").equals("200")) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
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

}