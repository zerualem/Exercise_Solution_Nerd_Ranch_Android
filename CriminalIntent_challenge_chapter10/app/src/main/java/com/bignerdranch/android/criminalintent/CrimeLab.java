package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by andre on 13.09.17.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private HashMap<UUID,Crime> mCrimeHashMap;

    private CrimeLab(Context context){
        mCrimeHashMap = new LinkedHashMap<>();
        for(int i = 0; i < 100; i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(false);
            mCrimeHashMap.put(crime.getId(), crime);
        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes(){
        return new ArrayList<>(mCrimeHashMap.values());
    }

    public Crime getCrime(UUID uuid){

        return mCrimeHashMap.get(uuid);


    }
}
