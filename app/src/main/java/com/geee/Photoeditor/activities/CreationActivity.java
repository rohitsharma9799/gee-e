package com.geee.Photoeditor.activities;

import static com.geee.Photoeditor.utils.SaveFileUtils.createFolder;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.geee.Photoeditor.fragment.CreationFragment;
import com.geee.R;

public class CreationActivity extends BaseActivity {


    public void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        super.onCreate(bundle);
        setContentView(R.layout.activity_creation);


        setFragment();
        createFolder();


        findViewById(R.id.image_view_exit).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    protected void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new CreationFragment());
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
