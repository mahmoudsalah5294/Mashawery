package com.mnm.mashawery.alarm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.mnm.mashawery.AddTrip;

public class MyJobIntentServices extends JobIntentService {
    static Context serviceContext;
    FireAlarm fireAlarm;
    public static void enqueueWork(Context context, Intent startIntent) {
        Log.i(FireAlarm.TAG,"myjob");
        serviceContext = context;
        enqueueWork(context, MyJobIntentServices.class,123,startIntent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String id = intent.getStringExtra("id");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");


//        fireAlarm = FireAlarm.getInstance(serviceContext,id,date,time);


        //FireAlarm fireAlarm = new FireAlarm(serviceContext,2021,2,23,9,6);
        //fireAlarm.setAlarm();
    }

    public void broadcastIntent(){
        Intent brodCastIntent = new Intent();
        brodCastIntent.setAction("openApp");
        brodCastIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(brodCastIntent);
    }
}
