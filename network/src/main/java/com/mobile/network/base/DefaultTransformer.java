package com.mobile.network.base;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class DefaultTransformer {

    public static <T> ObservableTransformer<T, T> defaultHandler() {
        return new ObservableTransformer<T,T>(){

            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWhenHandler())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
