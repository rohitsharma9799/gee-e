package com.geee.SharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.geee.Constants;

import org.json.JSONObject;

public class SharedPrefrence {

    public static SharedPreferences.Editor editor;
    public static SharedPreferences pref;
    public static String shared_user_login_detail_key = "user_info";
    public static String shared_user_FB_login_data_key = "FB_login";
    public static String share_media_upload_path_key = "media_upload_path";
    public static String share_user_profile_pic = "user_profile_pic";
    public static String share_user_cover_pic = "user_cover_pic";
    public static String share_search_key = "search_key";
    public static String share_post_key = "share_post";
    public static String token = "token";


    public static void initShare(Context context) {
        pref = context.getSharedPreferences("Instagram", 0); // 0 - for private mode
        editor = pref.edit();
    }

    public static void saveInfoShare(Context context, String value, String data_key) {
        initShare(context);
        editor.putString(data_key, value); // Storing string
        editor.commit();

    }

    public static void saveToken(Context context, String value) {
        initShare(context);
        editor.putString(SharedPrefrence.token, value); // Storing string
        editor.commit();
    }

    public static void logoutUser(Context context) {
        initShare(context);
        pref.edit().remove(shared_user_FB_login_data_key).commit();
        pref.edit().remove(shared_user_login_detail_key).commit();
        pref.edit().remove(share_media_upload_path_key).commit();
        pref.edit().remove(share_user_profile_pic).commit();
        pref.edit().remove(share_search_key).commit();
        pref.edit().remove(share_post_key).commit();
        pref.edit().remove(token).commit();
    }

    public static String getOffline(Context context, String key) {
        initShare(context);
        return pref.getString(key, null);
    }

    public static String getToken(Context context) {
        initShare(context);
        return pref.getString(SharedPrefrence.token, "");
    }

    public static String getUserIdFromJson(Context context) {
        String userJson = getOffline(context, SharedPrefrence.shared_user_FB_login_data_key);
        String userId = "0";
        try {
            JSONObject response = new JSONObject(userJson);
            JSONObject msg = response.getJSONObject("msg");
            JSONObject user = msg.getJSONObject("User");
            userId = user.getString("id");
        } catch (Exception b) {
            b.printStackTrace();
        }

        return userId;
    }


    ///////// ======= Get User Name
    public static String getUserNameFromJson(Context context) {
        String userJson = getOffline(context, shared_user_FB_login_data_key);
        String userName = "";
        try {
            JSONObject response = new JSONObject(userJson);
            JSONObject msg = response.getJSONObject("msg");
            JSONObject user = msg.getJSONObject("User");
            userName = user.getString("full_name");
        } catch (Exception b) {
            b.printStackTrace();
        }

        return userName;
    }
    //////////// ========= ENd get User Name

    ///////// ======= Get User Name
    public static String getUserImgFromJson(Context context) {
        String user_json = getOffline(context, shared_user_FB_login_data_key);
        String userName = "";
        try {
            JSONObject response = new JSONObject(user_json);
            JSONObject msg = response.getJSONObject("msg");
            JSONObject user = msg.getJSONObject("User");
            userName = Constants.BASE_URL + user.getString("image");
        } catch (Exception b) {
            b.printStackTrace();
        }

        return userName;
    }


    ///////// ======= Get User Name
    public static String getUserImageFromJson(Context context) {
        String userJson = getOffline(context, shared_user_FB_login_data_key);
        String userImg = "";
        try {
            JSONObject response = new JSONObject(userJson);
            JSONObject msg = response.getJSONObject("msg");
            JSONObject user = msg.getJSONObject("User");
            userImg = Constants.BASE_URL + user.getString("image");
        } catch (Exception b) {
            b.printStackTrace();
        }

        return userImg;
    }
    //////////// ========= ENd get User Name

}
