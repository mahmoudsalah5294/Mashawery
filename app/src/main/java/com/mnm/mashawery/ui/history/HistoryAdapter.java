package com.mnm.mashawery.ui.history;

import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mnm.mashawery.R;
import com.mnm.mashawery.Trip;
import com.mnm.mashawery.TripDataBase;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    List<Trip> tripList;
    private static final String TAG="HistoryAdapter";
    Context context;
    final TripDataBase tripDataBase = TripDataBase.getInstance(context);

    public HistoryAdapter(List<Trip> items,Context context) {
        tripList = items;
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card,parent,false);
        Log.e(TAG,"====onCreateViewHolder====");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip item=tripList.get(position);
      //  holder.tripImage.setImageResource(item.getImgId());
        holder.name.setText(item.getName());
        holder.status.setText(item.getState());
        holder.tripImage.setImageResource(R.drawable.download);
        holder.showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Showdata", Toast.LENGTH_SHORT).show();
            }
        });
        holder.showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.dataTextV.setText(item.getTripdate());
                holder.destinationTextV.setText(item.getEndpoint());
                holder.timeTextV.setText(item.getTriptime());
                holder.start.setText(item.getStartpoint());
                holder.constraintLayout.setVisibility(View.VISIBLE);
            }
        });
        holder.deleteBtnV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripDataBase.tripDAO()
                        .deleteTrip(tripList.get(position).getId())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                               /* Intent intent=new Intent(context,MainActivity.class);
                                context.startActivity(intent);*/
                                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                                HistoryFragment editFragment = new HistoryFragment();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_history,editFragment).addToBackStack(null).commit();
                            }

                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            }
                        });
            }
        });
     /*   if (onItemClickListener!=null){
            holder.showData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(position,item);
                    //show data
                    holder.dataTextV.setText(item.getTripdate());
                    holder.destinationTextV.setText(item.getEndpoint());
                    holder.timeTextV.setText(item.getTriptime());
                    holder.pickupTextV.setText(item.getEndpoint());
                    holder.deleteBtnV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tripList.remove(item);                        }
                    });

                }
            });*/


    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
   /* public OnItemClickListener onItemClickListener=null;
    public interface OnItemClickListener{
        public void onItemClick(int pos, Trip item);
    }
    public void changeData(ArrayList<Trip> newList){
        this.tripList=newList;
        notifyDataSetChanged();
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tripImage;
        TextView name;
        TextView status;
        Button showData;
        public View layout;
        ConstraintLayout constraintLayout;
     //-----------------------
        TextView dataTextV;
        TextView timeTextV;
        TextView destinationTextV;
        Button deleteBtnV;
        TextView start;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            //------card data-------
            tripImage=itemView.findViewById(R.id.imageView3);
            name=itemView.findViewById(R.id.Name);
            status=itemView.findViewById(R.id.Statustext);
            showData=itemView.findViewById(R.id.ShowDatabtn);
            constraintLayout=itemView.findViewById(R.id.constraintlayout);
            //--------data button shown
            dataTextV=itemView.findViewById(R.id.datetext);
            start=itemView.findViewById(R.id.start);
            timeTextV=itemView.findViewById(R.id.timetext);
            destinationTextV=itemView.findViewById(R.id.destinationtext);
            deleteBtnV=itemView.findViewById(R.id.button4);

        }
    }
}
