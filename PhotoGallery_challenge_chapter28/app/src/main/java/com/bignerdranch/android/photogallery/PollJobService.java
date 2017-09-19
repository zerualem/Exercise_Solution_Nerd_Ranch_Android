package com.bignerdranch.android.photogallery;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.List;

/**
 * Created by andre on 17.09.17.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PollJobService extends JobService {

    private PollTask mCurrentTask;

    private static final String TAG = "PollJobService";

    public static void setJobServiceAlarm(Context context){
        final int JOB_ID = 1;
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        boolean hasBeenScheduled = false;
        for (JobInfo jobInfo : scheduler.getAllPendingJobs()){
            if (jobInfo.getId() == JOB_ID){
                hasBeenScheduled = true;
            }
        }

        if (!hasBeenScheduled){
            Log.i(TAG, "Scheduling new job");
            JobInfo jobInfo = new JobInfo.Builder(JOB_ID, new ComponentName(context, PollJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPeriodic(1000 * 60 * 15)
                    .setPersisted(false)
                    .build();
            scheduler.schedule(jobInfo);
        } else {
            Log.i(TAG, "Job already scheduled");
        }
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mCurrentTask = new PollTask();
        mCurrentTask.execute(jobParameters);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mCurrentTask != null){
            mCurrentTask.cancel(true);
        }
        return true;
    }

    private class PollTask extends AsyncTask<JobParameters, Void, Void>{

        String mQuery;
        String mLastResultId;
        List<GalleryItem> mItems;


        @Override
        protected void onPreExecute() {
            mQuery = QueryPreferences.getStoredQuery(PollJobService.this);
            mLastResultId = QueryPreferences.getLastResultId(PollJobService.this);

        }

        @Override
        protected Void doInBackground(JobParameters... jobParameters) {
            if (mQuery == null){
                mItems = new FlickrFetchr().fetchRecentPhotos();
            } else {
                mItems = new FlickrFetchr().searchPhotos(mQuery);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String resultId = mItems.get(0).getId();
            if (resultId.equals(mLastResultId)){
                Log.i(TAG, "Got an old result: " + resultId);
            } else {
                Log.i(TAG, "Got a new result: " + resultId);
            }

            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent(PollJobService.this);
            PendingIntent pi = PendingIntent.getActivity(PollJobService.this, 0, i, 0);

            Notification notification = new NotificationCompat.Builder(PollJobService.this)
                    .setTicker(resources.getString(R.string.new_picture_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_picture_title))
                    .setContentText(resources.getString(R.string.new_picture_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PollJobService.this);
            notificationManager.notify(0, notification);

            QueryPreferences.setLastResultId(PollJobService.this, resultId);
        }
    }
}
