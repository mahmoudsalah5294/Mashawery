package com.mnm.mashawery.alarm;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mnm.mashawery.AddTrip;

import com.mnm.mashawery.MainActivity;
import com.mnm.mashawery.R;
import com.mnm.mashawery.SplashScreen;
import com.mnm.mashawery.Trip;
import com.mnm.mashawery.TripDataBase;
import com.siddharthks.bubbles.FloatingBubbleConfig;
import com.siddharthks.bubbles.FloatingBubblePermissions;
import com.siddharthks.bubbles.FloatingBubbleService;

import java.io.IOException;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class DialogActivity extends AppCompatActivity {
//    Double longitude = -122.2666;
//    //19.0513
//    Double latitude = 37.3271;//47.4925
    MediaPlayer mediaPlayer;
    String CHANNEL_ID = "1";
    Trip myTrip;
    TripDataBase tripDataBase;
    int id;
    List<Address> destination;
    int PERMISSION_ID = 15;
    List<Address> startPoint;
    Trip trip;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        FloatingBubblePermissions.startPermissionRequest(this);

//            setContentView(R.layout.custom_layout);
   //     AlertDialog.Builder builder = new AlertDialog.Builder(DialogActivity.this);
   //     View view = LayoutInflater.from(DialogActivity.this).inflate(R.layout.custom_layout, null);
    //    TextView title = (TextView) view.findViewById(R.id.titleId);



        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();


        tripDataBase = TripDataBase.getInstance(this);

        GsonTrip gsonTrip = new GsonTrip();
        String object = getIntent().getStringExtra("object");
        myTrip = gsonTrip.fromGsonToTrip(object);
        id = getIntent().getIntExtra("id", -1);

       // GifImageView gifImageView=findViewById(R.id.cardy);
        ImageButton btnCancel=findViewById(R.id.nobtn);
        ImageButton btnSnooze=findViewById(R.id.snoozebtn);
        ImageButton btnYes=findViewById(R.id.yesbtn);
      //  ImageButton imageButton = view.findViewById(R.id.imageId);


      //  imageButton.setImageResource(R.drawable.tw);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                startService(new Intent(getApplicationContext(), SimpleService.class));

                try {
                    Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + myTrip.getEndpoint());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mapIntent);

                    //https://www.google.com/maps/dir/?api=1&origin=Space+Needle+Seattle+WA&destination=Pike+Place+Market+Seattle+WA&travelmode=bicycling
//                    Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + MyFullAddress + "&destination=" + myTrip.getEndpoint() + "&travelmode=car");
//                    // Uri uri=Uri.parse("google.com/maps/d/viewer?ie=UTF8&oe=UTF8&msa=0&mid=1hR3Py02tfL4cUkYSCor0Wuf1qD8&ll=30.619438428759047%2C30.455475000000007&z=7");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    intent.setPackage("com.google.android.apps.maps");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);

                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

//                FloatingBubblePermissions.startPermissionRequest(DialogActivity.this);
//                startService(new Intent(getApplicationContext(), SimpleService.class));


                tripDataBase.tripDAO().getTrip().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<Trip>>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> trips) {
                                for (Trip t : trips) {
                                    if (t.getTripname().equals(myTrip.getName())) {
                                        trip = t;
                                        break;
                                    }
                                }
                                trip.setState("history");
                                tripDataBase.tripDAO()
                                        .updatehistory(trip)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new CompletableObserver() {
                                            @Override
                                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onComplete() {
//                                Toast.makeText(getApplicationContext(), "DoneNotes", Toast.LENGTH_SHORT).show();//Log.i("NoteActivity", "onComplete: ");
//                                                Log.i("DONEEEEEEEEEEEEE", "onComplete: DONe");
                                            }

                                            @Override
                                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                                Log.i("mahmoud", "Inside:onError: " + e.getMessage());
                                            }
                                        });
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                Log.i("mahmoud", "Find:onError: " + e.getMessage());
                            }
                        });
                finish();
            }

                });

        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                createNotificationChannel();
                String object = getIntent().getStringExtra("object");
                if (object!=null){
                    myTrip = gsonTrip.fromGsonToTrip(object);
                }
                Toast.makeText(DialogActivity.this, "snoooze", Toast.LENGTH_SHORT).show();
        finish();
            }
        });
     btnCancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             mediaPlayer.stop();


             Toast.makeText(DialogActivity.this, "caaaaaaaaaaaancel", Toast.LENGTH_SHORT).show();
             finish();
         }
     });


    }

    private void createNotificationChannel() {
        NotificationManagerCompat notificationMa = NotificationManagerCompat.from(this);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        GsonTrip gsonTrip = new GsonTrip();
        Intent notificationIntent = new Intent(DialogActivity.this, DialogActivity.class);
        notificationIntent.putExtra("object",gsonTrip.fromTripToGson(myTrip));
        PendingIntent pIntent = PendingIntent.getActivity(DialogActivity.this, (int) myTrip.getId(), notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


// notificationId is a unique int for each notification that you must define

            notificationMa.notify(2, new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(myTrip.getName())
                    .setContentText(myTrip.getEndpoint())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(myTrip.getStartpoint() + " -> " + myTrip.getEndpoint()))
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(pIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).build());
        } else {

            notificationMa.notify(2, new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(myTrip.getName())
                    .setContentText(myTrip.getEndpoint())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(myTrip.getStartpoint() + " -> " + myTrip.getEndpoint()))
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setContentIntent(pIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).build());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent bIntent = new Intent(this, MainActivity.class);
        startActivity(bIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
//        Intent main = new Intent(DialogActivity.this,MainActivity.class);
//        startActivity(main);
    }

    public boolean isGPSEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }
    public void enableGPS(){
        Toast.makeText(this, "Please Turn On Location", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }


}