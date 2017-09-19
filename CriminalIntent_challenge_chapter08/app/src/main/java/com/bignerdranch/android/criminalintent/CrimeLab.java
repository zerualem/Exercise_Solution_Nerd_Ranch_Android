package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by andre on 13.09.17.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(false);
            if (i % 10 == 0)
                crime.setRequiresPolice(true);
            mCrimes.add(crime);
        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID uuid){
        for(Crime crime : mCrimes){
            if (crime.getId().equals(uuid)){
                return crime;
            }
        }
        return null;
    }
}
