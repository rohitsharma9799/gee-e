package com.geee.Login_Package;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Main_VP_Package.Main;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Signup_Package.Signup;
import com.geee.SplashActivity;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Login extends Fragment implements View.OnClickListener {

    View view;
    EditText etEmailUsername, etPassword;
    ImageView iv, iv1;
    Button btnLogin;
    RelativeLayout signUpRlt;
    TextView signUp;
    LinearLayout btnFacebook;
    JSONObject userDataObjs;
    private CallbackManager mCallbackManager;
    SharedPreferences sharedPreferences;

    boolean condition = false;
    TextView forget,passsmd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.login, null);
        mCallbackManager = CallbackManager.Factory.create();
        etEmailUsername = view.findViewById(R.id.et_emailUsername);
        etPassword = view.findViewById(R.id.et_password);
        signUp = view.findViewById(R.id.sign_up);
        forget = view.findViewById(R.id.forget);
        iv = view.findViewById(R.id.login_IV_id);
        iv1 = view.findViewById(R.id.login_IV1_id);
        passsmd = view.findViewById(R.id.passsmd);
        btnLogin = view.findViewById(R.id.btn_login);
        btnFacebook = view.findViewById(R.id.btn_facebook);

        signUpRlt = view.findViewById(R.id.signUp_rlt);

        btnLogin.setOnClickListener(Login.this);
        signUp.setOnClickListener(this);
        signUpRlt.setOnClickListener(this);
        iv.setOnClickListener(this);
        iv1.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);

        Functions.checkPermissions(getActivity());
        Functions.printKeyHash(getActivity());
        ImageView  showpass = view.findViewById(R.id.showpass);
        showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    }
                }, 1500);
            }
        });
     //   String tokends = SharedPrefrence.getOffline(getActivity(), SharedPrefrence.share_user_profile_pic);
        passsmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
                String text = passsmd.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getActivity(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(getContext(), Fogetpassword.class);
                getContext().startActivity(sendIntent);
            }
        });

        etEmailUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = etEmailUsername.length();
                if (len > 0) {
                    //iv.setVisibility(View.VISIBLE);
                } else {
                    //iv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = etPassword.length();
                if (len > 0) {
                    //iv1.setVisibility(View.VISIBLE);
                } else {
                   // iv1.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp_rlt:
                Signup f = new Signup();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.login_parent_RL_id, f).commit();
                break;


            case R.id.btn_login:


                if (checkValidation()) {
                    API_Calling_Methods.signIn(
                            etEmailUsername.getText().toString(),
                            etPassword.getText().toString(),
                            getContext()
                    );
//                    Intent sendIntent = new Intent(getActivity(), MainActivity.class);
//                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                            Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getActivity().startActivity(sendIntent);
                }


                break;

            case R.id.btn_facebook:

                loginwithFB();

                break;

            case R.id.login_IV_id:
                etEmailUsername.setText("");
                break;

            case R.id.login_IV1_id:
                etPassword.setText("");
                break;
            case R.id.sign_up:
                Signup signup = new Signup();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.login_parent_RL_id, signup).commit();
                break;

            default:
                break;
        }
    }

    // this will check the validations like none of the field can be the empty
    public boolean checkValidation() {

        final String stEmail = etEmailUsername.getText().toString();
        final String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(stEmail)) {
            etEmailUsername.setError("Please enter valid email");
            etEmailUsername.setFocusable(true);
            etEmailUsername.requestFocus();
            return false;
        } else if (!Functions.isValidEmail(stEmail)) {
            etEmailUsername.setError("Please enter valid email");
            etEmailUsername.setFocusable(true);
            etEmailUsername.requestFocus();
            return false;
        }
        if (password.isEmpty() || password.length() < 8) {
            etPassword.setError("Please enter valid password");
            etPassword.setFocusable(true);
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    //facebook implimentation
    public void loginwithFB() {
        LoginManager.getInstance().logOut();

        LoginManager.getInstance()
                .logInWithReadPermissions(Login.this,
                        Arrays.asList("public_profile", "email"));
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                getUserProfile(loginResult.getAccessToken());
               // loginResult.getAccessToken().getToken();
                Log.d("resp_token", loginResult + "");
            }

            @Override
            public void onCancel() {
                // App code
                Functions.toastMsg(getActivity(), "Login cancel" );

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

                    Log.i("mmmmmmm",object.toString());
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                    try {
                        final String id = Profile.getCurrentProfile().getId();
                        String fbUserId = object.getString("id");
                        String imgurl = "https://graph.facebook.com/" + object.getString("id") + "/picture";
                        String fbName = object.getString("name");
                        //LoginManager.getInstance().logOut();
                        if(object.has("email")){
                            String fbEmail = object.getString("email");
                            methodSignUp(fbEmail, "" + fbUserId, "" + fbName);
                        }else {
                            String emailactive = fbUserId+"@facebook.com";
                            methodSignUp(emailactive, "" + fbUserId, "" + fbName);
                        }
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

    // Apply signup API
    public void methodSignUp(String email, String password, String fullName) {
        passsmd.setText(password);

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
        condition = true;
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
               // Functions.cancelLoader();
                Functions.logDMsg("signUp: " + response.toString());
                Log.i("DFsfsfs",response.toString());
                try {
                    if (response.getString("code").equals("200")) {
                        // If SignUp ok
                        SharedPrefrence.saveInfoShare(getContext(), response.toString(),
                                SharedPrefrence.shared_user_FB_login_data_key);


                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("msg"));
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("User"));
                        Change_Url_to_base64(jsonObject2.getString("id"),jsonObject2.getString("username"),jsonObject2.getString("full_name"),"null","facebook");


                       /* Intent sendIntent = new Intent(getContext(), MainActivity.class);
                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(sendIntent);*/

                    } else if (response.getString("code").equals("201")) {
                        // If Email exist and proceed to Signin
                        //Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();

                        if (userDataObjs.isNull("email")){
                            Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                        }else {
                            signIN(userDataObjs.getString("email"),
                                    userDataObjs.getString("password"));
                        }

                    }
                   // Functions.cancelLoader();
                } catch (Exception v) {
                    v.printStackTrace();
                 //   Functions.cancelLoader();
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


            Log.i("Dfdzfdzz",password);

            String deviceToken = "";

            userDataObjs = new JSONObject();

            userDataObjs.put("email", email);
            userDataObjs.put("password", password);
            userDataObjs.put("device_token", deviceToken);

            Functions.logDMsg("userDataObjs " + userDataObjs.toString());

           // Functions.showLoader(getActivity(), false, false);

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

                Log.i("mtfrt",response.toString());
                Functions.logDMsg("LoginResponse " + response.toString());
               // Functions.cancelLoader();
                try {
                    if (response.getString("code").equals("200")) {
                        SharedPrefrence.saveInfoShare(getContext(), response.toString(),
                                SharedPrefrence.shared_user_FB_login_data_key
                        );

                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("msg"));
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("User"));
                        Change_Url_to_base64(jsonObject2.getString("id"),jsonObject2.getString("username"),jsonObject2.getString("full_name"),"null","facebook");

                        Log.i("oiiiiioiiii",response.toString());
                      //  Toast.makeText(getActivity(), "j", Toast.LENGTH_SHORT).show();
                        /*Intent sendIntent = new Intent(getContext(), MainActivity.class);
                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(sendIntent);*/

                    }else {
                        Functions.cancelLoader();
                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
               // Functions.cancelLoader();
            //    Toast.makeText(this, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
          //  Functions.cancelLoader();
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
                        Functions.cancelLoader();
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
                        Functions.cancelLoader();
                        System.out.println(error.toString());
                        //      progressbar.setVisibility(View.GONE);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }


}
