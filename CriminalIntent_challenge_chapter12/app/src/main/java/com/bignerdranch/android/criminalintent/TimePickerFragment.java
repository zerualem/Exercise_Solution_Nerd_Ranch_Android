package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by andre on 14.09.17.
 */

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "extradate";
    public static final String EXTRA_TIME = "extra_time";

    TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = v.findViewById(R.id.time_picker_dialog);

        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hours = mTimePicker.getCurrentHour();
                        int minutes = mTimePicker.getCurrentMinute();
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_TIME, hours + ":" + minutes + "a clock");
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setTitle(R.string.time_crime_text)
                .create();
    }


}
