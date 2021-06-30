package com.mnm.mashawery.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.mnm.mashawery.Trip;
import com.mnm.mashawery.TripDataBase;

import java.util.ArrayList;
import java.util.Calendar;

public class FireAlarm {
    public static final String TAG = "mahmoud";
    private Context context;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent intent;
    long timeID;
    int position;
    private static FireAlarm instance;

    ArrayList<Trip> tripAlarm = new ArrayList<>();
    Trip myTrip;

    private TripDataBase tripDataBase;

//    private FireAlarm(Context context, int year, int month, int day, int hour, int minute) {
//        calendar = Calendar.getInstance();
//
//
//
//        this.context = context;
//        this.year = year;
//        this.month = month;
//        this.day = day;
//        this.hour = hour;
//        this.minute = minute;
//    }

    public FireAlarm(Context context, int p, Trip trip) {
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        timeID = trip.getId();
        position = p;
        myTrip = trip;
        String[] split = trip.getTripdate().split("/");
        String[] split1 = trip.getTriptime().split(String.valueOf(':'));
        day = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1]);
        year = Integer.parseInt(split[2]);



        hour = Integer.parseInt(split1[0]);
        minute = Integer.parseInt(split1[1]);



    }

    public FireAlarm(Context context) {
        this.context = context;
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
}

    public FireAlarm(Context context, Trip myTrip) {
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        this.context = context;
        this.myTrip = myTrip;


        this.context = context;


        String[] split = myTrip.getTripdate().split("/");
        String[] split1 = myTrip.getTriptime().split(String.valueOf(':'));
        day = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1]);
        year = Integer.parseInt(split[2]);
        hour = Integer.parseInt(split1[0]);
        minute = Integer.parseInt(split1[1]);
    }

    //    public static FireAlarm getInstance(Context context,int p,Trip trip) {
//        if (instance == null) {
//            synchronized (FireAlarm.class) {
//                if (instance == null) {
//                    instance = new FireAlarm(context,p,trip);
//
//                }
//            }
//        }
//        return instance;
//    }

    public void setAlarm(int p, Trip trip,int id) {

        String[] split = trip.getTripdate().split("/");
        String[] split1 = trip.getTriptime().split(String.valueOf(':'));
        int day = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int year = Integer.parseInt(split[2]);
        int hour = Integer.parseInt(split1[0]);
        int minute = Integer.parseInt(split1[1]);
        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.MONTH, month - 1);

        calendar.set(Calendar.DAY_OF_MONTH, day);

        calendar.set(Calendar.HOUR_OF_DAY, hour);

        calendar.set(Calendar.MINUTE, minute);

        calendar.set(Calendar.SECOND, 0);

        Log.i("mahmoud",calendar.getTime().toString());

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, MyReceiver.class);
        //intent.putExtra("alarm", "start");
//        intent.putExtra("id", trip.getId());
        GsonTrip gsonTrip = new GsonTrip();
        intent.putExtra("object",gsonTrip.fromTripToGson(trip));
//        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//        intent.setAction("alarm.MyReceiver");
//        Log.i("mahmoud","setfirealarmID: "+id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            Log.i("mahmoud","alarmset");
        } else {

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
//        switch (p) {
//            case 0:
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                    Log.i("mahmoud","firstSwitch");
//                } else {
//
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                }
//                break;
//            case 1:
//
//                repeatedMethod(alarmManager,calendar,pendingIntent, AlarmManager.INTERVAL_DAY);
//                break;
//            case 2:
//
//                repeatedMethod(alarmManager,calendar,pendingIntent,(7*AlarmManager.INTERVAL_DAY));
//                break;
//            case 3:
//
//                repeatedMethod(alarmManager,calendar,pendingIntent,(30*AlarmManager.INTERVAL_DAY));
//                break;
//        }
    }

    private static void repeatedMethod(AlarmManager alarmManager,Calendar calendar,PendingIntent pending, long l) {
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), l, pending);
    }


    public void cancelAlarm(int id){

        intent = new Intent(context, MyReceiver.class);
        intent.setAction("alarm.MyReceiver");

        pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.i(FireAlarm.TAG,"alarmcancelID: "+id);
    }

    public void editAlarm(int p, Trip trip){
        cancelAlarm((int)trip.getId());
        setAlarm(p,trip,(int)trip.getId());
    }


}



