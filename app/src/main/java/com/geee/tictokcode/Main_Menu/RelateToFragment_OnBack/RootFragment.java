package com.geee.tictokcode.Main_Menu.RelateToFragment_OnBack;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.geee.Inner_VP_Package.Main_F;

/**
 * Created by AQEEL on 3/30/2018.
 */

public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        Main_F.bottombarid.setVisibility(View.VISIBLE);
        return new BackPressImplimentation(this).onBackPressed();
    }
}