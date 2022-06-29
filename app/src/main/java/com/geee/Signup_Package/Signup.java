package com.geee.Signup_Package;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.geee.BackStack.RootFragment;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Login_Package.Login;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Signup extends RootFragment implements View.OnClickListener {

    View view;
    RelativeLayout loginRlt;
    JSONObject userDataObjs;
    RelativeLayout signupEmail, signupFb;
    private CallbackManager mCallbackManager;
    SharedPreferences sharedPreferences;
    Button btn_login;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCallbackManager = CallbackManager.Factory.create();
        view = LayoutInflater.from(getContext()).inflate(R.layout.signup, null);
        btn_login = view.findViewById(R.id.btn_signin);
        loginRlt = view.findViewById(R.id.login_rlt);
        signupEmail = view.findViewById(R.id.signup_RKL_id_o);
        signupFb = view.findViewById(R.id.signup_fb);

        signupEmail.setOnClickListener(this);
        loginRlt.setOnClickListener(this);
        signupFb.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        return view;
    }

    public void methodOpenlogin() {
        Login login = new Login();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.signup_parent_RL_id, login).commit();
    }

    public void methodOpensignupemailphn() {
        startActivity(new Intent(getActivity(), Signup_Email.class));
    }


    //facebook implimentation
    public void loginwithFB() {

        LoginManager.getInstance()
                .logInWithReadPermissions(Signup.this,
                        Arrays.asList("public_profile", "email"));

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserProfile(loginResult.getAccessToken());
                Log.d("resp_token", loginResult.getAccessToken() + "");
            }

            @Override
            public void onCancel() {
                // App code
                Functions.toastMsg(getActivity(), "Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("resp", "" + error.toString());
                Functions.toastMsg(getActivity(), "Login Error" + error.toString());
            }

        });


    }

    // ==> Get User data from Fb Servers
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                (object, response) -> {
                    try {
                        String fbUserId = object.getString("id");
                        String imgurl = "https://graph.facebook.com/" + object.getString("id") + "/picture";
                        String fbName = object.getString("name");
                        LoginManager.getInstance().logOut();
                        if(object.has("email")){
                            String fbEmail = object.getString("email");
                            methodSignUp(fbEmail, "" + fbUserId, "" + fbName);
                        }else {
                            String emailactive = fbUserId+"@facebook.com";
                            methodSignUp(emailactive, "" + fbUserId, "" + fbName);
                        }
                       //
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Functions.toastMsg(getContext(), "Error " + e.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Apply signup API
    public void methodSignUp(String email, String password, String fullName) {

        initVolleyCallbackSignUp();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());

        try {
            int index = email.indexOf("@");
            String userName = email.substring(0, index);

            userDataObjs = new JSONObject();

            userDataObjs.put("email", email);
            userDataObjs.put("full_name", fullName);
            userDataObjs.put("password", password);
            userDataObjs.put("username", userName);
            userDataObjs.put("accfrom", "facebook");

            Functions.logDMsg("userDataObjs " + userDataObjs.toString());

            Functions.showLoader(getActivity(), false, false);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_SIGN_UP, userDataObjs);

        } catch (Exception v) {
            v.printStackTrace();
        }


    } // End method to get home upload

    // TODO init for Home Posts

    // Initialize Interface Call Backs
    void initVolleyCallbackSignUp() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                Functions.logDMsg("signUp: " + response.toString());
                try {
                    if (response.getString("code").equals("200")) {
                        // If SignUp ok
                        SharedPrefrence.saveInfoShare(
                                getContext(),
                                response.toString(),
                                SharedPrefrence.shared_user_FB_login_data_key
                        );

                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("msg"));
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("User"));
                        Change_Url_to_base64(jsonObject2.getString("id"),jsonObject2.getString("username"),jsonObject2.getString("full_name"),"null","facebook");

                        /*Intent sendIntent = new Intent(getContext(), MainActivity.class);
                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(sendIntent);*/

                    } else if (response.getString("code").equals("201")) {
                        // If Email exist and proceed to Signin
                        signIN(userDataObjs.getString("email"),
                                userDataObjs.getString("password"));
                    }
                } catch (Exception v) {
                    v.printStackTrace();
                    Functions.logDMsg("SIGN UP v : " + v.toString());
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }
    // ENd Data From API


    /// ======== Sign In =========
    public void signIN(String email, String password) {

        initvolleycallbackSignIn();


        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {

            String deviceToken = "";

            userDataObjs = new JSONObject();

            userDataObjs.put("email", email);
            userDataObjs.put("password", password);
            userDataObjs.put("device_token", deviceToken);

            Functions.logDMsg("userDataObjs " + userDataObjs.toString());

            Functions.showLoader(getActivity(), false, false);

            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_SIGN_IN, userDataObjs);

        } catch (Exception v) {
            v.printStackTrace();
        }


    } // End method to get home upload


    // Initialize Interface Call Backs
    void initvolleycallbackSignIn() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.logDMsg("LoginResponse " + response.toString());
                Functions.cancelLoader();
                try {
                    if (response.getString("code").equals("200")) {
                        SharedPrefrence.saveInfoShare(getContext(), response.toString(),
                                SharedPrefrence.shared_user_FB_login_data_key
                        );

                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("msg"));
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("User"));
                        Change_Url_to_base64(jsonObject2.getString("id"),jsonObject2.getString("username"),jsonObject2.getString("full_name"),"null","facebook");

                        /*Intent sendIntent = new Intent(getContext(), MainActivity.class);
                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(sendIntent);*/

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_rlt:
                getFragmentManager().popBackStack();
                break;

            case R.id.signup_fb:
               /* Intent start = new Intent(getContext(),MainActivity.class);
                startActivity(start);*/
                loginwithFB();
                break;

            case R.id.signup_RKL_id_o:
                methodOpensignupemailphn();
                break;

            case R.id.btn_signin:
                methodOpensignupemailphn();
                break;
            default:
                break;
        }
    }



    //------------Ticktok
    public void Change_Url_to_base64(final String user_id,
                                     final String f_name, final String l_name, String picture, final String singnup_type){


        if(picture.equalsIgnoreCase("null")){
            Call_Api_For_Signup(user_id, f_name, l_name, picture, singnup_type);
        }
        else {
            Glide.with(this)
                    .asBitmap()
                    .load(picture)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            String image_base64 = Functions.Bitmap_to_base64(getActivity(), resource);
                            Call_Api_For_Signup(user_id, f_name, l_name, image_base64, singnup_type);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }




    // this function call an Api for Signin
    private void Call_Api_For_Signup(String id,
                                     String f_name,
                                     String l_name,
                                     String picture,
                                     String singnup_type) {

        PackageInfo packageInfo = null;
        try {
            packageInfo =getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appversion=packageInfo.versionName;

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("fb_id", id);
            parameters.put("first_name",""+f_name);
            parameters.put("last_name", ""+l_name);
            parameters.put("gender","m");
            parameters.put("version",appversion);
            parameters.put("signup_type",singnup_type);
            parameters.put("device",com.geee.tictokcode.SimpleClasses.Variables.device);
            parameters.put("hopeuserid","1");
            JSONObject file_data=new JSONObject();
            file_data.put("file_data",picture);
            parameters.put("image",file_data);

            Log.i("dfsdfsfsdfs",parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  Functions.Show_loader(this,false,false);
        ApiRequest.Call_Api(getActivity(), com.geee.tictokcode.SimpleClasses.Variables.SignUp, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                // Functions.cancel_loader();
                Parse_signup_data(resp);
                Log.i("dfsfsdfds",resp);
                //  Toast.makeText(mContext, "fddg", Toast.LENGTH_SHORT).show();

            }
        });

    }



    // if the signup successfull then this method will call and it store the user info in local
    public void Parse_signup_data(String loginData){

        try {
            JSONObject jsonObject=new JSONObject(loginData);
            String code=jsonObject.optString("code");
            if(code.equals("200")){

                JSONArray jsonArray=jsonObject.getJSONArray("msg");
                JSONObject userdata = jsonArray.getJSONObject(0);
                sharedPreferences= getActivity().getSharedPreferences(com.geee.tictokcode.SimpleClasses.Variables.pref_name,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.u_id,userdata.optString("fb_id"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.f_name,userdata.optString("first_name"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.l_name,userdata.optString("last_name"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.u_name,userdata.optString("username"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.gender,userdata.optString("gender"));
                //editor.putString(com.geee.tictokcode.SimpleClasses.Variables.u_pic,userdata.optString("profile_pic"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.api_token,userdata.optString("tokon"));
                editor.putBoolean(com.geee.tictokcode.SimpleClasses.Variables.islogin,true);
                editor.commit();

                Log.i("xcxc",userdata.optString("fb_id"));
                com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences=getActivity().getSharedPreferences(com.geee.tictokcode.SimpleClasses.Variables.pref_name,MODE_PRIVATE);
                com.geee.tictokcode.SimpleClasses.Variables.user_id=com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences.getString(com.geee.tictokcode.SimpleClasses.Variables.u_id,"");

                com.geee.tictokcode.SimpleClasses.Variables.Reload_my_videos=true;
                com.geee.tictokcode.SimpleClasses.Variables.Reload_my_videos_inner=true;
                com.geee.tictokcode.SimpleClasses.Variables.Reload_my_likes_inner=true;
                com.geee.tictokcode.SimpleClasses.Variables.Reload_my_notification=true;
                sendhopeidtohopedb(getActivity(),userdata.optString("fb_id"));
                // sendhopeidtohopedb(message,userdata.optString("fb_id"));
                // Toast.makeText(this, "ds"+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }else {
                //    Toast.makeText(this, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static void sendhopeidtohopedb(Context context, String fb_id) {
        JSONObject parameters = new JSONObject();
        try {

            parameters.put("user_id",  SharedPrefrence.getUserIdFromJson(context));
            parameters.put("tictic_id", fb_id);
            Log.i("dfsdfsfsdfs", parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_PRODUCTS =  com.geee.tictokcode.SimpleClasses.Variables.updatetictokidhopedb;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_PRODUCTS, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mncxvxcvxcv", response.toString());
                        Log.d("mncxvxcvxdfsfcv", URL_PRODUCTS);
                        Intent sendIntent = new Intent(context, MainActivity.class);
                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(sendIntent);
                        // Toast.makeText(context, "Send Hope Id To ", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        //      progressbar.setVisibility(View.GONE);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

}
