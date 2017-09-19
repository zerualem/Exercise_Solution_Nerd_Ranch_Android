package com.bignerdranch.android.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 16.09.17.
 */

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private GridLayoutManager mLayoutManager;
    private int mCurrentPageNumber;
    private PhotoAdapter mPhotoAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();

    public static PhotoGalleryFragment newInstance(){
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCurrentPageNumber = 1;
        new FetchItemTask().execute(mCurrentPageNumber++);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView  = (RecyclerView) v.findViewById(R.id.photo_recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mPhotoRecyclerView.setLayoutManager(mLayoutManager);
        mPhotoRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    new FetchItemTask().execute(mCurrentPageNumber++);
                }
            }
        });
        mPhotoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mPhotoRecyclerView.getWidth();
                int widthForImage = 360;
                int columns = 1;
                if (width > widthForImage){
                    columns = width / widthForImage;
                }
                mLayoutManager.setSpanCount(columns);
                Log.i(TAG, "columns" + columns );

            }
        });

        updateAdapter();

        return v;
    }

    private void updateAdapter() {
        if (isAdded()){
            mPhotoAdapter = new PhotoAdapter(mItems);
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        }
    }


    private class PhotoHolder extends RecyclerView.ViewHolder{


        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item){
            mTitleTextView.setText(item.toString());
        }

    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems){
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            holder.bindGalleryItem(galleryItem);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        public void setGalleryItems(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }
    }


    private class FetchItemTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Integer... pageNumber) {

            return new FlickrFetchr().fetchItems(pageNumber[0]);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems.addAll(items);
            updateAdapter();

        }
    }
}
