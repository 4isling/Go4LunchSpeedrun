package com.Hayse.go4lunch.utils;

import androidx.lifecycle.LiveData;

public class LiveDataTestUtils {
    public static <T> void observeForTest(LiveData<T> liveData, OnObserveListener<T> block) {
        liveData.observeForever(o -> {
        });

        block.onObserved(liveData.getValue());
    }

    public interface OnObserveListener<T> {
        void onObserved(T livedata);
    }
}