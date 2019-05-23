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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        alertDatabaseAdapter=new AlertDatabaseAdapter(getApplicationContext());
        alertDatabaseAdapter=alertDatabaseAdapter.open();

        listView=(ListView)findViewById(R.id.listView);
        dataModels= new ArrayList<>();

        dataModels = alertDatabaseAdapter.getSinlgeEntry();
        alertDatabaseAdapter.close();

        /*Alert a1 = new Alert("BITMEX","USD", "1234", 0 );
        Alert a2 = new Alert("BINANCE", "USD","2134", 1 );
        Alert a3 = new Alert("BITMEX", "BTC", "5432", 1 );
        dataModels.add(a1);
        dataModels.add(a2);
        dataModels.add(a3);*/

        adapter= new AlertAdapter(getApplicationContext(),dataModels);
        listView.setAdapter(adapter);
    }
}
