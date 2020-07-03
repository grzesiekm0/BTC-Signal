package com.yourstronghelper.grzegorzmacko.btcsignal.model;

import java.io.Serializable;

public class SMSAlert implements Serializable {
    private String phoneNumber;
    private String course;
    private int enableAlarm;

    public SMSAlert(String phoneNumber, String course, int enableAlarm) {
        this.phoneNumber = phoneNumber;
        this.course = course;
        this.enableAlarm = enableAlarm;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getEnableAlarm() {
        return enableAlarm;
    }

    public void setEnableAlarm(int enableAlarm) {
        this.enableAlarm = enableAlarm;
    }
}
