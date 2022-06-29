package com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.geee.CodeClasses.Variables;
import com.geee.CodeClasses.ViewPager_Adp;
import com.geee.R;

public class FollowersFollowingF extends Fragment {

    View view;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPager_Adp viewPagerAdp;
    ImageView backId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.follower_following, null);

        tabLayout =  view.findViewById(R.id.tablayout_id);
        viewPager =  view.findViewById(R.id.viewpager_id);

        backId = view.findViewById(R.id.back_id);

        final View parent = (View) backId.getParent();  // button: the view you want to enlarge hit area
        parent.post(() -> {
            final Rect rect = new Rect();
            backId.getHitRect(rect);
            rect.top -= Variables.clickAreaTop100;    // increase top hit area
            rect.left -= Variables.clickAreaLeft100;   // increase left hit area
            rect.bottom += Variables.clickAreaBottom100; // increase bottom hit area
            rect.right += Variables.clickAreaRight200;  // increase right hit area
            parent.setTouchDelegate(new TouchDelegate(rect, backId));
        });


        backId.setOnClickListener(v -> getFragmentManager().popBackStack());


        String userId = getArguments().getString("user_id");
        String fromWhere = getArguments().getString("fromWhere");


        // Set User id

        Bundle bundleLinearPosts = new Bundle();
        bundleLinearPosts.putString("user_id", "" + userId);

        // set Fragmentclass Arguments
        FollowerF follower = new FollowerF();
        follower.setArguments(bundleLinearPosts);


        Bundle bundleFollowing = new Bundle();
        bundleFollowing.putString("user_id", "" + userId);

        // set Fragmentclass Arguments
        FollowingF following = new FollowingF();
        following.setArguments(bundleLinearPosts);


        viewPagerAdp = new ViewPager_Adp(getChildFragmentManager());
        viewPagerAdp.add_fragment(follower, "followers");
        viewPagerAdp.add_fragment(following, "following");
        viewPager.setAdapter(viewPagerAdp);

        tabLayout.setupWithViewPager(viewPager);
        if (fromWhere != null && fromWhere.equals("following")) {
            viewPager.setCurrentItem(1);
        }



        return view;
    }
}
