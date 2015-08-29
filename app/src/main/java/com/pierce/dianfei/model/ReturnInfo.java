package com.pierce.dianfei.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: pierce
 * Date: 2015/8/15
 */
public class ReturnInfo implements Serializable{
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * state状态解释：
     * 0:初始状态
     * 1:已获取区域信息
     * 2:以获取楼栋信息
     * 3:已获取楼层信息
     */
    private int state;

    private String viewstate;
    private String eventvalidation;
    private String html;

    private String section;
    private String building;
    private String flow;
    private String room;

    public ReturnInfo(){
        state=0;
    }
    public ReturnInfo(ReturnInfo r){
        this.state=r.getState();
        this.viewstate=r.getViewstate();
        this.eventvalidation=r.getEventvalidation();
        this.html=r.getHtml();
        this.section=r.getSection();
        this.building=r.getBuilding();
        this.flow=r.getFlow();
        this.room=r.getRoom();
    }

    @Override
    public String toString() {
        return "state:"+state+"||section:"+section+"||building:"+
                building+"||flow:"+flow+"||room:"+room+
                "||viewstate:"+viewstate+"||eventvalidation:"+eventvalidation;
    }

    public void setInfo(ReturnInfo r){
        this.state=r.getState();
        this.viewstate=r.getViewstate();
        this.eventvalidation=r.getEventvalidation();
        this.html=r.getHtml();
        this.section=r.getSection();
        this.building=r.getBuilding();
        this.flow=r.getFlow();
        this.room=r.getRoom();
    }



    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getViewstate() {
        return viewstate;
    }

    public void setViewstate(String viewstate) {
        this.viewstate = viewstate;
    }

    public String getEventvalidation() {
        return eventvalidation;
    }

    public void setEventvalidation(String eventvalidation) {
        this.eventvalidation = eventvalidation;
    }




}
