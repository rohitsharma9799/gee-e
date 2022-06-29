package com.geee.Inner_VP_Package;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.google.android.material.tabs.TabLayout;
import com.geee.BackStack.RootFragment;
import com.geee.CodeClasses.ViewPager_Adp;
import com.geee.Inner_VP_Package.Add_picture_Package.GalleryF;
import com.geee.Inner_VP_Package.Home_Package.Home;
import com.geee.Inner_VP_Package.Notification_Pacakge.Notification;
import com.geee.Inner_VP_Package.Profile_Pacakge.Profile;
import com.geee.Inner_VP_Package.Search_Pacakge.ExploreF;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main_F extends RootFragment {

    public static TabLayout tabLayout;
    public static CustomViewpager customViewpager;
    View view;
    ViewPager_Adp viewPagerAdp;
    public static FrameLayout bottombarid;
    public static RelativeLayout mainsnacklayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.main_f, null);
        bottombarid = view.findViewById(R.id.bottombarid);
        mainsnacklayout = view.findViewById(R.id.mainsnacklayout);
        Bottompanel bottompanel = new Bottompanel();
        tabLayout = view.findViewById(R.id.tablayout_id);
        customViewpager = view.findViewById(R.id.viewpager_id);
        viewPagerAdp = new ViewPager_Adp(getActivity().getSupportFragmentManager());
        viewPagerAdp.add_fragment(new Home(), "");
        viewPagerAdp.add_fragment(new ExploreF(), "");
        viewPagerAdp.add_fragment(new GalleryF(), "");
        viewPagerAdp.add_fragment(new Notification(), "");
        viewPagerAdp.add_fragment(new Profile(), "");
        customViewpager.setOffscreenPageLimit(4);
        customViewpager.setAdapter(viewPagerAdp);

        tabLayout.setupWithViewPager(customViewpager);

        final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView1 =  view1.findViewById(R.id.imageview_id);
        imageView1.setColorFilter(getActivity().getResources().getColor(R.color.white));
        imageView1.setImageResource(R.drawable.menuiconn);
        tabLayout.getTabAt(0).setCustomView(view1);

        final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView2 =  view2.findViewById(R.id.imageview_id);
        imageView2.setColorFilter(getActivity().getResources().getColor(R.color.white));
        imageView2.setImageResource(R.drawable.saerchh);
        tabLayout.getTabAt(1).setCustomView(view2);

        final View view3 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView3 =  view3.findViewById(R.id.imageview_id);
        imageView3.setImageResource(R.drawable.addmesd);
        tabLayout.getTabAt(2).setCustomView(view3);

        final View view4 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layoutds, null);
        ImageView imageView4 =  view4.findViewById(R.id.imageview_idnoti);
        imageView4.setColorFilter(getActivity().getResources().getColor(R.color.white));

        imageView4.setImageResource(R.drawable.notifica);
        tabLayout.getTabAt(3).setCustomView(view4);

        final View view5 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView5 =  view5.findViewById(R.id.imageview_id);
        imageView5.setImageResource(R.drawable.profffff);
        tabLayout.getTabAt(4).setCustomView(view5);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.imageview_id);
                ImageView imaged = view.findViewById(R.id.imageview_idnoti);
                switch (tab.getPosition()) {
                    case 0:
                        image.setImageResource(R.drawable.menuiconn);
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));

                        MainActivity.customMainViewPager.setPagingEnabled(false);
                        break;
                    case 1:
                        image.setImageResource(R.drawable.saerchh);
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));

                        MainActivity.customMainViewPager.setPagingEnabled(false);
                        break;
                    case 2:
                        image.setImageResource(R.drawable.addmesd);

                        break;
                    case 3:
                        imaged.setImageResource(R.drawable.notifica);
                        imaged.setColorFilter(getActivity().getResources().getColor(R.color.white));

                        MainActivity.customMainViewPager.setPagingEnabled(false);

                        break;
                    case 4:
                        image.setImageResource(R.drawable.profffff);
                        MainActivity.customMainViewPager.setPagingEnabled(false);
                        break;

                    default:
                        break;
                }
                tab.setCustomView(view);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.imageview_id);
                ImageView imaged = view.findViewById(R.id.imageview_idnoti);

                switch (tab.getPosition()) {
                    case 0:
                        image.setImageResource(R.drawable.menuiconn);
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));

                        MainActivity.customMainViewPager.setPagingEnabled(false);
                        break;
                    case 1:
                        image.setImageResource(R.drawable.saerchh);
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));

                        MainActivity.customMainViewPager.setPagingEnabled(false);
                        break;
                    case 2:
                        image.setImageResource(R.drawable.createe);
                        MainActivity.customMainViewPager.setPagingEnabled(false);
                        break;
                    case 3:
                        imaged.setImageResource(R.drawable.notifica);
                        imaged.setColorFilter(getActivity().getResources().getColor(R.color.white));

                        MainActivity.customMainViewPager.setPagingEnabled(false);
                        break;
                    case 4:
                        image.setImageResource(R.drawable.profffff);
                        MainActivity.customMainViewPager.setPagingEnabled(false);
                        break;

                    default:
                        break;
                }
                tab.setCustomView(view);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        getUserFollwerReqWithUserId();

        return view;
    }


   /* public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }*/
   public void getUserFollwerReqWithUserId() {

       initVolleyCallbackRequest();
       Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getActivity());

       try {
           JSONObject sendObj = new JSONObject();
           sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getActivity()));
           Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Show_Follow_Request, sendObj);

       } catch (Exception v) {
           v.printStackTrace();
           Functions.logDMsg("notification error : " + v.toString());
       }
   } // End method to getfollwer

    // Initialize Interface Call Backs
    void initVolleyCallbackRequest() {
        ImageView imaged = view.findViewById(R.id.imageview_idnoti);
        RelativeLayout badge = view.findViewById(R.id.badge);
        TextView tv_request = view.findViewById(R.id.tv_request);
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    JSONArray msgArr = response.getJSONArray("msg");
                    // Save Response for Offline Showing Data
                    if (msgArr.length() > 0) {
                        badge.setVisibility(View.VISIBLE);
                        for (int i = 0; i < msgArr.length(); i++) {
                            tv_request.setText("" + msgArr.length());
                        }
                    } else {
                        badge.setVisibility(View.GONE);

                    }
                } catch (Exception v) {
                    v.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }

}
