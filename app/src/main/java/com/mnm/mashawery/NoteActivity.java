package com.mnm.mashawery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoteActivity extends AppCompatActivity {
    Button addnotes;
    TextView Notes;
    EditText notestxt;
    RecyclerView recyclerView;
    List<String> mynotes;
    ImageView cancel;
    NoteAdapter noteAdapter;
    Trip mytrip;
    TripDataBase tripDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
         tripDataBase = TripDataBase.getInstance(this);
        addnotes = (Button) findViewById(R.id.addbtn);
        notestxt = (EditText) findViewById(R.id.notetxt);
        Notes = (TextView) findViewById(R.id.textView2);
        addnotes = (Button) findViewById(R.id.addbtn);
        cancel = (ImageView) findViewById(R.id.imageView2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        String tripname = getIntent().getStringExtra("Tripname");
        tripDataBase.tripDAO().getTrip().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Trip>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Trip> trips) {
                        for (Trip t : trips) {
                            if (t.getTripname().equals(tripname)) {
                                mytrip=t;
                                mynotes = t.getNotes();
                                break;
                            }

                        }
                        noteAdapter = new NoteAdapter(getApplicationContext(), mynotes);
                        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(noteAdapter);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("NoteActivity", "onError: ");
                    }
                });

        addnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mynotes.add(notestxt.getText().toString());
                noteAdapter = new NoteAdapter(getApplicationContext(), mynotes);
                recyclerView.setAdapter(noteAdapter);
                notestxt.setText("");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mytrip.setNotes(mynotes);
                tripDataBase.tripDAO()
                        .updatenotes(mytrip)
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
//                                Toast.makeText(getApplicationContext(), "DoneNotes", Toast.LENGTH_SHORT).show();
                                Log.i("NoteActivity", "onComplete: ");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mytrip.setNotes(mynotes);
        tripDataBase.tripDAO()
                .updatenotes(mytrip)
                .subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
//                                Toast.makeText(getApplicationContext(), "DoneNotes", Toast.LENGTH_SHORT).show();
                        Log.i("NoteActivity", "onComplete: ");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}