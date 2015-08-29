package com.pierce.dianfei;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Author: pierce
 * Date: 2015/8/15
 */
public class AppControler extends Application {
    public static final String TAG = AppControler.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private static AppControler mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static synchronized AppControler getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue==null){
            mRequestQueue= Volley.newRequestQueue(getApplicationContext(),new OkHttpStack(new OkHttpClient()));
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request,String tag){
        request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(request);
    }

    public<T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
