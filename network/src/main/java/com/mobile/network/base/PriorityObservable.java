package com.mobile.network.base;


import com.mobile.network.entity.ExePriority;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by pxy on 2017/11/27.
 */

public class PriorityObservable<T> {

    private ExePriority mPriority = ExePriority.Normal;
    private static boolean isSuperRunning = false;
    public static ArrayList<PriorityObservable> requests = new ArrayList<>();

    private Observable mObservable;
    private Consumer<T> mConsumer;
    private Consumer<Throwable> mErrorConsumer;

    public PriorityObservable(Observable observable){
        mObservable = observable;
    }

    public PriorityObservable<T> setPriority(ExePriority priority){
        mPriority = priority;
        return this;
    }

    public Observable getObservable(){
        return mObservable;
    }

    public Consumer<T> getConsumer(){
        return mConsumer;
    }

    public Consumer<Throwable> getErrorConsumer(){
        return mErrorConsumer;
    }

    public ExePriority getPriority(){
        return mPriority;
    }

    public PriorityObservable<T> doOnSubscribe(Consumer<Disposable> consumer){
        mObservable.doOnSubscribe(consumer);
        return this;
    }

    public PriorityObservable<T> delay(long delay,TimeUnit timeUnit){
        mObservable.delaySubscription(delay, timeUnit);
        return this;
    }

    public void subscribe(final Consumer<T> consumer,final Consumer<Throwable> errorConsumer){
        mConsumer = consumer;
        mErrorConsumer = errorConsumer;
        if (mPriority != ExePriority.Super){
            if (!isSuperRunning){
                if (mErrorConsumer == null){
                    mObservable.subscribe(consumer);
                }else{
                    mObservable.subscribe(consumer,errorConsumer);
                }

            }else{
                requests.add(this);
            }

        }else{
            isSuperRunning = true;
            mObservable.subscribe(new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {
                    isSuperRunning = false;
                    consumer.accept(t);
                    doOtherNetwork();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    isSuperRunning = false;
                    if (errorConsumer != null){
                        errorConsumer.accept(throwable);
                    }
                    doOtherNetwork();
                }
            });
        }

    }

    public void subscribe(Consumer<T> consumer){
        this.subscribe(consumer,null);
    }

    private void doOtherNetwork(){
        for (PriorityObservable observable:requests){
            if (observable.getErrorConsumer() != null){
                observable.getObservable().subscribe(observable.getConsumer(),observable.getErrorConsumer());
            }else{
                observable.getObservable().subscribe(observable.getConsumer());
            }

        }
    }


}
