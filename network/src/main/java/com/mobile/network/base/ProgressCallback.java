package com.mobile.network.base;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by alvinzhang on 2017/7/19.
 */

public interface ProgressCallback {
    void onSuccess(Call call, Response response);
    void onProgress(long byteReadOrWrite, long contentLength, boolean done);
    void onError(Call call, IOException e);

}
