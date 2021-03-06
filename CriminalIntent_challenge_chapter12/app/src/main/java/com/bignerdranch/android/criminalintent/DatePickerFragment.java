package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by andre on 14.09.17.
 */

public class DatePickerFragment extends DialogFragment {


    public static final String EXTRA_DATE = "criminalintent.date";

    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    private Button mOkButton;

    public static DatePickerFragment newInstance(Date date){

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_date, null);
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        mOkButton = (Button) v.findViewById(R.id.dialog_ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Date date1 = new GregorianCalendar(year, month, day).getTime();
                sendResult(Activity.RESULT_OK, date1);
                if (getTargetFragment() != null)
                    dismiss();
                else
                    getActivity().finish();
            }
        });

        return v;
    }


    private void sendResult(int resultCode, Date date){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        if (getTargetFragment() != null){
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        } else {
            getActivity().setResult(Activity.RESULT_OK, intent);
        }


    }
}
