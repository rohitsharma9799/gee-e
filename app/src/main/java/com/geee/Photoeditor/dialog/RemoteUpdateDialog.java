/*
package com.geee.Photoeditor.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.geee.BuildConfig;

import com.geee.R;

public class RemoteUpdateDialog extends Dialog {
    private Context ctx;
    private TextView textViewTitle;
    private TextView textViewUpdate;
    private TextView textViewLater;

    public RemoteUpdateDialog(@NonNull Context context, FirebaseRemoteConfig config) {
        super(context);
        this.ctx = context;
        this.config = config;

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.dialog_update);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewUpdate = findViewById(R.id.textViewUpdate);
        textViewLater = findViewById(R.id.textViewLater);


        textViewTitle.setText(ctx.getResources().getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME);

        if (config.getBoolean(RemoteUpdateUtil.IS_FORCE)){
            textViewLater.setVisibility(View.GONE);
            setCancelable(false);
        } else {
            textViewLater.setVisibility(View.VISIBLE);
        }

        textViewLater.setOnClickListener((v)->{
            dismiss();
        });

        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
            }
        });
        getWindow().setLayout(-1, -2);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    @Override
    public void onBackPressed() {
        if (config.getBoolean(RemoteUpdateUtil.IS_FORCE)) ((Activity)ctx).finish();
    }
}
*/
