package com.mobile.network.base;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 为每个请求统一设置默认的消息头
 * Created by alvinzhang on 2017/6/18.
 */

public class StaticHeadersInterceptor implements Interceptor {

    private HashMap<String,Headers> mHeader = new HashMap<>();

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Headers headers = mHeader.get(original.url().host());
        Request.Builder requestBuilder = original.newBuilder();
        if (headers != null){
            for (String key:headers.names()){
                String value = headers.get(key);
                if (value != null){
                    requestBuilder.addHeader(key,value);
                }
            }
        }
        return chain.proceed(requestBuilder.build());
    }

    public void setHeadersForEach(String host,Headers headers){
        mHeader.put(host,headers);
//        this.headers = headers;
    }


}
