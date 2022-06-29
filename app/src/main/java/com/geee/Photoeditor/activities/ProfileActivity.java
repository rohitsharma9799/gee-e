package com.geee.Photoeditor.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.geee.Photoeditor.Editor.PolishDripView;
import com.geee.R;
import com.geee.Photoeditor.adapters.ProfileAdapter;
import com.geee.Photoeditor.drip.imagescale.TouchListener;
import com.geee.Photoeditor.listener.LayoutItemListener;
import com.geee.Photoeditor.support.MyExceptionHandlerPix;
import com.geee.Photoeditor.utils.BitmapTransfer;
import com.geee.Photoeditor.utils.DripFrameLayout;
import com.geee.Photoeditor.utils.DripUtils;
import com.geee.Photoeditor.utils.ImageUtils;

import java.util.ArrayList;


public class ProfileActivity extends BaseActivity implements LayoutItemListener {
    public static Bitmap resultBmp;
    private static Bitmap faceBitmap;
    private Bitmap selectedBitmap;
    private Bitmap cutBitmap;
    private Bitmap mainBitmap = null;
    private Bitmap OverLayBackground = null;
    private boolean isFirst = true;
    private boolean isReady = false;
    private PolishDripView dripViewStyle;
    private PolishDripView dripViewBack;
//    String URL_APP_1 = "co";
    private DripFrameLayout frameLayoutBackground;
    private RecyclerView recyclerViewStyle;
    private ProfileAdapter dripItemAdapter;
    private ArrayList<String> dripEffectList = new ArrayList<String>();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_profile);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(ProfileActivity.this));
        this.dripViewStyle = findViewById(R.id.dripViewStyle);
        this.dripViewBack = findViewById(R.id.dripViewBack);
        recyclerViewStyle = (RecyclerView) findViewById(R.id.recyclerViewStyle);
        this.frameLayoutBackground = findViewById(R.id.frameLayoutBackground);
//        if (checkApp(URL_APP_2))  URL_APP_1= null;
//        URL_APP_1.charAt(0);
        this.dripViewBack.setOnTouchListenerCustom(new TouchListener());

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dripViewBack.post(new Runnable() {
                    public void run() {
                        if (isFirst) {
                            isFirst = false;
                            initBitmap();
                        }
                    }
                });
            }
        },  1000);


        findViewById(R.id.imageViewCloseSplash).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.imageViewSaveSplash).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new saveFile().execute();
            }
        });

        for (int i = 1; i <= 60; i++) {
            dripEffectList.add("frame_" + i);
        }

        recyclerViewStyle.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        setDripList();
        dripViewBack.post(new Runnable() {
            public void run() {
                initBitmap();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (resultBmp != null) {
                cutBitmap = resultBmp;
                dripViewBack.setImageBitmap(cutBitmap);
                isReady = true;
                Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(ProfileActivity.this, "profile/" + dripItemAdapter.getItemList().get(dripItemAdapter.selectedPos) + ".webp");
                if (!"none".equals(dripItemAdapter.getItemList().get(0))) {
                    OverLayBackground = bitmapFromAsset;
                }
            }
        }
    }

    public static void setFaceBitmap(Bitmap bitmap) {
        faceBitmap = bitmap;
    }


    private void initBitmap() {
        if (faceBitmap != null) {
            selectedBitmap = ImageUtils.getBitmapResize(ProfileActivity.this, faceBitmap, 1024, 1024);
            mainBitmap = Bitmap.createScaledBitmap(DripUtils.getBitmapFromAsset(ProfileActivity.this, "white.webp"), selectedBitmap.getWidth(), selectedBitmap.getHeight(), true);
            Glide.with(this).load(Integer.valueOf(R.drawable.frame_1)).into(dripViewStyle);
            dripViewBack.setImageBitmap(selectedBitmap);
        }
    }
//    String URL_APP_2 = "m.pC";
    public void onLayoutListClick(View view, int i) {
        Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(ProfileActivity.this, "profile/" + dripItemAdapter.getItemList().get(i) + ".webp");
            if (!"none".equals(dripItemAdapter.getItemList().get(i))) {
                OverLayBackground = bitmapFromAsset;
                dripViewStyle.setImageBitmap(OverLayBackground);
                return;
            }
            OverLayBackground = null;
    }

    public void setDripList() {
        dripItemAdapter = new ProfileAdapter(ProfileActivity.this);
        dripItemAdapter.setClickListener(this);
        recyclerViewStyle.setAdapter(dripItemAdapter);
        dripItemAdapter.addData(dripEffectList);
    }
//    String URL_APP_3 = "ode.picSho";

    private class saveFile extends android.os.AsyncTask<String, Bitmap, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public Bitmap getBitmapFromView(View view) {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            frameLayoutBackground.setDrawingCacheEnabled(true);
            try {
                return getBitmapFromView(frameLayoutBackground);
            } catch (Exception e) {
                return null;
            } finally {
                frameLayoutBackground.setDrawingCacheEnabled(false);
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                BitmapTransfer.bitmap = bitmap;
            }
            Intent intent = new Intent(ProfileActivity.this, PhotoEditorActivity.class);
            intent.putExtra("MESSAGE","done");
            setResult(RESULT_OK,intent);
            finish();
        }
    }

}
