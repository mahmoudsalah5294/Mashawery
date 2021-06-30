package com.mnm.mashawery.ui.upcomming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mnm.mashawery.adapters.Adapter;
import com.mnm.mashawery.AddTrip;
import com.mnm.mashawery.R;
import com.mnm.mashawery.Trip;
import com.mnm.mashawery.TripDataBase;
import com.mnm.mashawery.alarm.FireAlarm;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpcomingFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    private UpcommingViewModel upcommingViewModel;
    Adapter adapter;
    TripDataBase tripDataBase;
    Trip myHistory;
    FireAlarm fireAlarm;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        upcommingViewModel =
                new ViewModelProvider(this).get(UpcommingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_upcomming, container, false);

        String username = getActivity().getIntent().getStringExtra("username");

        if (username!=null){
            Snackbar.make(container,"Welcome "+username,Snackbar.LENGTH_LONG).show();
        }
        floatingActionButton = root.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), AddTrip.class));
            }
        });

        upcommingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.thelist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        tripDataBase = TripDataBase.getInstance(getContext());
        tripDataBase.tripDAO().getTrip().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Trip>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> trips) {
                        int tripID = 0;
                        Trip alarmTrip = null;
                        for(Trip t: trips){
                            if (t.getId()>tripID){
                                tripID =(int) t.getId();
                                alarmTrip = t;
                            }
                            Log.i("Upcoming", "onSuccess: "+t.getName()+" "+t.getStartpoint()+ " "+t.getEndpoint()+" "+t.getRepeatation()+" "+t.getTriptime()+" "+t.getWay()+" "+t.getTripdate()+t.getState());
                            Log.i("THE ID", "THE ID: "+String.valueOf(t.getId()));
                            if (t.getState().equals("history"))
                                myHistory = t;
                        }
                        if (alarmTrip!=null) {

                        }
                        trips.remove(myHistory);
                        adapter = new Adapter(trips,getContext(),getActivity());
                        recyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.i("UPCOMING", "onError: ");
                    }
                });

    }

    @Override
    public void onPause() {
        super.onPause();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().finish();
    }
}