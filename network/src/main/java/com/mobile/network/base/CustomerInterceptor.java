package com.mobile.network.base;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pxy on 2017/10/15.
 */

public class CustomerInterceptor implements Interceptor {

    private ArrayList<NetWorkHandle> mNetWorkHandles = new ArrayList<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        for (NetWorkHandle handle : mNetWorkHandles) {
            handle.beforeSendToServer(original);
        }
        return chain.proceed(original);
    }

    public void addNetWorkHandle(NetWorkHandle handle) {
        mNetWorkHandles.add(handle);
    }

    public interface NetWorkHandle {
        void beforeSendToServer(Request request);
    }

}
