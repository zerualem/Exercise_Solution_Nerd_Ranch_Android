package com.bignerdranch.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by andre on 15.09.17.
 */

public class PhotoDialogFragment extends DialogFragment {

    private ImageView mImageView;
    private File mPhotoFile;

    private static final String KEY_FILE = "file";

    public static PhotoDialogFragment newInstance(File file){
        Bundle args = new Bundle();
        args.putSerializable(KEY_FILE, file);

        PhotoDialogFragment fragment = new PhotoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);

        mPhotoFile = (File) getArguments().getSerializable(KEY_FILE);
        mImageView = (ImageView) v.findViewById(R.id.image_zoom_dialog_photo);


        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mImageView.setImageBitmap(bitmap);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, null)
                .setTitle("Your Photo")
                .create();


    }
}
