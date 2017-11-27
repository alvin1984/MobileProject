package com.mobile.network.base;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 为所有请求添加默认的请求参数
 * Created by alvinzhang on 2017/6/18.
 */

public class StaticQueryParamsInterceptor implements Interceptor {

//    private HashMap<String,String> queryParams = new HashMap<>();
    private HashMap<String,HashMap<String,Object>> mParams = new HashMap<>();

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        HashMap<String,Object> queryParams = mParams.get(original.url().host());
        if (queryParams == null){
            return chain.proceed(original);
        }
        if (original.method().equals("GET")) {
            HttpUrl originalHttpUrl = original.url();
            HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();
            if (!queryParams.isEmpty()){
                for (String key: queryParams.keySet()){
                    urlBuilder.addQueryParameter(key,String.valueOf(queryParams.get(key)));
                }
            }
            HttpUrl url = urlBuilder.build();
            Request request = original.newBuilder().url(url).build();
            return chain.proceed(request);
        }else if (original.method().equals("POST")){
            RequestBody body = original.body();
            if (body instanceof DyFormBody){
//                FormBody formBody = (FormBody)body;
//                FormBody.Builder formBuilder = new FormBody.Builder();
//                for (int i = 0; i < formBody.size();i++){
//                    formBuilder.add(formBody.name(i),formBody.value(i));
//                }
//                if (!queryParams.isEmpty()){
//                    for (String key: queryParams.keySet()){
//                        formBuilder.add(key,queryParams.get(key));
//                    }
//                }
//                Request.Builder requestBuilder = original.newBuilder();
//                requestBuilder.post(formBuilder.build());
//                return chain.proceed(requestBuilder.build());
                DyFormBody formBody = (DyFormBody)body;
                if (!queryParams.isEmpty()){
                    for (String key: queryParams.keySet()){
                        formBody.addParam(key,String.valueOf(queryParams.get(key)));
                    }
                }
//                Request.Builder requestBuilder = original.newBuilder();
//                requestBuilder.post(formBody);
//                return chain.proceed(requestBuilder.build());

            }else if (body instanceof JsonBody){
                JsonBody jsonBody = (JsonBody)body;
                if (!queryParams.isEmpty()){
                    for (String key: queryParams.keySet()){
                        jsonBody.addParams(key,queryParams.get(key));
                    }
                }
//                Request.Builder requestBuilder = original.newBuilder();
//                requestBuilder.post(jsonBody);
//                return chain.proceed(requestBuilder.build());
            }
        }



        return chain.proceed(original);
    }

    public void setStaticQueryParams(String host,HashMap<String,Object> params){
        mParams.put(host,params);

    }
}
