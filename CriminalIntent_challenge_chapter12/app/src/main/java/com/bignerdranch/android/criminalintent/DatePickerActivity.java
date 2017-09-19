package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;

/**
 * Created by andre on 14.09.17.
 */

public class DatePickerActivity extends SingleFragmentActivity {

    private static final String EXTRA_DATE = "extra_date";


    public static Intent createIntent(Context context, Date date){
        Intent intent = new Intent(context, DatePickerActivity.class);
        intent.putExtra(EXTRA_DATE, date);
        return intent;
    }


    @Override
    public Fragment createFragment() {

        Date date = (Date) getIntent().getSerializableExtra(EXTRA_DATE);
        return DatePickerFragment.newInstance(date);
    }
}
