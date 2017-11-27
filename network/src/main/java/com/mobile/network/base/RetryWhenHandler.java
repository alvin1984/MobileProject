package com.mobile.network.base;


import com.mobile.network.NetworkManager;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public class RetryWhenHandler implements Function<Observable<? extends Throwable>, Observable<?>> {

    private int mCount = NetworkManager.RETRY_COUNT;
    private long mDelay = NetworkManager.RETRY_DELAY_TIME; //s

    private int counter = 1;

    public RetryWhenHandler() {
    }

    @Override
    public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
        return observable
                .flatMap(new Function<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> apply(Throwable throwable) {
                        if (counter < mCount && (throwable instanceof UnknownHostException
                                || throwable instanceof SocketException
                                || throwable instanceof SocketTimeoutException)) {
                            counter++;
                            return Observable.timer(mDelay, TimeUnit.MILLISECONDS);
                        }
                        return Observable.error(throwable);
                    }
                });
    }
}
