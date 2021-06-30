package com.mnm.mashawery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mnm.mashawery.R;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyHolder> {

    private final Context context;
    private List<String> values;
    private static final String TAG = "NoteAdapter";

    public NoteAdapter(Context context, List<String> data) {
        Log.i(TAG, "NoteAdapter: ");
        this.context = context;
        values = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.notescard, parent, false);
        MyHolder viewHolder = new MyHolder(view);
        Log.i(TAG, "onCreateViewHolder: ");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.txt1.setText(values.get(position));
        Log.i(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return values.size();
    }


     class MyHolder extends RecyclerView.ViewHolder {
        public TextView txt1;
        public ConstraintLayout constraintLayout;
        public View layout;

        public MyHolder(View view) {
            super(view);
            layout = view;
            txt1 = view.findViewById(R.id.notestext);
            constraintLayout = view.findViewById(R.id.thenoteconstraintlayout);
        }
    }

}
