package com.geee.Inner_VP_Package.Search_Pacakge.Search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Accounts_Search_Tabs_frag;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Tags_Search_Tabs_frag;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;

import java.util.ArrayList;
import java.util.List;

public class Search_thing_activity extends AppCompatActivity {
    ViewPager viewPager;
    ImageView icBack, icCross;
    EditText search;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_thing_activity);
        Intent intent = getIntent();
        String dataTransmited=intent.getStringExtra("serchtxt");
        icBack = findViewById(R.id.ic_back);
        icCross = findViewById(R.id.ic_cross);

        final View view = (View) icBack.getParent();  // button: the view you want to enlarge hit area
        view.post(() -> {
            final Rect rect = new Rect();
            icBack.getHitRect(rect);
            rect.top -= Variables.clickAreaTop100;    // increase top hit area
            rect.left -= Variables.clickAreaLeft100;   // increase left hit area
            rect.bottom += Variables.clickAreaBottom100; // increase bottom hit area
            rect.right += Variables.clickAreaRight200;  // increase right hit area
            view.setTouchDelegate(new TouchDelegate(rect, icBack));
        });


        final View parent = (View) icCross.getParent();  // button: the view you want to enlarge hit area
        parent.post(() -> {
            final Rect rect = new Rect();
            icBack.getHitRect(rect);
            rect.top -= Variables.clickAreaTop100;    // increase top hit area
            rect.left -= Variables.clickAreaLeft100;   // increase left hit area
            rect.bottom += Variables.clickAreaBottom100; // increase bottom hit area
            rect.right += Variables.clickAreaRight100;  // increase right hit area
            parent.setTouchDelegate(new TouchDelegate(rect, icCross));
        });


        icBack.setOnClickListener(v -> {
            // your handler code here
            finish();
        });

        icCross.setOnClickListener(v -> {
            // your handler code here
            search.setText("");
            icCross.setVisibility(View.GONE);
        });


        search = findViewById(R.id.search_text);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                if (search.getText().toString().length() == 0) {
                    // If lenght is zerooo
                    icCross.setVisibility(View.GONE);
                } else if (search.getText().toString().length() > 0) {
                    // if length is greater than 0
                    icCross.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { //auto generated
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) { //auto generated
            }
        });


        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(search.getText().toString());
                return true;
            }
            return false;
        });


        viewPager =  findViewById(R.id.viewpager);
        setupViewPager(viewPager, "");

        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if (!dataTransmited.equals("")){
            performSearch(dataTransmited);
            search.setText(dataTransmited);
        }


    }

    private void performSearch(String searchKey) {
        search.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(search.getWindowToken(), 0);
        setupViewPager(viewPager, searchKey);
    }


    private void setupViewPager(ViewPager viewPager, String searchKey) {

        Bundle bundle = new Bundle();
        bundle.putString("search_key", search.getText().toString());
        Accounts_Search_Tabs_frag objSearchAcccount = new Accounts_Search_Tabs_frag();
        objSearchAcccount.setArguments(bundle);

        Bundle bundleSearchKey1 = new Bundle();
        bundleSearchKey1.putString("search_key", search.getText().toString());
        Tags_Search_Tabs_frag objTagSearch = new Tags_Search_Tabs_frag();
        objTagSearch.setArguments(bundle);

        SharedPrefrence.saveInfoShare(Search_thing_activity.this, searchKey, SharedPrefrence.share_search_key);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(objSearchAcccount, "ACCOUNTS");
        adapter.addFragment(objTagSearch, "TAGS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
