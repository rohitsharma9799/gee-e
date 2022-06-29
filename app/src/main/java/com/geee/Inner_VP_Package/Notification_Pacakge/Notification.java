package com.geee.Inner_VP_Package.Notification_Pacakge;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.geee.Inner_VP_Package.Notification_Pacakge.Following_Package.Notifi_Following;
import com.geee.Inner_VP_Package.Notification_Pacakge.You_Package.Notifi_You;
import com.geee.CodeClasses.ViewPager_Adp;
import com.geee.R;

public class Notification extends Fragment {

    View view;
    TabLayout tl;
    ViewPager vp;
    ViewPager_Adp adp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.notification, null);

        tl = (TabLayout) view.findViewById(R.id.tablayout_id);
        vp = (ViewPager) view.findViewById(R.id.noti_viewpager_id);

        adp = new ViewPager_Adp(getChildFragmentManager());
        adp.add_fragment(new Notifi_You(), "YOU");
        adp.add_fragment(new Notifi_Following(), "FOLLOWING");

        vp.setAdapter(adp);
        vp.setOffscreenPageLimit(2);

        tl.setupWithViewPager(vp);

        return view;
    }
}

