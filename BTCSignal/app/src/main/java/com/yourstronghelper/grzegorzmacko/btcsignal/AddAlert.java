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

        Spinner spin = (Spinner) findViewById(R.id.exchange);
        Spinner spin2 = (Spinner) findViewById(R.id.currency);

        spin.setOnItemSelectedListener(this);
        spin2.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Spinner Drop down elements
        List<String> categories2 = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spin.setAdapter(dataAdapter);
        spin.setAdapter(dataAdapter2);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
