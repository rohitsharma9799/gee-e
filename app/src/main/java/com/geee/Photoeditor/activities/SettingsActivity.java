package com.geee.Photoeditor.activities;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.geee.R;

import com.geee.Photoeditor.dialog.RateDialog;

public class SettingsActivity extends AppCompatActivity {

    public LinearLayout relativeLayoutShare;
    public LinearLayout relativeLayoutRate;
    public LinearLayout relativeLayoutFeedBack;
    public LinearLayout relativeLayoutPrivacy;
    public LinearLayout relativeLayoutApps;
    
    public void onCreate(Bundle bundle) {
        
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_settings);
        
        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                onBackPressed();
            }
        });
        this.relativeLayoutShare = findViewById(R.id.linearLayoutShare);
        this.relativeLayoutRate = findViewById(R.id.linearLayoutRate);
        this.relativeLayoutFeedBack = findViewById(R.id.linearLayoutFeedback);
        this.relativeLayoutPrivacy = findViewById(R.id.linearLayoutPrivacy);
        this.relativeLayoutApps = findViewById(R.id.linearLayoutApps);

        this.relativeLayoutShare.setOnClickListener(view -> {
            Intent myapp = new Intent(Intent.ACTION_SEND);
            myapp.setType("text/plain");
            myapp.putExtra(Intent.EXTRA_TEXT, getString(R.string.download_this) + "\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
            startActivity(myapp);
        });

        this.relativeLayoutRate.setOnClickListener(view -> {
            new RateDialog(SettingsActivity.this, false).show();
        });

        this.relativeLayoutFeedBack.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_feedback)});
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

            PackageManager packageManager = getPackageManager();
            boolean isIntentSafe = emailIntent.resolveActivity(packageManager) != null;
            if (isIntentSafe) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(this, R.string.email_app_not_installed, Toast.LENGTH_SHORT).show();
            }
        });

        this.relativeLayoutPrivacy.setOnClickListener(view -> {
            Intent privacy = new Intent(SettingsActivity.this, PolicyActivity.class);
            startActivity(privacy);
        });

        this.relativeLayoutApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qq","moreApp");
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + getString(R.string.developer_account_id))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=" + getString(R.string.developer_account_id))));
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
