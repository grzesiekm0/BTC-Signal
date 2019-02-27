package com.yourstronghelper.grzegorzmacko.btcsignal;


import java.io.Serializable;

public class Alert implements Serializable {
    private String exchange;
    private String course;
    private int perPoints;
    private int enableAlarm;

    public Alert(String exchange, String course, int perPoints, int enableAlarm) {
        this.exchange = exchange;
        this.course = course;
        this.perPoints = perPoints;
        this.enableAlarm = enableAlarm;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getPerPoints() {
        return perPoints;
    }

    public void setPerPoints(int perPoints) {
        this.perPoints = perPoints;
    }

    public int getEnableAlarm() {
        return enableAlarm;
    }

    public void setEnableAlarm(int enableAlarm) {
        this.enableAlarm = enableAlarm;
    }
}
