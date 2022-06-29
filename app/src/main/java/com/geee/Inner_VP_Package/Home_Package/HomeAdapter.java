package com.geee.Inner_VP_Package.Home_Package;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.geee.tictokcode.Home.Home_F;


public class HomeAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public HomeAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = c;
        this.totalTabs = totalTabs;
    }

    public HomeAdapter(FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GeeeFeed RegularVoice = new GeeeFeed();
                return RegularVoice;
          /*  case 1:
                Home_F Mtones = new Home_F();
                return Mtones;*/
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
