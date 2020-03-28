package com.example.snapets.view.menu.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FeedViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}