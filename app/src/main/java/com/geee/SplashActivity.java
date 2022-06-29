package com.geee;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.geee.CodeClasses.Functions;
import com.geee.Main_VP_Package.Main;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Utils.MyApplication;
import com.geee.tictokcode.Main_Menu.MainMenuActivity;
import com.geee.tictokcode.SimpleClasses.Variables;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {
    Button getstarted;
    public static String token;
    ProgressBar p_bar;
    SimpleExoPlayer player;
    String currentVersion="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
        }
        setContentView(R.layout.activity_splash);
        p_bar=findViewById(R.id.p_bar);
        p_bar.setVisibility(View.VISIBLE);
       // checkversion();
        // GOOGLE PLAY APP SIGNING SHA-1 KEY:- 65:5D:66:A1:C9:31:85:AB:92:C6:A2:60:87:5B:1A:DA:45:6E:97:EA
        byte[] sha1 = {
                (byte)0x8A, (byte)0xBE, 0x50, (byte)0xC9, (byte)0xCB, 0x33, (byte)0x5E, (byte)0xD7, (byte)0x77, (byte)0xF4, (byte)0x55, 0x3B, 0x76, (byte)0xC4, (byte)0xF9, (byte)0xAD, 0x2D, (byte)0xEA, (byte)0xD9, (byte)0x17
        };
        System.out.println("keyhashGooglePlaySignIn:"+ Base64.encodeToString(sha1, Base64.NO_WRAP));
      /*  SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this);
        PlayerView playerView = findViewById(R.id.playerview);

        // Bind the player to the view.
        playerView.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "geee"));

        // This is the MediaSource representing the media to be played.
        MediaSource firstSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.inshot));
        PlaybackParameters param = new PlaybackParameters(1.5f);
        player.setPlaybackParameters(param);
        // Prepare the player with the source.
        player.prepare(firstSource);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_BUFFERING){
                   *//* PlaybackControlView controlView = playerView.findViewById(R.id.exo_controller);
                    controlView.findViewById(R.id.exo_play).setVisibility(View.GONE);
                    controlView.findViewById(R.id.exo_play).setVisibility(View.INVISIBLE);
                    controlView.findViewById(R.id.exo_pause).setVisibility(View.GONE);
                    controlView.findViewById(R.id.exo_pause).setVisibility(View.INVISIBLE);*//*
                } else {
                   *//* PlaybackControlView controlView = playerView.findViewById(R.id.exo_controller);
                    controlView.findViewById(R.id.exo_play).setVisibility(View.GONE);
                    controlView.findViewById(R.id.exo_play).setVisibility(View.INVISIBLE);
                    controlView.findViewById(R.id.exo_pause).setVisibility(View.GONE);
                    controlView.findViewById(R.id.exo_pause).setVisibility(View.INVISIBLE);*//*
                }
                if (playbackState == ExoPlayer.STATE_ENDED){
                    player.stop();
                    player.release();
                    Intent start = new Intent(SplashActivity.this, Main.class);
                    startActivity(start);
                    finish();
                  //  onNext();
                }
            }
        });
        player.setPlayWhenReady(true);*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Variables.screen_height= displayMetrics.heightPixels;
        Variables.screen_width= displayMetrics.widthPixels;




        Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);

        Variables.user_id=Variables.sharedPreferences.getString(Variables.u_id,"");
        Variables.user_name=Variables.sharedPreferences.getString(Variables.u_name,"");
        Variables.user_pic=Variables.sharedPreferences.getString(Variables.u_pic,"");


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.i("Fdgdfd",token);
                        if(token==null || (token.equals("")||token.equals("null"))){
                            token=Variables.sharedPreferences.getString(Variables.device_token,"null");



                        }

                        // Log and toast
                    }
                });




        Allid();
     ////*/*/

        subscribeToPushService();

        final String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor2 =  Variables.sharedPreferences.edit();
        editor2.putString(Variables.device_id, android_id).commit();
        //------------------

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent start = new Intent(SplashActivity.this, Main.class);
                startActivity(start);
                finish();
            }
        }, 900);
    }
    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    String token = task.getResult();
                    SharedPrefrence.saveToken(SplashActivity.this, token);
                    String tokendf = Variables.sharedPreferences.getString(Variables.device_token,"");

                    Log.i("Dfsdfsdfs",token);

                });
    }
    private void Allid() {
        getstarted = findViewById(R.id.getstarted);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*player.stop();
        player.release();*/
    }

    @Override
    protected void onDestroy() {
     /*   player.stop();
        player.release();*/
        super.onDestroy();
    }

    private void checkversion() {
        try {

            currentVersion = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            Log.e("Current Version","::"+currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new GetVersionCode().execute();
    }
    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (onlineVersion.equals(currentVersion)) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            Intent start = new Intent(SplashActivity.this, Main.class);
                            startActivity(start);
                            finish();
                        }
                    }, 900);
                } else {
                    final Dialog dialog = new Dialog(SplashActivity.this);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.item_yesno_dialouge);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.d_round_corner_white_bkg));
                    TextView title = dialog.findViewById(R.id.title);
                    TextView des = dialog.findViewById(R.id.des);
                    title.setText("Google Playstore App Update");
                    des.setText("Important update available on playstore. Bug fix, some new features and lots more");
                    Button okBtn = dialog.findViewById(R.id.ok_btn);
                    Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
                    okBtn.setOnClickListener(view -> {
                        dialog.dismiss();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                           finish();
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                            finish();

                        }
                    });
                    dialog.show();
                }

            }

            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }
    }
}