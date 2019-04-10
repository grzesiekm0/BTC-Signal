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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        listView=(ListView)findViewById(R.id.listView);
        dataModels= new ArrayList<>();
        Alert a1 = new Alert("bitmex", "USD", 6, 0 );
        Alert a2 = new Alert("binance", "USD", 3, 1 );

        adapter= new AlertAdapter(getApplicationContext(),dataModels);
        listView.setAdapter(adapter);
    }
}
