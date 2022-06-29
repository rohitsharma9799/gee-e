package com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.geee.CodeClasses.CubeTransformer;
import com.geee.CodeClasses.Functions;
import com.geee.R;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;
import java.util.List;

public class ViewStory extends AppCompatActivity {

    public static ViewPager mPager;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    String userId;
    String position;
    List<ShowStoryDM> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        position = intent.getStringExtra("position");
        list = (List<ShowStoryDM>) intent.getSerializableExtra("storyList");
        mPager = findViewById(R.id.viewPager);
        mPager.setPageTransformer(true, new CubeTransformer());

        Functions.logDMsg("position : " + position);

        mPager.setOffscreenPageLimit(1);
        for (int i = 0; i < list.size(); i++) {
            mFragmentList.add(new Story_F(list, i));
        }

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), mFragmentList);
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(Integer.parseInt(position));

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Fragment fragment = adapter.getRegisteredFragment(position);
                ((Story_F) fragment).pause();
            }

            @Override
            public void onPageSelected(int position) {
                Functions.logDMsg("no position");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


}
