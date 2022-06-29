package com.geee.Main_VP_Package;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.geee.CodeClasses.Variables;
import com.geee.Login_Package.Login;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;

public class Main extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        frameLayout = (FrameLayout) findViewById(R.id.main_container_id);

        // Check if user is already logged in
        String userInfo = SharedPrefrence.getOffline(Main.this, SharedPrefrence.shared_user_FB_login_data_key);
        if (userInfo != null) {
            Intent sendIntent = new Intent(Main.this, MainActivity.class);
            startActivity(sendIntent);
            finish();
        } else {
            if (!Variables.check) {
                Login f = new Login();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container_id, f).commit();
            } else {
                Login f = new Login();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container_id, f).commit();
            }
        }
    }
}
