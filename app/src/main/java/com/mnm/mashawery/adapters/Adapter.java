package com.mnm.mashawery.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mnm.mashawery.EditFragment;
import com.mnm.mashawery.MainActivity;
import com.mnm.mashawery.NoteActivity;
import com.mnm.mashawery.R;
import com.mnm.mashawery.Trip;
import com.mnm.mashawery.TripDataBase;
import com.mnm.mashawery.alarm.FireAlarm;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Trip> values;
    private static final String TAG = "RecyclerView";
    Context context;
    Activity activity;
    Trip trip;
    final TripDataBase tripDataBase = TripDataBase.getInstance(context);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.upcommingcard, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        Log.i(TAG, "onCreateViewHolder: ");
        return viewHolder;
    }

    public Adapter(List<Trip> values, Context context, Activity activity) {
        this.values = values;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] thedate = values.get(position).getTripdate().split("/");
        switch (thedate[1]) {
            case "1":
                holder.date.setText(thedate[0] + "\n" + "JUN");
                break;
            case "2":
                holder.date.setText(thedate[0] + "\n" + "FEB");
                break;
            case "3":
                holder.date.setText(thedate[0] + "\n" + "MAR");
                break;
            case "4":
                holder.date.setText(thedate[0] + "\n" + "APR");
                break;
            case "5":
                holder.date.setText(thedate[0] + "\n" + "MAY");
                break;
            case "6":
                holder.date.setText(thedate[0] + "\n" + "JUN");
                break;
            case "7":
                holder.date.setText(thedate[0] + "\n" + "JUL");
                break;
            case "8":
                holder.date.setText(thedate[0] + "\n" + "AUG");
                break;
            case "9":
                holder.date.setText(thedate[0] + "\n" + "SEP");
                break;
            case "10":
                holder.date.setText(thedate[0] + "\n" + "OCT");
                break;
            case "11":
                holder.date.setText(thedate[0] + "\n" + "NOV");
                break;
            case "12":
                holder.date.setText(thedate[0] + "\n" + "DEC");
                break;

        }
        holder.time.setText(values.get(position).getTriptime());
        holder.from.setText(values.get(position).getStartpoint());
        holder.to.setText(values.get(position).getEndpoint());
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values.get(position).setState("history");
                try {
                    //https://www.google.com/maps/dir/?api=1&origin=Space+Needle+Seattle+WA&destination=Pike+Place+Market+Seattle+WA&travelmode=bicycling
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + values.get(position).getStartpoint() + "&destination=" + values.get(position).getEndpoint() + "&travelmode=car");
                    // Uri uri=Uri.parse("google.com/maps/d/viewer?ie=UTF8&oe=UTF8&msa=0&mid=1hR3Py02tfL4cUkYSCor0Wuf1qD8&ll=30.619438428759047%2C30.455475000000007&z=7");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                tripDataBase.tripDAO().getTrip().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<Trip>>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> trips) {
                                for (Trip t : trips) {
                                    if (t.getTripname().equals(values.get(position).getName())) {
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
//                                              Toast.makeText(getApplicationContext(), "DoneNotes", Toast.LENGTH_SHORT).show();//Log.i("NoteActivity", "onComplete: ");
                                                Log.i("DONEEEEEEEEEEEEE", "onComplete: DONe");
                                            }

                                            @Override
                                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                                            }
                                        });
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                Log.i("NoteActivity", "onError: ");
                            }
                        });
            }

        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fireid = (int) values.get(position).getId();
                tripDataBase.tripDAO()
                        .deleteTrip(values.get(position).getId())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                            }
                            @Override
                            public void onComplete() {
                                FireAlarm fireAlarm = new FireAlarm(context);
                                fireAlarm.cancelAlarm(fireid);
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            }
                        });

            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                EditFragment editFragment = new EditFragment(context);
                Bundle args = new Bundle();
                args.putLong("Id", values.get(position).getId());
                editFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Rec, editFragment).addToBackStack(null).commit();
            }
        });
        holder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteActivity.class);
                intent.putExtra("Tripname", values.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView name;
        public TextView date;
        public TextView from;
        public TextView to;
        public Button start;
        public ImageView delete;
        public ImageView edit;
        public ImageView image4;
        public ImageView notes;
        public View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            time = itemView.findViewById(R.id.time);
            // name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.from2);
            start = itemView.findViewById(R.id.Startbutton);
            delete = itemView.findViewById(R.id.Delete);
            edit = itemView.findViewById(R.id.Edit);
            image4 = itemView.findViewById(R.id.imageView4);
            notes = itemView.findViewById(R.id.notes);
        }
    }


}
