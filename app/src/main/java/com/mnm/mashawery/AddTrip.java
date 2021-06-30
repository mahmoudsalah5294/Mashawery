package com.mnm.mashawery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;



import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import com.google.android.libraries.places.widget.Autocomplete;

import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mnm.mashawery.alarm.FireAlarm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddTrip extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener dateSetListener;
    ImageView calenderImage, alarmImage;
    final Context context = AddTrip.this;
    EditText tripname;
    EditText frompoint;
    EditText topoint;
    Spinner repeated;
    Spinner way;
    ConstraintLayout background;
    String thedate;
    String thetime;
    ImageButton add;
    ImageButton cancel;
    boolean Roundtrip = false;
    Calendar calendar;
    FireAlarm fireAlarm;
    boolean timeOpen;
    boolean dateOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        calendar = Calendar.getInstance();
        tripname = (EditText) findViewById(R.id.tripNameTxt);
        frompoint =  (EditText) findViewById(R.id.fromPoint);
        topoint =  (EditText) findViewById(R.id.toPoint);
        repeated = (Spinner) findViewById(R.id.repeatedSpinner);
        way = (Spinner) findViewById(R.id.tripSpinner);
        add = (ImageButton) findViewById(R.id.button);
        cancel = (ImageButton) findViewById(R.id.button2);
        background = findViewById(R.id.backgorund);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean is24Hour = false;
        final TripDataBase tripDataBase = TripDataBase.getInstance(this);
        DialogPicker dialogPicker = new DialogPicker();

        timeOpen = false;

        dateOpen = false;


        //autocomplete
        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyCJ11f_J69usIFnVrYekzZ6sh1nZhPm6LM");

        }
        frompoint.setFocusable(false);
        topoint.setFocusable(false);
        frompoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AddTrip.this);
                startActivityForResult(intent, 0);
            }
        });
        topoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AddTrip.this);
                startActivityForResult(intent, 1);

            }
        });
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        calenderImage = findViewById(R.id.calenderImage);
        alarmImage = findViewById(R.id.alarmImage);
        calenderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPicker.datePickerDialog(context, year, month, day);
                dateOpen = true;
            }
        });
        alarmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPicker.timePickerDialog(context, hour, minute, is24Hour);
            timeOpen = true;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frompoint.getText().toString().trim().equals("")) {
                    Toasty.error(AddTrip.this,"Please put your Start Point").show();
                }
                if (topoint.getText().toString().trim().equals("")) {
                    Toasty.error(AddTrip.this,"Please put your Destination Point").show();
                }
                if (tripname.getText().toString().trim().equals("")) {
                    Toasty.error(AddTrip.this,"Please put your Trip Name").show();
                }
                if (timeOpen==false||dateOpen==false){
                    Toasty.error(AddTrip.this,"Please select time and date").show();
                }
                if ((!frompoint.getText().toString().trim().equals("")) && (!topoint.getText().toString().trim().equals("")) && (!tripname.getText().toString().trim().equals(""))&&dateOpen==true&&timeOpen==true) {
                    String theRepeatation = repeated.getSelectedItem().toString() + "," + String.valueOf(repeated.getSelectedItemPosition());
                    String theway = way.getSelectedItem().toString() + "," + String.valueOf(way.getSelectedItemPosition());
                    Trip upcoming = new Trip(tripname.getText().toString(), frompoint.getText().toString(), topoint.getText().toString(), DialogPicker.date, DialogPicker.time, theRepeatation, "upcoming", theway, new ArrayList<>());
                    if (way.getSelectedItem().toString().equals("Round Trip"))
                        Roundtrip = true;
                    tripDataBase.tripDAO()
                            .insertTrip(upcoming)
                            .subscribeOn(Schedulers.computation())
                            .subscribe(new SingleObserver<Long>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@NonNull Long aLong) {
                                    fireAlarm = new FireAlarm(AddTrip.this);
                                    fireAlarm.setAlarm(repeated.getSelectedItemPosition(), upcoming,aLong.intValue());

                                    Log.i("mahmoud", "AddTrip: " + aLong);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }
                            });
                    if (Roundtrip) {
                        Roundtrip = false;
                        tripDataBase.tripDAO()
//                                .insertTrip(new Trip(tripname.getText().toString() + "BacK", topoint.getText().toString(), frompoint.getText().toString(), DialogPicker.date, DialogPicker.time, theRepeatation, theway, "upcoming", new ArrayList<>()))
                                .insertTrip(upcoming)
                                .subscribeOn(Schedulers.computation())
                                .subscribe(new SingleObserver<Long>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(@NonNull Long aLong) {

                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }
                                });
                    }
                    Intent intent = new Intent(AddTrip.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if(requestCode== 0)
                frompoint.setText(place.getAddress());
            else if (requestCode== 1)
                topoint.setText(place.getAddress());
        }
        else {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public Calendar setTime(Calendar calendar,int year,int month,int day,int hour,int minute){
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        return calendar;
    }

    public boolean manageTime(Calendar calendar){
        if (calendar.before(Calendar.getInstance())){
            return true;
        }
        return false;
    }
}