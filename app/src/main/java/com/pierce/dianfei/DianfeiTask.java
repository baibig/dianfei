package com.pierce.dianfei;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pierce.dianfei.AppControler;
import com.pierce.dianfei.InfoProcess;
import com.pierce.dianfei.MainActivity;
import com.pierce.dianfei.model.Building;
import com.pierce.dianfei.model.Dianfei;
import com.pierce.dianfei.model.Flow;
import com.pierce.dianfei.model.ReturnInfo;
import com.pierce.dianfei.model.Section;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DianfeiTask {
    private static final String TAg="DianfeiTask";

    private final static String ADDRESS="http://202.114.18.218/main.aspx";
    //private final static String ADDRESS="http://www.baidu.com";


    //ProgressDialog dialog;
    InfoProcess mCallback;


    public void setCallback(Activity activity){
        this.mCallback= (InfoProcess) activity;
    }


    public void query(final ReturnInfo info) {
        StringRequest request;
        if (info.getState()==0){
            request=new StringRequest(ADDRESS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(TAg,"返回数据成功");
                            mCallback.callback(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.i(TAg,"返回数据失败");
                            mCallback.error();
                        }
                    }
            );
        }else {
            request=new StringRequest(Request.Method.POST, ADDRESS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mCallback.callback(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    if (info.getState()==1){
                        params.put("programId", info.getSection());
                        params.put("__EVENTTARGET", "programId");
                    }else if (info.getState()==2){
                        params.put("txtyq",info.getBuilding());
                        params.put("programId",info.getSection());
                        params.put("__EVENTTARGET","txtyq");
                    }else if (info.getState()==3){
                        params.put("Txtroom",info.getRoom());
                        params.put("txtld",info.getFlow());
                        params.put("txtyq",info.getBuilding());
                        params.put("programId",info.getSection());
                        params.put("ImageButton1.x","0");
                        params.put("ImageButton1.y","0");

                    }
                    params.put("__VIEWSTATE",info.getViewstate());
                    params.put("__EVENTVALIDATION",info.getEventvalidation());
                    return params;
                }
            };
        }
        AppControler.getInstance().addToRequestQueue(request);
    }


}