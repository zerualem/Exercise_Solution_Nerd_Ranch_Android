package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

/**
 * Created by andre on 13.09.17.
 */

public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Button mFirstButton;
    private Button mLastButton;
    private List<Crime> mCrimes;

    private static final String EXTRA_CRIME_ID = "crime_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newInstance(mCrimes.get(position).getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        for (int i = 0; i < mCrimes.size(); i++){
            if (mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mFirstButton = (Button) findViewById(R.id.first_button);
        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);

            }
        });
        mLastButton = (Button) findViewById(R.id.last_button);
        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mCrimes.size() - 1);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    mFirstButton.setEnabled(false);
                    mLastButton.setEnabled(true);
                } else if (position == mCrimes.size() - 1){
                    mLastButton.setEnabled(false);
                    mFirstButton.setEnabled(true);
                } else {
                    mFirstButton.setEnabled(true);
                    mLastButton.setEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}
