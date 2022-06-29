package com.geee.Volley_Package;

import android.content.Context;
import android.util.Log;

import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;

import org.json.JSONObject;

public class API_Calling_Methods {

    public static void signUp(String email, String password, String fullName, Context context, String flag) {
        JSONObject object;
        try {
            object = new JSONObject();
            object.put("email", email);
            object.put("full_name", fullName);
            object.put("password", password);
            object.put("username", fullName);
            object.put("accfrom", "other");

            Volley_Requests.apiCalling(context,
                    "" + API_LINKS.API_SIGN_UP, object,
                    "" + Variables.flagFbLogin);

            Functions.logDMsg("signUp : "+object);

            Log.i("DFsfsdf",object.toString());

        } catch (Exception v) {
            v.printStackTrace();
        }


    } // End sign up function

    public static void signIn(String email, String password, Context context) {
        JSONObject object;
        try {
            String device_token = "";
            object = new JSONObject();
            object.put("email", email);
            object.put("password", password);
            object.put("device_token", device_token);

            Volley_Requests.apiCalling(context, "" + API_LINKS.API_SIGN_IN,
                    object, "" + Variables.flagLogin
            );

            Functions.logDMsg("signIn : "+object);
        } catch (Exception v) {
            v.printStackTrace();
        }
    } // End Sign In method

    public static void editProfile(String email, String fullName, String username, String userId, String gender,
                                   String website, String bio, String phone, Context context) {
        JSONObject object;
        try {
            object = new JSONObject();
            object.put("email", email);
            object.put("full_name", fullName);
            object.put("username", username);
            object.put("user_id", userId);
            object.put("gender", gender);
            object.put("website", website);
            object.put("bio", bio);
            object.put("phone", phone);

            Volley_Requests.apiCalling(context, "" + API_LINKS.API_EDIT_PROFILE,
                    object, "" + Variables.flagEditProfile
            );
            Functions.logDMsg("editProfile : "+object);
            Functions.logDMsg("editProfile : "+object);
        } catch (Exception v) {
            v.printStackTrace();
        }


    } // End Edit Profile method

    public static void addNewPost(String caption, String locationString, String lat, String lng, String user_id,
                                  String attachment, Context context) {
        JSONObject object;
        try {
            object = new JSONObject();
            object.put("caption", "" + caption);
            object.put("location_string", "" + locationString);
            object.put("lat", "" + lat);
            object.put("long", "" + lng);
            object.put("user_id", "" + user_id);
            JSONObject file_data = new JSONObject();
            file_data.put("file_data", "" + attachment);
            object.put("attachment", file_data);
            Functions.logDMsg("" + object);
            Volley_Requests.apiCalling(context, "" + API_LINKS.API_ADD_NEW_POST,
                    object, "" + Variables.flagAddNewPost
            );

            Functions.logDMsg("addNewPost : "+object);

        } catch (Exception v) {
            v.printStackTrace();
        }
    }


    public static void addUserImage(String userId, String attachment, Context context) {
        JSONObject object;
        try {
            object = new JSONObject();
            object.put("user_id", "" + userId);
            object.put("title", "" + "test");
            JSONObject file_data = new JSONObject();
            file_data.put("file_data", "" + attachment);
            object.put("image", file_data);
            Volley_Requests.apiCalling(context, "" + API_LINKS.API_ADD_USER_IMAGE,
                    object, "" + Variables.flagAddUserImage
            );

            Functions.logDMsg("addUserImage : "+object);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    public static void addUsercoverImage(String userId, String attachment, Context context) {
        JSONObject object;
        try {
            object = new JSONObject();
            object.put("user_id", "" + userId);
            object.put("title", "" + "test");
            JSONObject file_data = new JSONObject();
            file_data.put("file_data", "" + attachment);
            object.put("image", file_data);
            Volley_Requests.apiCalling(context, "" + API_LINKS.API_ADD_USER_COVERIMAGE,
                    object, "" + Variables.flagAddUsercoverImage
            );

            Functions.logDMsg("addUserImage : "+object);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }
    public static void addPostBookmark(String userId, String postId, String key, String value, Context context) {

        JSONObject object;

        try {
            object = new JSONObject();
            object.put("post_id",postId);
            object.put("user_id",userId);
            object.put(key,value);

            Volley_Requests.apiCalling(context, "" + API_LINKS.API_Post_Action,
                    object, "" + Variables.flagAddPostAction
            );
            Functions.logDMsg("addPostBookmark : "+object);
        } catch (Exception v) {
            v.printStackTrace();
        }

    }

    public static void addPostAction(String userId, String postId, String key, String value, Context context) {
        //General post Actions like comment, Like , Bookmark
        JSONObject params;
        try {
            params = new JSONObject();
            params.put("post_id",postId);
            params.put("user_id",userId);
            params.put(key,value);

            Volley_Requests.apiCalling(
                    context,
                    "" + API_LINKS.API_POST_COMMENT,
                    params,
                    "" + Variables.flagAddPostAction
            );

            Functions.logDMsg("addPostAction : "+params);
        } catch (Exception v) {
            v.printStackTrace();
        }


    }


    public static void commentDelete(int commentId, Context context) {
        JSONObject object;
        try {
            object = new JSONObject();
            object.put("id",commentId);
            Volley_Requests.apiCalling(context, "" + API_LINKS.API_DEL_COMMENT,
                    object, "okkk"
            );

            Functions.logDMsg("params at commentDelete : "+object);
        } catch (Exception v) {
            v.printStackTrace();
        }


    } // End Sign In method

    public static void addStoryAPI(String userId, String attachment, String type, Context context) {

        JSONObject userDataObjs;
        try {
            userDataObjs = new JSONObject();
            userDataObjs.put("user_id",userId);
            userDataObjs.put("attachment",attachment);
            userDataObjs.put("type",type);

            Volley_Requests.apiCalling(context, "" + API_LINKS.API_ADD_USER_STORY,
                    userDataObjs, "" + Variables.flagAddUserStory
            );

            Functions.logDMsg("params at addStoryAPI : "+userDataObjs);
        } catch (Exception v) {
            v.printStackTrace();
        }


    }

}
