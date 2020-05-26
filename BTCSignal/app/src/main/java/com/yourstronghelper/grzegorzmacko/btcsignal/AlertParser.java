package com.yourstronghelper.grzegorzmacko.btcsignal;

import org.json.JSONObject;

/**
 * This is an example parser to 'parse' pretend json news feed.
 */
public class AlertParser {
    public static Alert parse(JSONObject jsonArticle) {
        Alert alert = new Alert();
        alert.setAlertId(jsonArticle.optString("alertId"));
        alert.setExchange(jsonArticle.optString("exchange"));
        alert.setCurrency(jsonArticle.optString("currency"));
        alert.setCourse(jsonArticle.optString("course"));
        alert.setEnableAlarm(jsonArticle.optInt("enableAlarm"));
        return alert;
    }
}