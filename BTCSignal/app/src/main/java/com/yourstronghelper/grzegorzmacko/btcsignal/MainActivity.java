package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    static ArrayList<Alert> dataModels;
    AlertAdapter adapter;
   static AlertDatabaseAdapter alertDatabaseAdapter;
    Intent myIntent;
    Toolbar toolbar;
    TextView textView;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        //The list is attached to the view
        listView=(ListView)findViewById(R.id.listView);
        adapter= new AlertAdapter(getApplicationContext(),downloadRowsDb());
        listView.setAdapter(adapter);
        //Text view for api response
        textView = (TextView) findViewById(R.id.textView);
        apiRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            myIntent = new Intent(MainActivity.this, AddAlertActivity.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void apiRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://my-json-feed";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        textView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        textView.setText("Błąd: Chuj, dupa i kamieni kupa. ");
                    }
                });

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public ArrayList downloadRowsDb(){
        //An instance database was initialized and a connection was established
        alertDatabaseAdapter=new AlertDatabaseAdapter(getApplicationContext());
        alertDatabaseAdapter.open();
        dataModels= new ArrayList<>();
        //Alerts are retrieved from the database
        //Connection with datebase is closed.
        dataModels = alertDatabaseAdapter.getSinlgeEntry();
        alertDatabaseAdapter.close();
        return dataModels;
    }

    public static void updateRowsDb(String alertId, int enableAlarm){

        //An instance database was initialized and a connection was established
        alertDatabaseAdapter =new AlertDatabaseAdapter(getAppContext());
        alertDatabaseAdapter.open();
        //Alerts are retrieved from the database
        //Connection with datebase is closed.
        alertDatabaseAdapter.updateEntrySwitch(alertId, enableAlarm);
        alertDatabaseAdapter.close();
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }



}
