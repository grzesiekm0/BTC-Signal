package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddAlert extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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

        Spinner spinExchange = (Spinner) findViewById(R.id.exchange);
        Spinner spinCurrency = (Spinner) findViewById(R.id.currency);

        spinExchange.setOnItemSelectedListener(this);
        spinCurrency.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categoriesExchange = new ArrayList<String>();
        categoriesExchange.add("Bitmex");
        categoriesExchange.add("Bitbay");
        categoriesExchange.add("Binance");

        // Spinner Drop down elements
        List<String> categoriesCurrency = new ArrayList<String>();
        categoriesCurrency.add("PLN");
        categoriesCurrency.add("USD");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterExchange = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesExchange);
        ArrayAdapter<String> dataAdapterCurrency = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesCurrency);


        // Drop down layout style - list view with radio button
        dataAdapterExchange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinExchange.setAdapter(dataAdapterExchange);
        spinCurrency.setAdapter(dataAdapterCurrency);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
