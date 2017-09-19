package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by andre on 13.09.17.
 */

public class CrimeListActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }
}
