package com.geee.CodeClasses;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Variables {

    public static Boolean check = true;
    public static int MY_SOCKET_TIMEOUT_MS = 30000;
    public static com.geee.Volley_Package.IResult mResultCallback = null;
    public static com.geee.Volley_Package.VolleyService mVolleyService;
    public static String res;
    public static String typeImg = "image";
    public static String typeVideo = "video";
    public static String errorUrl = "https://i.ibb.co/pxP8dnX/ic-profile-gray.png";
    public static int clickAreaTop100 = 100;
    public static int clickAreaBottom100 = 100;
    public static int clickAreaLeft100 = 100;
    public static int clickAreaRight100 = 100;
    public static int clickAreaRight200 = 200;

    public static String flagLogin = "login";
    public static String flagFbLoginNew = "fb_login_update";

    public static String flagFbLogin = "fb_login";

    public static String flagEditProfile = "edit_profile";

    public static String flagAddNewPost = "add_new_post";

    public static String flagAddUserImage = "add_user_image";

    public static String flagAddUsercoverImage = "add_user_coverimage";
    public static String flagSendFollowReq = "send_follow";

    public static String flagAddPostAction = "add_post_action";

    public static String flagAddUserStory = "add_user_story";

    public static String downloadPref = "download_pref";

    public static String gifFirstpart = "https://media.giphy.com/media/";
    public static String gifSecondpart = "/100w.gif";

    public static String gifFirstpartChat = "https://media.giphy.com/media/";
    public static String gifSecondpartChat = "/200w.gif";

    public static int permissionCameraCode = 786;
    public static int permissionWriteData = 788;
    public static int permissionReadData = 789;
    public static int permissionRecordingAudio = 790;
    public static int vedioRequestCode = 9;

    public static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);

    public static String openedChatId;
    public static String userName;
    public static String userPic;
    public static String userId;
}
