package com.mobile.network;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.mobile.network.base.CallExecuteOnSubscribe;
import com.mobile.network.base.CookieManager;
import com.mobile.network.base.CustomerInterceptor;
import com.mobile.network.base.DefaultTransformer;
import com.mobile.network.base.DyFormBody;
import com.mobile.network.base.JsonBody;
import com.mobile.network.base.ProgressCallback;
import com.mobile.network.base.ProgressRequestBody;
import com.mobile.network.base.StaticHeadersInterceptor;
import com.mobile.network.base.StaticQueryParamsInterceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 网格管理器
 * Created by alvinzhang on 2017/7/17.
 */

public class NetworkManager {
//    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static StaticHeadersInterceptor staticHeadersInterceptor = new StaticHeadersInterceptor();
    private static StaticQueryParamsInterceptor staticQueryParamsInterceptor = new StaticQueryParamsInterceptor();
    private static CustomerInterceptor customerInterceptor = new CustomerInterceptor();
    private static final int DEFAULT_TIMEOUT = 10;
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static final int RETRY_COUNT = 2;
    public static final long RETRY_DELAY_TIME = 2000;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(staticHeadersInterceptor)
            .addInterceptor(staticQueryParamsInterceptor)
            .addInterceptor(customerInterceptor)
            .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
            .cookieJar(new CookieManager())
            .retryOnConnectionFailure(true)
            .build();

    public static void setStaticHeaders(String host,Headers headers) {
        staticHeadersInterceptor.setHeadersForEach(host,headers);
    }

    public static void setLogLevel(HttpLoggingInterceptor.Level level) {
        logging.setLevel(level);
    }

    public static void setStaticQueryParams(String host,HashMap<String, Object> params) {
        staticQueryParamsInterceptor.setStaticQueryParams(host,params);
    }

    public static Observable<Response> doGet(String url,boolean addDefaultNetworkHandler) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Call rawCall = okHttpClient.newCall(builder.build());
        return addDefaultNetworkHandler(rawCall,addDefaultNetworkHandler);
    }

    private static Observable<Response> addDefaultNetworkHandler(Call rawCall, boolean add){
        CallExecuteOnSubscribe callEnqueueOnSubscribe = new CallExecuteOnSubscribe(rawCall);
        if (add){
            return Observable.create(callEnqueueOnSubscribe).compose(DefaultTransformer.<Response>defaultHandler());
        }else{
            return Observable.create(callEnqueueOnSubscribe);
        }
    }


    public static Observable<Response> doGet(String url, HashMap<String, Object> params,boolean addDefaultNetworkHandler) {
        HttpUrl httpUrl = initQueryParams(url, params);
        if (httpUrl != null) {
            Request.Builder builder = new Request.Builder();
            builder.url(httpUrl);
            Call rawCall = okHttpClient.newCall(builder.build());
//            CallExecuteOnSubscribe callExecuteOnSubscribe = new CallExecuteOnSubscribe(rawCall);
//            return Observable.create(callExecuteOnSubscribe).compose(DefaultTransformer.<Response>defaultHandler());
            return addDefaultNetworkHandler(rawCall,addDefaultNetworkHandler);
        } else {
            return Observable.error(new Exception("httpUrl is null"));
        }

    }

    public static Observable<Response> doGet(String url, HashMap<String, Object> params, Headers headers,boolean addDefaultNetworkHandler) {
        HttpUrl httpUrl = initQueryParams(url, params);
        if (httpUrl != null) {
            Request.Builder builder = new Request.Builder();
            builder.url(httpUrl)
                    .headers(headers);
            Call rawCall = okHttpClient.newCall(builder.build());
//            CallExecuteOnSubscribe callExecuteOnSubscribe = new CallExecuteOnSubscribe(rawCall);
//            return Observable.create(callExecuteOnSubscribe).compose(DefaultTransformer.<Response>defaultHandler());
            return addDefaultNetworkHandler(rawCall,addDefaultNetworkHandler);
        } else {
            return Observable.error(new Exception("httpUrl is null"));
        }
    }

    public static void doGet(String url, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        request(builder.build(), callback,true);
    }

    public static void doGet(String url, HashMap<String, Object> params, Callback callback) {
        HttpUrl httpUrl = initQueryParams(url, params);
        if (httpUrl != null) {
            Request.Builder requestBuild = new Request.Builder();
            requestBuild.url(httpUrl);
            request(requestBuild.build(), callback,true);
        }

    }

    public static void doGet(String url, HashMap<String, Object> params, Headers headers, Callback callback) {
        HttpUrl httpUrl = initQueryParams(url, params);
        if (httpUrl != null) {
            Request.Builder requestBuild = new Request.Builder();
            requestBuild.url(httpUrl);
            requestBuild.headers(headers);
            request(requestBuild.build(), callback,true);
        }

    }

    public static void doPost(String url, HashMap<String, Object> params, Callback callback) {
        RequestBody formBody = generalPostBody(params);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).post(formBody);
        request(requestBuilder.build(), callback,true);
    }

    public static Observable<Response> doPost(String url, HashMap<String, Object> params,boolean addDefaultNetworkHandler) {
        RequestBody formBody = generalPostBody(params);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).post(formBody);
        Call rawCall = okHttpClient.newCall(requestBuilder.build());
//        CallExecuteOnSubscribe callExecuteOnSubscribe = new CallExecuteOnSubscribe(rawCall);
//        return Observable.create(callExecuteOnSubscribe).compose(DefaultTransformer.<Response>defaultHandler());
        return addDefaultNetworkHandler(rawCall,addDefaultNetworkHandler);
    }

    public static void doPost(String url, String json, Callback callback) {
//        RequestBody body = RequestBody.create(JSON, json);
        JsonBody body = new JsonBody.Builder()
                .addJson(json).build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).post(body);
        request(requestBuilder.build(), callback,true);
    }

    public static Observable<Response> doPost(String url, String json,boolean addDefaultNetworkHandler) {
//        RequestBody body = RequestBody.create(JSON, json);
        JsonBody body = new JsonBody.Builder()
                .addJson(json).build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).post(body);
        Call rawCall = okHttpClient.newCall(requestBuilder.build());
//        CallExecuteOnSubscribe callExecuteOnSubscribe = new CallExecuteOnSubscribe(rawCall);
//        return Observable.create(callExecuteOnSubscribe).compose(DefaultTransformer.<Response>defaultHandler());
        return addDefaultNetworkHandler(rawCall,addDefaultNetworkHandler);
    }

    public static void doPost(String url, Headers headers, HashMap<String, Object> params, Callback callback) {
        RequestBody formBody = generalPostBody(params);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).headers(headers).post(formBody);
        request(requestBuilder.build(), callback,true);
    }

    public static Observable<Response> doPost(String url, Headers headers, HashMap<String, Object> params,boolean addDefaultNetworkHandler) {
        RequestBody formBody = generalPostBody(params);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).headers(headers).post(formBody);
        Call rawCall = okHttpClient.newCall(requestBuilder.build());
//        CallExecuteOnSubscribe callExecuteOnSubscribe = new CallExecuteOnSubscribe(rawCall);
//        return Observable.create(callExecuteOnSubscribe).compose(DefaultTransformer.<Response>defaultHandler());
        return addDefaultNetworkHandler(rawCall,addDefaultNetworkHandler);
    }

    public static void doPost(String url, Headers headers, String json, Callback callback) {
//        RequestBody body = RequestBody.create(JSON, json);
        JsonBody body = new JsonBody.Builder()
                .addJson(json).build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).headers(headers).post(body);
        request(requestBuilder.build(), callback,true);
    }

    public static Observable<Response> doPost(String url, Headers headers, String json,boolean addDefaultNetworkHandler) {
//        RequestBody body = RequestBody.create(JSON, json);
        JsonBody body = new JsonBody.Builder()
                .addJson(json).build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).headers(headers).post(body);
        Call rawCall = okHttpClient.newCall(requestBuilder.build());
//        CallExecuteOnSubscribe callExecuteOnSubscribe = new CallExecuteOnSubscribe(rawCall);
//        return Observable.create(callExecuteOnSubscribe).compose(DefaultTransformer.<Response>defaultHandler());
        return addDefaultNetworkHandler(rawCall,addDefaultNetworkHandler);
    }

    public static void downloadFile(String url, final String savePath, final DownloadListener downloadListener) {

        if (!isSDCardEnable()) {
            if (downloadListener != null) {
                downloadListener.downloadFail("SDCard is disable");
            }
            return;
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        request(builder.build(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (downloadListener != null) {
                    downloadListener.downloadFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    byte[] buffer = new byte[1024];
                    File file = new File(savePath);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    FileOutputStream outputStream = new FileOutputStream(file);
                    int downloadSize = 0;
                    long totalSize = response.body().contentLength();
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                        downloadSize += len;
                        if (downloadListener != null) {
                            downloadListener.downloadProgress(downloadSize, totalSize);
                        }
                    }
                    outputStream.close();
                    if (downloadListener != null) {
                        downloadListener.downloadFinish();
                    }

                }
            }
        },false);

    }

    public static Observable<Response> downloadFile(String url) {
        if (!isSDCardEnable()) {
            return Observable.error(new Exception("SDCard is disable"));
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Call rawCall = okHttpClient.newCall(builder.build());
        CallExecuteOnSubscribe callExecuteOnSubscribe = new CallExecuteOnSubscribe(rawCall);
        return Observable.create(callExecuteOnSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static void uploadFile(String url, final File file, final ProgressCallback uploadListener) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);
        Request request = new Request.Builder()
                .url(url)
                .post(new ProgressRequestBody(requestBody, uploadListener, handler))
                .build();
        request(request, new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uploadListener.onError(call, e);
                    }
                });

            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uploadListener.onSuccess(call, response);
                    }
                });

            }
        },false);
    }

    public static void addNetworkHandle(CustomerInterceptor.NetWorkHandle handle){
        customerInterceptor.addNetWorkHandle(handle);
    }

    interface DownloadListener {
        void downloadFail(String error);

        void downloadFinish();

        void downloadProgress(long downloadSize, long totalSize);
    }

    private static boolean isSDCardEnable() {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
    }


    private static HttpUrl initQueryParams(String url, HashMap<String, Object> params) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl != null) {
            HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    urlBuilder.addQueryParameter(key, String.valueOf(params.get(key)));
                }
            }
            return urlBuilder.build();
        }
        return null;

    }

    private static void request(Request request, final Callback callback,final boolean retry) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            int retryCount = RETRY_COUNT;
            int counter;

            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException throwable) {
                if (retry && counter < retryCount && (throwable instanceof UnknownHostException
                        || throwable instanceof SocketException
                        || throwable instanceof SocketTimeoutException)) {
                    counter++;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Request newRequest = call.request();
                            request(newRequest,callback,false);
                        }
                    },RETRY_DELAY_TIME);
                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(call, throwable);
                        }
                    });

                }
            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callback.onResponse(call, response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private static DyFormBody generalPostBody(HashMap<String, Object> params) {
        DyFormBody.Builder formBody = new DyFormBody.Builder();
        if (params == null || params.isEmpty()) {
            return formBody.build();
        }

        for (String key : params.keySet()) {
            formBody.add(key, String.valueOf(params.get(key)));
        }

        return formBody.build();

    }


}
