package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by andre on 13.09.17.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);


    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mCrimeTitle;
        private TextView mCrimeDate;
        private Button mPoliceButton;
        private Crime mCrime;


        public CrimeHolder(LayoutInflater inflater, int layoutId, ViewGroup parent){
            super(inflater.inflate(layoutId, parent, false));
            if (layoutId == R.layout.list_item_crime_police){
                mPoliceButton = (Button) itemView.findViewById(R.id.police_button);
                mPoliceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Police called", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            mCrimeDate = (TextView) itemView.findViewById(R.id.crime_date);
            mCrimeTitle = (TextView) itemView.findViewById(R.id.crime_title);
            itemView.setOnClickListener(this);

        }

        public void bind(Crime crime){
            mCrime = crime;
            mCrimeTitle.setText(crime.getTitle());
            mCrimeDate.setText(crime.getDate().toString());
        }


        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mCrime.getTitle() + " commited on " + mCrime.getDate().toString(),Toast.LENGTH_SHORT).show();
        }
    }


    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        public static final int STANDARD_VIEW_TYPE = 0;
        public static final int POLICE_VIEW_TYPE = 1;

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == STANDARD_VIEW_TYPE)
                return new CrimeHolder(LayoutInflater.from(getActivity()), R.layout.list_item_crime, parent);
            else
                return new CrimeHolder(LayoutInflater.from(getActivity()), R.layout.list_item_crime_police, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            if (crime.isRequiresPolice()){
                return POLICE_VIEW_TYPE;
            } else {
                return STANDARD_VIEW_TYPE;
            }
        }
    }
}
