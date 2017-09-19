package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by andre on 13.09.17.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,
    CrimeFragment.Callbacks{


    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null){
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        Log.d("TAG", "oncrimeUPdated called");
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

}
