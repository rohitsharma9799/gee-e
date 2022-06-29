/*
package com.geee.Photoeditor.activities;

import static com.geee.Photoeditor.ads.Config.GOOGLE_DRIVE_JSON_FILE_ID;
import static com.geee.Photoeditor.ads.Config.JSON_FILE_HOST_TYPE;
import static com.geee.Photoeditor.ads.Config.SPLASH_DURATION;
import static com.xCoding.ads.sdk.util.Constant.ADMOB;
import static com.xCoding.ads.sdk.util.Constant.AD_STATUS_ON;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.geee.Photoeditor.AppEditor;
import com.geee.R;
import com.geee.Photoeditor.ads.Ads;
import com.geee.Photoeditor.ads.AdsManager;
import com.geee.Photoeditor.ads.App;
import com.geee.Photoeditor.ads.CallbackConfig;
import com.geee.Photoeditor.ads.prefs.AdsPref;
import com.geee.Photoeditor.ads.prefs.SharedPref;
import com.geee.Photoeditor.ads.rests.RestAdapter;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.xCoding.ads.sdk.format.AppOpenAd;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity{
    public static final String TAG = "SplashScreenActivity";
    Call<CallbackConfig> callbackCall = null;
    AdsManager adsManager;
    AppOpenAd appOpenAdManager;
    private boolean isAdShown = false;
    private boolean isAdDismissed = false;
    private boolean isLoadCompleted = false;
    SharedPref sharedPref;
    AdsPref adsPref;
    App app;
    Ads ads;

    public static Context contextApp;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);
        sharedPref = new SharedPref(this);
        adsManager = new AdsManager(this);
        
        requestAPI();
        contextApp = this.getApplicationContext();
    }


    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }


    private void requestAPI() {
        if (JSON_FILE_HOST_TYPE == 0) {
            callbackCall = RestAdapter.createApiGoogleDrive().getDriveJsonFileId(GOOGLE_DRIVE_JSON_FILE_ID);
            Log.d(TAG, "Request API from Google Drive");
        } else {
            callbackCall = RestAdapter.createApiGoogleDrive().getDriveJsonFileId(GOOGLE_DRIVE_JSON_FILE_ID);
            Log.d(TAG, "Request API from Google Drive");
        }
        callbackCall.enqueue(new Callback<CallbackConfig>() {
            public void onResponse(@NonNull Call<CallbackConfig> call, @NonNull Response<CallbackConfig> response) {
                CallbackConfig resp = response.body();
                displayApiResults(resp);
            }

            public void onFailure(@NonNull Call<CallbackConfig> call, @NonNull Throwable th) {
                Log.e(TAG, "initialize failed");
                onSplashFinished();
            }
        });
    }

    private void displayApiResults(CallbackConfig resp) {

        if (resp != null) {
            app = resp.app;
            ads = resp.ads;

            if (app.status.equals("1")) {
                adsManager.saveConfig(sharedPref, app);
                adsManager.saveAds(adsPref, ads);

                onSplashFinished();
                Log.d(TAG, "App status is live");
            } else {
                Log.d(TAG, "App status is suspended");
            }
            Log.d(TAG, "initialize success");
        } else {
            Log.d(TAG, "initialize failed");
            onSplashFinished();
        }

    }

    private void onSplashFinished() {
        if (adsPref.getAdType().equals(ADMOB) && adsPref.getAdStatus().equals(AD_STATUS_ON)) {
            if (!adsPref.getAdMobAppOpenAdId().equals("0")) {
                launchAppOpenAd();
            } else {
                launchMainScreen();
            }
        } else {
            launchMainScreen();
        }
    }

    private void launchMainScreen() {
       */
/* Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);*//*

        new Handler(Looper.getMainLooper()).postDelayed(this::finish, SPLASH_DURATION);
    }

    private void launchAppOpenAd() {
        appOpenAdManager = ((AppEditor) getApplication()).getAppOpenAdManager();
        loadResources();
        appOpenAdManager.showAdIfAvailable(adsPref.getAdMobAppOpenAdId(), new FullScreenContentCallback() {

            @Override
            public void onAdShowedFullScreenContent() {
                isAdShown = true;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                isAdDismissed = true;
                if (isLoadCompleted) {
                    launchMainScreen();
                    Log.d(TAG, "isLoadCompleted and launch main screen...");
                } else {
                    Log.d(TAG, "Waiting resources to be loaded...");
                }
            }
        });
    }

    private void loadResources() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isLoadCompleted = true;
            if (isAdShown) {
                if (isAdDismissed) {
                    launchMainScreen();
                    Log.d(TAG, "isAdDismissed and launch main screen...");
                } else {
                    Log.d(TAG, "Waiting for ad to be dismissed...");
                }
                Log.d(TAG, "Ad shown...");
            } else {
                launchMainScreen();
                Log.d(TAG, "Ad not shown...");
            }
        }, SPLASH_DURATION);
    }

}
*/
