package com.geee.Main_VP_Package;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.geee.tictokcode.SimpleClasses.Variables;
import com.google.firebase.messaging.FirebaseMessaging;
import com.geee.CodeClasses.ViewPager_Adp;
import com.geee.Inbox.Chat_inbox_F;
import com.geee.Inner_VP_Package.Main_F;
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.Item_Details;
import com.geee.Inner_VP_Package.Profile_Pacakge.Profile;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;

public class MainActivity extends AppCompatActivity {

    public static Custom_Main_ViewPager customMainViewPager;
    ViewPager_Adp viewPagerAdp;
    long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Variables.screen_height= displayMetrics.heightPixels;
        Variables.screen_width= displayMetrics.widthPixels;
        Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);
        Variables.user_id=Variables.sharedPreferences.getString(Variables.u_id,"");
        Variables.user_name=Variables.sharedPreferences.getString(Variables.u_name,"");
        Variables.user_pic=Variables.sharedPreferences.getString(Variables.u_pic,"");
        customMainViewPager = findViewById(R.id.Main_viewpager_id);
        viewPagerAdp = new ViewPager_Adp(getSupportFragmentManager());

        viewPagerAdp.add_fragment(new Main_F(), "");

        customMainViewPager.setAdapter(viewPagerAdp);

        customMainViewPager.setCurrentItem(0);


        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);

                if (key.equals("AnotherActivity") && value.equals("True")) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("value", value);
                    startActivity(intent);
                    finish();
                }

            }
        }

        subscribeToPushService();

    }


    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    String token = task.getResult();
                    SharedPrefrence.saveToken(MainActivity.this, token);
                });
    }


    @Override
    public void onBackPressed() {
        Main_F.bottombarid.setVisibility(View.VISIBLE);

        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (Main_F.customViewpager.getCurrentItem() == 1) {
                Main_F.customViewpager.setCurrentItem(0);
            } else if (Main_F.customViewpager.getCurrentItem() == 2) {
                Main_F.customViewpager.setCurrentItem(0);
            } else if (Main_F.customViewpager.getCurrentItem() == 3) {
                Main_F.customViewpager.setCurrentItem(0);
            } else if (Main_F.customViewpager.getCurrentItem() == 4) {
                Main_F.customViewpager.setCurrentItem(0);
            } else if (Main_F.customViewpager.getCurrentItem() == 5) {
                Main_F.customViewpager.setCurrentItem(0);
            } else {
                if (mBackPressed + 2000 > System.currentTimeMillis()) {
                    Main_F.bottombarid.setVisibility(View.VISIBLE);
                    super.onBackPressed();
                    return;
                } else {
                    Toast.makeText(getBaseContext(), "Tap again", Toast.LENGTH_SHORT).show();
                    mBackPressed = System.currentTimeMillis();
                }
            }
        } else {
            super.onBackPressed();
        }

    }

    public void openInbox() {
        Chat_inbox_F objChat = new Chat_inbox_F();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.main_container, objChat).commit();

    }

    public void openPostDetail(String postId, String userId) {

        Bundle bundle = new Bundle();
        bundle.putString("post_id", "" + postId);
        bundle.putString("user_id", "" + userId);
        bundle.putString("type", "showAllPosts");
        bundle.putString("fragment", "other");
        Item_Details f = new Item_Details();
        f.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.main_container, f).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof Profile)
                fragment.onActivityResult(requestCode, resultCode, data);
            else if (fragment instanceof Main_F) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

    }


}
