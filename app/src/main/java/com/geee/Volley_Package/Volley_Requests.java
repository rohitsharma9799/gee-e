package com.geee.Volley_Package;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Home_Package.GeeeFeed;
import com.geee.Inner_VP_Package.Home_Package.Home;
import com.geee.Inner_VP_Package.Main_F;
import com.geee.Main_VP_Package.Main;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.SplashActivity;
import com.geee.Utils.Callback_upload;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.google.android.exoplayer2.C;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Volley_Requests {
    public static Callback_upload mCallback = null;

    public static String apiCalling(final Context context, String apiLink, final JSONObject sendData, final String flag) {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.cancelLoader();

                Variables.res = response.toString();
                Log.i("DSFsfddsf",sendData.toString());
                Log.i("DSdfffFsfddsf",response.toString());


                Log.i("Fgddgdg",flag);

                String response_codef = null;
                try {
                    response_codef = response.getString("code");
                    if (response_codef.equals("201")) {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        Toast.makeText(context.getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //   Toast.makeText(context.getApplicationContext(), "bbb", Toast.LENGTH_SHORT).show();



                //Flag comparison for Data Maniplation from APIs response. :-D

                if (flag.equals("" + Variables.flagAddUserStory)) {
                    Functions.toastMsg(context, "Story added Successfully");
                    /*GeeeFeed geeeFeed = new GeeeFeed();
                    geeeFeed.setUpStoriesAdapter();*/
                    Intent sendIntent = new Intent(context, MainActivity.class);
                    context.startActivity(sendIntent);
                    /*Home geeeFeed = new Home();
                    geeeFeed.getStories();*/
                   // context.finish();
                }

                if (flag.equals("" + Variables.flagSendFollowReq)) {
                    // Follow Request
                }

                if (flag.equals("" + Variables.flagAddNewPost)) {
                    // If flag equal to Post Upload
                    /*Main_F.tabLayout.getTabAt(0).select();
                    ((Activity) context).finish();*/
                    Functions.logDMsg("resp at upload :" + response.toString());
                    SharedPrefrence.saveInfoShare(context,
                            "" + response.toString(),
                            "" + SharedPrefrence.share_post_key
                    );
                    Intent start = new Intent(context, Main.class);
                    start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(start);
                }

                if (flag.equals("" + Variables.flagEditProfile)) {
                    try {
                        if (response.getString("code").equals("200")) {
                            // If code equl to 200.
                            SharedPrefrence.saveInfoShare(context, response.toString(),
                                    SharedPrefrence.shared_user_FB_login_data_key);
                            Functions.toastMsg(context, "Updated Successfully.");
                            Intent sendIntent = new Intent(context, MainActivity.class);
                            //Intent.FLAG_ACTIVITY_CLEAR_TOP
                            sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(sendIntent);

                        } else {
                            Functions.alertDialogue(context, "Messege", "" + response.getString("msg"));
                        }
                        // Edit Profile
                    } catch (Exception b) {
                        b.printStackTrace();
                    }

                }

                if (flag.equals("" + Variables.flagFbLogin)) {
                    try {
                        String response_code = response.getString("code");
                     //   Toast.makeText(context.getApplicationContext(), "bbb", Toast.LENGTH_SHORT).show();

                        Functions.logDMsg("signUp response : " + response);

                        if (response_code.equals("200")) {
                            SharedPrefrence.saveInfoShare(
                                    context, response.toString(),
                                    SharedPrefrence.shared_user_FB_login_data_key
                            );
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("msg"));
                            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("User"));

                            Change_Url_to_base64(jsonObject2.getString("id"),jsonObject2.getString("username"),jsonObject2.getString("full_name"),"null","phone",context);

//
//                            Intent sendIntent = new Intent(context, MainActivity.class);
//                            sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(sendIntent);

                        } else if (response_code.equals("201")) {

                            // Toast.makeText(context, "kdfkfd", Toast.LENGTH_SHORT).show();
                           // Functions.alertDialogue(context, "Messege", "" + response.getString("msg"));
                        }

                    } catch (Exception v) {
                        v.printStackTrace();
                    }


                } else if (flag.equals("" + Variables.flagLogin)) {
                    ///If Login performed
                    ///Data Add into Shared Prefrence

                    Log.i("rrrrrr",response.toString());

                  //  Toast.makeText(context.getApplicationContext(), "njfnj", Toast.LENGTH_SHORT).show();
                    try {
                        if (response.getString("code").equals("200")) {
                            // Successfull login
                            SharedPrefrence.saveInfoShare(context, response.toString(),
                                    SharedPrefrence.shared_user_FB_login_data_key);
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("msg"));
                            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("User"));
                            Change_Url_to_base64(jsonObject2.getString("id"),jsonObject2.getString("username"),jsonObject2.getString("full_name"),"null","phone",context);

                            // Open Home Activity
                          /*  Intent sendIntent = new Intent(context, MainActivity.class);
                            //Intent.FLAG_ACTIVITY_CLEAR_TOP
                            sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(sendIntent);*/
                        } else {
                           // Toast.makeText(context, "hd", Toast.LENGTH_SHORT).show();

                            // Not Successfull Login
                            Functions.alertDialogue(context, "Messege", "" + response.getString("msg"));
                            // Run Login API
                        }

                    } catch (Exception v) {
                        v.printStackTrace();
                    }
                    // Open Activity after

                } else if (flag.equals(Variables.flagAddUserImage)) {
                    // If user add profile picture
                    SharedPrefrence.saveInfoShare(context, response.toString(),
                            SharedPrefrence.share_user_profile_pic
                    );
                    Toast.makeText(context, "Image Update Successfully", Toast.LENGTH_SHORT).show();
                    Intent start = new Intent(context, Main.class);
                    context.startActivity(start);

                    Log.i("dfdfdfdfdf",response.toString());
                } else if (flag.equals(Variables.flagAddUsercoverImage)) {
                    // If user add profile picture
                    SharedPrefrence.saveInfoShare(context, response.toString(),
                            SharedPrefrence.share_user_cover_pic
                    );
                } else if (flag.equals("" + Variables.flagFbLoginNew)) {
                    try {

                        if (response.getString("code").equals("201")) {
                            // If user is  Registered but exist
                            try {
                                API_Calling_Methods.signIn("" + sendData.getString("email"),
                                        "" + sendData.getString("password"),
                                        context);
                            } catch (Exception v) {
                                v.printStackTrace();
                            }
                        } else {
                            API_Calling_Methods.signUp("" + sendData.getString("email"),
                                    "" + sendData.getString("password"),
                                    "" + sendData.getString("full_name"),
                                    context,
                                    "" + Variables.flagFbLogin
                            );
                        }
                    } catch (Exception v) {
                        v.printStackTrace();
                    }

                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.cancelLoader();
                Functions.toastMsg(context, "Msg " + error.toString() + " Flag " + flag);
            }
        };
        /// ENd Interface

        if (!flag.equals(Variables.flagAddUserStory)) {
            Functions.showLoader(context, false, false);
        }

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, context);
        Variables.mVolleyService.postDataVolley("POSTCALL", apiLink, sendData);

        return Variables.res;

    }

    public void setListener(Callback_upload callBack) {
        mCallback = callBack;

    }

    //----------tictok
    public static void Change_Url_to_base64(final String user_id,
                                            final String f_name, final String l_name, String picture, final String singnup_type, Context context){

        Functions.showLoader(context,false,true);
        // Log.d(Variables.tag,picture);

        if(picture.equalsIgnoreCase("null")){
            Call_Api_For_Signup(user_id, f_name, l_name, picture, singnup_type,context);
        }
        else {
            Glide.with(context)
                    .asBitmap()
                    .load(picture)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            String image_base64 = Functions.Bitmap_to_base64((Activity) context, resource);
                            Call_Api_For_Signup(user_id, f_name, l_name, image_base64, singnup_type,context);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }
    // this function call an Api for Signin
    private static void Call_Api_For_Signup(String id,
                                            String f_name,
                                            String l_name,
                                            String picture,
                                            String singnup_type, Context context) {

        PackageInfo packageInfo = null;
        try {
            packageInfo =context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
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
            parameters.put("device", com.geee.tictokcode.SimpleClasses.Variables.device);
            parameters.put("hopeuserid",id);
            JSONObject file_data=new JSONObject();
            file_data.put("file_data",picture);
            parameters.put("image",file_data);

            Log.i("dfsdfsfsdfs",parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.showLoader(context,false,false);
/*
        ApiRequest.Call_Api(context, com.geee.tictokcode.SimpleClasses.Variables.SignUp, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancelLoader();
                Parse_signup_data(resp,context);
                Log.i("dfsfsdfds",resp);
                Toast.makeText(context, "fddg", Toast.LENGTH_SHORT).show();

            }
        });
*/

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,  com.geee.tictokcode.SimpleClasses.Variables.SignUp, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mncxvxcvxcv", response.toString());
                        Functions.cancelLoader();
                        Parse_signup_data(response.toString(),context);
                        Log.i("dfsfsdfds",response.toString());
                      //  Toast.makeText(context, "fddg", Toast.LENGTH_SHORT).show();
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



    // if the signup successfull then this method will call and it store the user info in local
    public static void Parse_signup_data(String loginData, Context context){
        SharedPreferences sharedPreferences;
        try {
            JSONObject jsonObject=new JSONObject(loginData);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray jsonArray=jsonObject.getJSONArray("msg");
                JSONObject userdata = jsonArray.getJSONObject(0);
                sharedPreferences= context.getSharedPreferences(com.geee.tictokcode.SimpleClasses.Variables.pref_name,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.u_id,userdata.optString("fb_id"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.f_name,userdata.optString("first_name"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.l_name,userdata.optString("last_name"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.u_name,userdata.optString("username"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.gender,userdata.optString("gender"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.u_pic,userdata.optString("profile_pic"));
                editor.putString(com.geee.tictokcode.SimpleClasses.Variables.api_token,userdata.optString("tokon"));
                editor.putBoolean(com.geee.tictokcode.SimpleClasses.Variables.islogin,true);
                editor.commit();
                com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences = context.getSharedPreferences(com.geee.tictokcode.SimpleClasses.Variables.pref_name,MODE_PRIVATE);
                com.geee.tictokcode.SimpleClasses.Variables.user_id=com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences.getString(com.geee.tictokcode.SimpleClasses.Variables.u_id,"");

                com.geee.tictokcode.SimpleClasses.Variables.Reload_my_videos=true;
                com.geee.tictokcode.SimpleClasses.Variables.Reload_my_videos_inner=true;
                com.geee.tictokcode.SimpleClasses.Variables.Reload_my_likes_inner=true;
                com.geee.tictokcode.SimpleClasses.Variables.Reload_my_notification=true;
                sendhopeidtohopedb(context,userdata.optString("fb_id"));
                // Toast.makeText(this, "ds"+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

     private static void sendhopeidtohopedb( Context context,String fb_id) {
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
