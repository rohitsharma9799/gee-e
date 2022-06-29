package com.geee.Photoeditor.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.geee.R;

public class ThankYouActivity extends AppCompatActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_goodbye);
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom_delay);
        findViewById(R.id.tv_thankyou_container).startAnimation(loadAnimation);
        findViewById(R.id.tv_thankyou_2).startAnimation(loadAnimation2);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 2970);
    }
}
