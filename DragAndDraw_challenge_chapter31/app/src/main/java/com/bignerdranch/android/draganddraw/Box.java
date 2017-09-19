package com.bignerdranch.android.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by andre on 18.09.17.
 */

public class Box implements Parcelable{
    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin){
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }


    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mOrigin, flags);
        dest.writeParcelable(this.mCurrent, flags);
    }

    protected Box(Parcel in) {
        this.mOrigin = in.readParcelable(PointF.class.getClassLoader());
        this.mCurrent = in.readParcelable(PointF.class.getClassLoader());
    }

    public static final Creator<Box> CREATOR = new Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel source) {
            return new Box(source);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };
}
