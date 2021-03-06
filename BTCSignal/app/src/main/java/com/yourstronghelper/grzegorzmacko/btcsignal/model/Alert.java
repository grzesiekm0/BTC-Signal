package com.yourstronghelper.grzegorzmacko.btcsignal.model;

public class Alert {
    private String alertId;
    private String exchange;
    private String course;
    private String currency;
    private int enableAlarm;

    public Alert(String alertId, String exchange, String currency, String course, int enableAlarm) {
        this.alertId = alertId;
        this.exchange = exchange;
        this.course = course;
        this.currency = currency;
        this.enableAlarm = enableAlarm;
    }

    public Alert() {
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
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