package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

/**
 * Created by andre on 13.09.17.
 */

public class CrimeFragment extends Fragment{

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHONE = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeID);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });
        updateDate();

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                        .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));
                startActivity(i);

            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        mCallSuspectButton = (Button) v.findViewById(R.id.call_suspect_button);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent with phone number;
                if (mCrime.getSuspectPhoneNumber() != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mCrime.getSuspectPhoneNumber()));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Nobody to call", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
            mCallSuspectButton.setText("Call " + mCrime.getSuspect());
        }



        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.setEnabled(false);
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME};

            // Perform your query - the contactUri is like a "where"
            // clause here
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            String suspect_id;
            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name
                c.moveToFirst();
                String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                suspect_id = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));

                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }

            contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            queryFields = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
            c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ? ", new String[] { suspect_id }, null);

            try {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                mCallSuspectButton.setEnabled(true);
                mCallSuspectButton.setText(phoneNumber);
                mCrime.setSuspectPhoneNumber(phoneNumber);
            } finally {
                c.close();
            }



        }



    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getCrimeReport(){
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }
}
