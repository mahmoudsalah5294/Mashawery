package com.mnm.mashawery.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mnm.mashawery.R;
import com.mnm.mashawery.Trip;
import com.mnm.mashawery.TripDataBase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    List<Trip> tripHolder=new ArrayList<>();
    private HistoryViewModel historyViewModel;
    TripDataBase tripDataBase;
    HistoryAdapter hstoryAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_history, container, false);
        tripDataBase = TripDataBase.getInstance(getContext());
        tripDataBase.tripDAO().getTrip()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Trip>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Trip> trips) {

                        for (Trip trip1 : trips) {
                            if(trip1.getState().equals("history")){
                                tripHolder.add(trip1);
                            }
                        }
                        hstoryAdapter=new HistoryAdapter(tripHolder,getContext());
                        recyclerView=(RecyclerView) view.findViewById(R.id.thelist);
                        recyclerView.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(hstoryAdapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
   //     hstoryAdapter.changeData((ArrayList<Trip>) tripHolder);
   //     hstoryAdapter.notifyDataSetChanged();



        return view;
    }
}