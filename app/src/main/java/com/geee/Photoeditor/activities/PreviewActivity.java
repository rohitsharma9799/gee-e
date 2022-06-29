package com.geee.Photoeditor.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.geee.R;
import com.geee.Photoeditor.adapters.PreviewAdapter;

import java.io.File;
import java.util.ArrayList;

public class PreviewActivity extends BaseActivity {
    private PreviewActivity previewActivity;
    private ArrayList<File> files;
    private int Position = 0;
    PreviewAdapter previewAdapter;
    ViewPager viewPager;
    ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_preview);
        previewActivity = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            files = (ArrayList<File>) getIntent().getSerializableExtra("ImageDataFile");
            Position = getIntent().getIntExtra("Position", 0);
        }

        initViews();

    }

    public void initViews() {
        this.imageViewBack = findViewById(R.id.imageViewBack);
        this.viewPager = findViewById(R.id.viewPager);
        previewAdapter = new PreviewAdapter(this, files, previewActivity);
        viewPager.setAdapter(previewAdapter);
        viewPager.setCurrentItem(Position);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                Position = arg0;
                System.out.println("Current position==" + Position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int num) {
            }
        });

        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        previewActivity = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
