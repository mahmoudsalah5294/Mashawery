package com.mnm.mashawery;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mnm.mashawery.alarm.FireAlarm;

import java.util.Arrays;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


public class EditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    DatePickerDialog.OnDateSetListener dateSetListener;
    ImageView calenderImage, alarmImage;
    EditText tripname;
    TextView frompoint;
    TextView topoint;
    Spinner repeated;
    Spinner way;
    String thedate;
    String thetime;
    Button save;
    Button cancel;
    Context context;
    Trip editedtrip;
    int year,month,day,hour,minute;
    Boolean timechanged,datechanged=false;
    public EditFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
         //   Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        container.removeAllViews();
        final TripDataBase tripDataBase = TripDataBase.getInstance(context);
        tripname = (EditText) v.findViewById(R.id.tripNameTxt);
        frompoint = (TextView) v.findViewById(R.id.fromPoint);
        topoint = (TextView) v.findViewById(R.id.toPoint);
        repeated = (Spinner) v.findViewById(R.id.repeatedSpinner);
        way = (Spinner) v.findViewById(R.id.tripSpinner);
        save = (Button) v.findViewById(R.id.button);
        cancel = (Button) v.findViewById(R.id.button2);

        Bundle bundle = this.getArguments();
        tripDataBase.tripDAO().editTrip(bundle.getLong("Id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Trip>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Trip trip) {
                        editedtrip=trip;
                        tripname.setText(trip.getTripname());
                        frompoint.setText(trip.getStartpoint());
                        topoint.setText(trip.getEndpoint());
                        String[] values = trip.getRepeatation().split(",");
                        repeated.setSelection(Integer.parseInt(values[1]));
                        values = trip.getWay().split(",");
                        way.setSelection(Integer.parseInt(values[1]));
                        values = trip.getTripdate().split("/");
                        year = Integer.parseInt(values[2]);
                        month = Integer.parseInt(values[1]) - 1;
                        day = Integer.parseInt(values[0]);
                        values = trip.getTriptime().split(":");
                        hour = Integer.parseInt(values[0]);
                        minute = Integer.parseInt(values[1]);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
        boolean is24Hour = false;
        DialogPicker dialogPicker = new DialogPicker();
        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyCJ11f_J69usIFnVrYekzZ6sh1nZhPm6LM");

        }
        frompoint.setFocusable(false);
        topoint.setFocusable(false);
        frompoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(context);
                startActivityForResult(intent, 0);
            }
        });
        topoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(context);
                startActivityForResult(intent, 1);

            }
        });
        calenderImage = v.findViewById(R.id.calenderImage);
        alarmImage = v.findViewById(R.id.alarmImage);
        calenderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPicker.datePickerDialog(getContext(), year, month, day);
                datechanged=true;

            }
        });
        alarmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPicker.timePickerDialog(getContext(), hour, minute, is24Hour);
                timechanged=true;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theRepeatation = repeated.getSelectedItem().toString() + "," + String.valueOf(repeated.getSelectedItemPosition());
                String theway = way.getSelectedItem().toString() + "," + String.valueOf(way.getSelectedItemPosition());
                editedtrip.setName(tripname.getText().toString());
                editedtrip.setStartpoint(frompoint.getText().toString());
                editedtrip.setEndpoint(topoint.getText().toString());
                if(timechanged){
                    editedtrip.setTriptime(DialogPicker.time);
                }
                else
                    editedtrip.setTriptime(hour+":"+minute);
                Log.i("EditFragment", "Triptime: "+editedtrip.getTriptime());
                if(datechanged){
                    editedtrip.setTripdate(DialogPicker.date);
                }
                else
                editedtrip.setTripdate(day+"/"+month+"/"+year);
                Log.i("EditFragment", "Tripdate: "+editedtrip.getTripdate());
                editedtrip.setWay(theway);
                editedtrip.setRepeatation(theRepeatation);
                tripDataBase.tripDAO()
                        .updatetrip(editedtrip)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                            }
                            @Override
                            public void onComplete() {
                                Log.i("mahmoud", "edit ID: "+editedtrip.getId());

                                FireAlarm fireAlarm = new FireAlarm(context);
                                fireAlarm.editAlarm(repeated.getSelectedItemPosition(),editedtrip);
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                            }
                        });
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

}