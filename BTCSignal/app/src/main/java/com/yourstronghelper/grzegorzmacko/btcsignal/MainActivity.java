package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Alert> dataModels;
    AlertAdapter adapter;
    AlertDatabaseAdapter alertDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar was hanidng to view
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //An instance database was initialized and a connection was established
        alertDatabaseAdapter=new AlertDatabaseAdapter(getApplicationContext());
        alertDatabaseAdapter=alertDatabaseAdapter.open();
        //The list is attached to the view
        listView=(ListView)findViewById(R.id.listView);
        //Data to the models are initialized
        dataModels= new ArrayList<>();
        //Alerts are retrieved from the database
        //Connection with datebase is closed.
        dataModels = alertDatabaseAdapter.getSinlgeEntry();
        alertDatabaseAdapter.close();

        adapter= new AlertAdapter(getApplicationContext(),dataModels);
        listView.setAdapter(adapter);
    }
}
