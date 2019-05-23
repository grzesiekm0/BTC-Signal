package com.yourstronghelper.grzegorzmacko.btcsignal;


import java.io.Serializable;

public class Alert implements Serializable {
    private String exchange;
    private String course;
    private String currency;
    private int enableAlarm;

    public Alert(String exchange, String currency, String course,  int enableAlarm) {
        this.exchange = exchange;
        this.course = course;
        this.currency = currency;
        this.enableAlarm = enableAlarm;
    }

    public Alert(){}

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getEnableAlarm() {
        return enableAlarm;
    }

    public void setEnableAlarm(int enableAlarm) {
        this.enableAlarm = enableAlarm;
    }
}
