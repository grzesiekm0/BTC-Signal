package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddAlertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    AlertDatabaseAdapter alertDatabaseAdapter;
    Button mAddButton;
    EditText mCourse;
    Intent myIntent;
    Spinner spinExchange;
    Spinner spinCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);
        //Toolbar was hanidng to view
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);

        mAddButton = (Button) findViewById(R.id.add);
        mCourse = (EditText) findViewById(R.id.course);
        alertDatabaseAdapter=new AlertDatabaseAdapter(getApplicationContext());

        spinExchange = (Spinner) findViewById(R.id.exchange);
        spinCurrency = (Spinner) findViewById(R.id.currency);

        spinExchange.setOnItemSelectedListener(this);
        spinCurrency.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categoriesExchange = new ArrayList<String>();
        categoriesExchange.add("Wybierz giełdę");
        categoriesExchange.add("Bitmex");
        categoriesExchange.add("Bitbay");
        categoriesExchange.add("Binance");

        // Spinner Drop down elements
        List<String> categoriesCurrency = new ArrayList<String>();
        categoriesCurrency.add("Wybierz walutę");
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

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (validate() == true) {
                        Alert al = new Alert(null, spinExchange.getSelectedItem().toString(), spinCurrency.getSelectedItem().toString(), mCourse.getText().toString(), 1);
                        alertDatabaseAdapter = alertDatabaseAdapter.open();
                        //adding data to database
                        alertDatabaseAdapter.insertEntry(al.getExchange(), al.getCurrency(), al.getCourse(), al.getEnableAlarm());
                        alertDatabaseAdapter.close();
                    }
                }
                catch (Exception e){

                }
            }
        });
    }

    public boolean validate(){
        if(mCourse.getText().toString().equals("")){
            //setEditTextError(mCourse, "Pole nie może być puste");
            mCourse.setError("Blad");
            mCourse.requestFocus();
            return false;
        }
         else if (spinCurrency.getSelectedItem().toString().equals("Wybierz walutę"))
        {
            setSpinnerError(spinCurrency,"field can't be empty");
            return false;
        }
        else if (spinExchange.getSelectedItem().toString().equals("Wybierz giełdę"))
        {
            setSpinnerError(spinExchange,"field can't be empty");
            return false;
        }
        return true;
    }

    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            //spinner.performClick(); // to open the spinner list if error is found.

        }
    }
    /*private void setEditTextError(EditText editText, String error){
        View selectedView = (View) editText.getText();
        if (selectedView == null && selectedView instanceof EditText) {
        editText.setError(error);
        }
    }*/

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
            resetFields();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(AddAlertActivity.this, MainActivity.class);
        AddAlertActivity.this.startActivity(myIntent);
        super.onBackPressed();
    }

    public void resetFields(){
        spinExchange.setSelection(0);
        spinCurrency.setSelection(0);
        mCourse.setText("");
    }
}