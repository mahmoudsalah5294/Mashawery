package com.mnm.mashawery.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i(FireAlarm.TAG,"myReceiver");

//        Intent intent2 = new Intent("android.intent.action.MAIN");
//        intent2.setClass(context, DialogActivity.class);
//        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent2);

//        if(intent.getAction() != null) {
            // if (intent.getStringExtra("alarm").equals("start")){
            Intent start = new Intent("android.intent.action.MAIN");
            start.setClass(context, DialogActivity.class);
            start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Log.i(FireAlarm.TAG,"myreciver:inside");
            start.putExtra("id", intent.getIntExtra("id", -1));
            start.putExtra("object", intent.getStringExtra("object"));
            context.startActivity(start);
//        }
//        }
//            String alarm = intent.getStringExtra("alarm");
//            if (alarm.equals("start")) {
//                Intent startIntent = new Intent(context, MyJobIntentServices.class);
//                MyJobIntentServices.enqueueWork(context,startIntent);
//            }
//
//        if (intent != null) {
//            if (intent.getAction().equals("openApp")) {
//                Log.i(FireAlarm.TAG, "openApp");
//                Intent startActivity = new Intent(context, MainActivity.class);
//                startActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(startActivity);
//            }
//        }
    }
}