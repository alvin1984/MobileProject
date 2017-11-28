package com.mobile.network.base;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.mobile.network.entity.Result;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by alvinzhang on 2017/9/3.
 */

public class BaseConsumer<T> implements Consumer<Response> {

    private Consumer<Result<T>> consumer;
    private Type mType;

    public BaseConsumer(Consumer<Result<T>> consumer){
        this.consumer = consumer;
        mType = getTypeArguments(consumer.getClass());
    }

    public static Type getTypeArguments(Class<?> subclass) {
        //得到带有泛型的类
        Type generic = subclass.getGenericInterfaces()[0];
        if (generic instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        //取出当前类的泛型
        ParameterizedType parameter = (ParameterizedType) generic;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }


    @Override
    public void accept(@NonNull Response response) throws Exception {
        if (response.isSuccessful()){
            ResponseBody responseBody = response.body();
            if (responseBody != null){
                Result<T> result = new Gson().fromJson(responseBody.string(),mType);
                consumer.accept(result);
            }
        }

    }
}
