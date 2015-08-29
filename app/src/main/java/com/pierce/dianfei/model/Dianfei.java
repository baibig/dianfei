package com.pierce.dianfei.model;

import java.io.Serializable;

/**
 * Author: pierce
 * Date: 2015/8/15
 */
public class Dianfei implements Serializable{
    String power;
    String time;
    public String getPower(){
        return power;
    }
    public void setPower(String power){
        this.power=power;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time=time;
    }
}
