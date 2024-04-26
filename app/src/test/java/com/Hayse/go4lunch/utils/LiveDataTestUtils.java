package com.Hayse.go4lunch.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtils {

    /**
     * Gets the value from a LiveData object. Waits for LiveData to emit, up to 2 seconds.
     */
    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        if (liveData == null) {
            throw new IllegalArgumentException("LiveData must not be null");
        }

        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        if (!latch.await(2, TimeUnit.SECONDS)) {
            liveData.removeObserver(observer);
            throw new InterruptedException("LiveData value was never set.");
        }
        return (T) data[0];
    }
}