package com.mobile.network.base;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by alvinzhang on 2017/7/17.
 */

public class CallExecuteOnSubscribe implements ObservableOnSubscribe<Response> {

    private Call mCall;

    public CallExecuteOnSubscribe(Call call) {
        mCall = call;
    }

    @Override
    public void subscribe(final @NonNull ObservableEmitter<Response> emitter) throws Exception {
        if (!emitter.isDisposed()) {
            try {
                //此处是由于重连时，OkHttp需要重新生成call对象，否则会报错
                if (mCall.isExecuted()) {
                    mCall = mCall.clone();
                }
                Response response = mCall.execute();
                emitter.onNext(response);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }
    }
}
