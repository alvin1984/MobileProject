package com.mobile.network.entity;

import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.internal.http2.Header;

/**
 * Created by pxy on 2017/10/20.
 */

public class NetRequest {
    private HashMap<String,Object> params;
    private Header header;
    private String url;
    private Callback callback;
    private boolean addDefaultNetworkHandler = true;
    private ExePriority priority;



}
