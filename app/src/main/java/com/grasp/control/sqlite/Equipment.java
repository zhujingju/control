package com.grasp.control.sqlite;

/**
 * Created by zhujingju on 2017/8/30.
 */

public class Equipment {

    private String uid="";  //设备编号
    private String sid="";  //摄像头编号
    private String index="";  //位置

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
