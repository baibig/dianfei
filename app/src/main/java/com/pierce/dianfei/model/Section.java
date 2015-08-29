package com.pierce.dianfei.model;

import java.io.Serializable;

/**
 * Created by HJ on 2015/4/27.
 */
public class Section implements Serializable {
    private int id;
    private String section;
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setSection(String section){
        this.section=section;
    }
    public String getSection(){
        return section;
    }

    @Override
    public String toString() {
        return section;
    }
}
