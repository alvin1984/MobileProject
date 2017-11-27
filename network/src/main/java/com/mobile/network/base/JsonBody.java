package com.mobile.network.base;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by alvinzhang on 2017/9/1.
 */

public class JsonBody extends RequestBody {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private JSONObject params = new JSONObject();

    public JsonBody(JSONObject params) {
        this.params = params;
    }

    public void addParams(String key, Object value) {
        try {
            params.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return JSON;
    }

    @Override
    public long contentLength() throws IOException {
        return params.toString().getBytes("UTF-8").length;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.writeUtf8(params.toString());
    }

    public static final class Builder {

        private JSONObject jsonObject = new JSONObject();

        public JsonBody.Builder add(String name, String value) {
            try {
                jsonObject.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public JsonBody.Builder addJson(String json) {
            if (json.isEmpty()) {
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject temp = new JSONObject(json);
                    Iterator<String> iterator = temp.keys();
                    while (iterator.hasNext()) {
                        String name = iterator.next();
                        jsonObject.put(name, temp.get(name));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        public JsonBody build() {
            return new JsonBody(jsonObject);
        }
    }
}
