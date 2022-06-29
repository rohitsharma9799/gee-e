package com.geee.tictokcode.SimpleClasses;

import android.content.SharedPreferences;

import com.geee.Constants;
import com.geee.Utils.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by AQEEL on 2/15/2019.
 */

public class  Variables {
    public static String Legacy_SERVER_KEY = "AAAA1LwjFBw:APA91bFsru6a0vgGN1GCNzbmpsQYEmjFMh-AomWmuEJV_Y7I7FdxSS1q0WHUxzPaprivcDsFAMzSyjOrzjqQQk-FGdUJdFW4suaonQnLYUXxHsJx9yInOWbLWrfkZWfu7fEuZ_Acv9fR";
    public static final String device="android";

    public static int screen_width;
    public static int screen_height;

    public static final String SelectedAudio_AAC ="SelectedAudio.aac";
    public static final String root= MyApplication.getAppContext().getExternalFilesDir(null).getAbsolutePath();
    public static final String app_hided_folder =root+"/.HidedTicTic/";
    public static final String app_showing_folder =root+"/TicTic/";
    public static final String draft_app_folder= app_hided_folder +"Draft/";


    public static int max_recording_duration=18000;
    public static int recording_duration=18000;
    public static int min_time_recording=5000;

    public static String output_frontcamera= app_hided_folder + "output_frontcamera.mp4";
    public static String outputfile= app_hided_folder + "output.mp4";
    public static String outputfile2= app_hided_folder + "output2.mp4";
    public static String output_filter_file= app_hided_folder + "output-filtered.mp4";
    public static String gallery_trimed_video= app_hided_folder + "gallery_trimed_video.mp4";
    public static String gallery_resize_video= app_hided_folder + "gallery_resize_video.mp4";



    public static SharedPreferences sharedPreferences;
    public static final String pref_name="pref_name";
    public static final String u_id="u_id";
    public static final String u_name="u_name";
    public static final String u_pic="u_pic";
    public static final String u_coverpic="u_coverpic";
    public static final String f_name="f_name";
    public static final String l_name="l_name";
    public static final String gender="u_gender";
    public static final String islogin="is_login";
    public static final String device_token="device_token";
    public static final String api_token="api_token";
    public static final String device_id="device_id";
    public static final String uploading_video_thumb="uploading_video_thumb";

    public static String user_id;
    public static String user_name;
    public static String user_pic;
    public static String user_coverpic;

    public static String tag="tictic_";

    public static String Selected_sound_id="null";

    public static  boolean Reload_my_videos=false;
    public static  boolean Reload_my_videos_inner=false;
    public static  boolean Reload_my_likes_inner=false;
    public static  boolean Reload_my_notification=false;
    public static final String gif_firstpart="https://media.giphy.com/media/";
    public static final String gif_secondpart="/100w.gif";
    public static final String gif_firstpart_chat="https://media.giphy.com/media/";
    public static final String gif_secondpart_chat="/200w.gif";
    public static final SimpleDateFormat df =
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.ENGLISH);
    public static final SimpleDateFormat df2 =
            new SimpleDateFormat("dd-MM-yyyy HH:mmZZ", Locale.ENGLISH);
    // if you want a user can't share a video from your app then you have to set this value to true
    public static final boolean is_secure_info=false;
    // if you want a ads did not show into your app then set the belue valuw to true.
    public static final boolean is_remove_ads=false;
    // if you want a video thumnail image show rather then a video gif then set the below value to false.
    public static final boolean is_show_gif=true;
    // if you want to add a limit that a user can watch only 6 video then change the below value to true
    // if you want to change the demo videos limit count then set the count as you want
    public static final boolean is_demo_app=false;
    public static final int demo_app_videos_count=6;
    // if you want to add a duet function into our project you have to set this value to "true"
    // and also get the extended apis
    public static final boolean is_enable_duet=false;



    public final static int permission_camera_code=786;
    public final static int permission_write_data=788;
    public final static int permission_Read_data=789;
    public final static int permission_Recording_audio=790;
    public final static int Pick_video_from_gallery=791;




   public static String gif_api_key1="giphy_api_key_here";

    public static final String privacy_policy="http://www.privacypolicygenerator.info/live.php?token=";


    public static final String main_domain="http://gee-e.com/gee_tictok/";
    public static final String base_url=main_domain+"API/";
    public static final String api_domain =base_url+"index.php?p=";
    public static final String SignUp = api_domain +"signup";
    public static final String uploadVideo = api_domain +"uploadVideo";
    public static final String showAllVideos = api_domain +"showAllVideos";
    public static final String showMyAllVideos= api_domain +"showMyAllVideos";
    public static final String likeDislikeVideo= api_domain +"likeDislikeVideo";
    public static final String updateVideoView= api_domain +"updateVideoView";
    public static final String allSounds= api_domain +"allSounds";
    public static final String fav_sound= api_domain +"fav_sound";
    public static final String my_FavSound= api_domain +"my_FavSound";
    public static final String my_liked_video= api_domain +"my_liked_video";
    public static final String follow_users= api_domain +"follow_users";
    public static final String discover= api_domain +"discover";
    public static final String showVideoComments= api_domain +"showVideoComments";
    public static final String postComment= api_domain +"postComment";
    public static final String edit_profile= api_domain +"edit_profile";
    public static final String get_user_data= api_domain +"get_user_data";
    public static final String get_followers= api_domain +"get_followers";
    public static final String get_followings= api_domain +"get_followings";
    public static final String SearchByHashTag= api_domain +"SearchByHashTag";
    public static final String sendPushNotification= api_domain +"sendPushNotification";
    public static final String uploadImage= api_domain +"uploadImage";
    public static final String uploadcoverImage= api_domain +"uploadCover";
    public static final String DeleteVideo= api_domain +"DeleteVideo";
    public static final String search= api_domain +"search";
    public static final String getNotifications= api_domain +"getNotifications";
    public static final String getVerified= api_domain +"getVerified";
    public static final String downloadFile= api_domain +"downloadFile";
    public static final String profileupdate= api_domain +"sethopeprofile";
    public static final String Updatfirebasetoken= api_domain +"settoken";
    public static final String updatetictokidhopedb= Constants.BASE_URL+"api/ticticidupdate";
    public static final String getticktoid= "http://hopeapp.in/back/public/api/user/ticticid?";
    public static final String GETTOKEN= "http://hopeapp.in/tictok/API/index.php?p=gettoken";
    public static final String TOURNAMENT= "http://hopeapp.in/back/public/api/tournament/apply";
    public static final String GETTOURNAMENT= "http://hopeapp.in/back/public/api/tournament/tournamentregistration";
    public static final String GETTOURNAMENTROUND= "http://hopeapp.in/back/public/api/tournament/runningtournament";
    public static final String GETTOURNAMENTROUNDDETAIL= "http://hopeapp.in/back/public/api/tournament/roundhistory";
    public static final String GEEEALLUSER= Constants.BASE_URL+"api/allusers";
    public static final String GEEEFORGETPASS= Constants.BASE_URL+"api/forgotpassword";
    public static final String CHANGEPASS= Constants.BASE_URL+"api/changepassword";
}
