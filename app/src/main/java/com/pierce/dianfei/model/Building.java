package com.pierce.dianfei.model;

import java.io.Serializable;

/**
 * Created by HJ on 2015/4/27.
 */
public class Building implements Serializable{
    private int id;
    private String building;
    private int sectionId;
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setBuilding(String building){
        this.building=building;
    }
    public String getBuilding(){
        return building;
    }
    public void setSectionId(int id){
        this.sectionId=id;
    }
    public int getSectionId(){
        return sectionId;
    }

    @Override
    public String toString() {
        return building;
    }
}
