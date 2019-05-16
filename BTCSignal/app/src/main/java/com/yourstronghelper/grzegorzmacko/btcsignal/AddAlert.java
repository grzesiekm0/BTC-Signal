package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddAlert extends AppCompatActivity {
    AlertDatabaseAdapter alertDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);
        alertDatabaseAdapter=new AlertDatabaseAdapter(getApplicationContext());
        alertDatabaseAdapter=alertDatabaseAdapter.open();
        //example data
        Alert al = new Alert("bitmex", "PLN", "2000", 1);
        System.out.println("Test"+al.getExchange());
        alertDatabaseAdapter.insertEntry(al.getExchange(), al.getCurrency(), al.getCourse(), al.getEnableAlarm());
        alertDatabaseAdapter.close();
    }
}
