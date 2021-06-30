package com.mnm.mashawery.ui.upcomming;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpcommingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UpcommingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is upcomming fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}