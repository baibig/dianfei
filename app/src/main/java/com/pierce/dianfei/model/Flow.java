package com.pierce.dianfei.model;

import java.io.Serializable;

/**
 * Created by HJ on 2015/4/27.
 */
public class Flow implements Serializable {
    private int id;
    private String flow;
    private int buildingId;
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setFlow(String flow){
        this.flow=flow;
    }
    public String getFlow(){
        return flow;
    }
    public void setBuildingId(int buildingId){
        this.buildingId=buildingId;
    }
    public int getBuildingId(){return buildingId;}

    @Override
    public String toString() {
        return flow;
    }
}
