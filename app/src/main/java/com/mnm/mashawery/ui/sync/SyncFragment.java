package com.mnm.mashawery.ui.sync;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mnm.mashawery.R;

public class
SyncFragment extends Fragment {

    private SyncViewModel syncViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        syncViewModel =
                new ViewModelProvider(this).get(SyncViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sync, container, false);
        final TextView textView = root.findViewById(R.id.text_sync);
        syncViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}