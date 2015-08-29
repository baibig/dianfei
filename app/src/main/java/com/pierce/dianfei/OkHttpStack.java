package com.pierce.dianfei;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: pierce
 * Date: 2015/8/18
 */
public class OkHttpStack extends HurlStack {
    private final OkHttpClient client;
    public OkHttpStack(){
        this(new OkHttpClient());
    }
    public OkHttpStack(OkHttpClient client){
        if (client==null){
            throw new NullPointerException("client must not be null");
        }
        this.client=client;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        OkUrlFactory okUrlFactory = new OkUrlFactory(client);
        return okUrlFactory.open(url);
    }
}
