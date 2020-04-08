package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import android.os.*;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    /**
     * This is our example content observer.
     */
    private AlertObserver alertObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create your sync account
        AccountGeneral.createSyncAccount(this);

        // Perform a manual sync by calling this:
        SyncAdapter.performSync();


        // Setup example content observer
        alertObserver = new AlertObserver();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Register the observer at the start of our activity
        getContentResolver().registerContentObserver(
                AlertContract.Alert.CONTENT_URI, // Uri to observe (our articles)
                true, // Observe its descendants
               alertObserver); // The observer
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (alertObserver != null) {
            // Unregister the observer at the stop of our activity
            getContentResolver().unregisterContentObserver(alertObserver);
        }
    }

    private void refreshArticles() {
        Log.i(getClass().getName(), "Articles data has changed!");
    }


    /**
     * Example content observer for observing article data changes.
     */
    private final class AlertObserver extends ContentObserver {
        private AlertObserver() {
            // Ensure callbacks happen on the UI thread
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            // Handle your data changes here!!!
            refreshArticles();
        }
    }
    static Context context;
    static AlertDatabaseAdapter alertDatabaseAdapter;
    //old version
 /*   ListView listView;
    static ArrayList<Alert> dataModels;
    AlertAdapter adapter;
   static AlertDatabaseAdapter alertDatabaseAdapter;
    Intent myIntent;
    Toolbar toolbar;
    TextView textView, mTextView;
    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;

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
       // textView = (TextView) findViewById(R.id.textView);
        try {
            apiRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTextView = (TextView) findViewById(R.id.textView);

        // Create the dummy account
        mAccount = CreateSyncAccount(this);
    }

    *//**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     *//*
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        *//*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         *//*
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            *//*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             *//*
        } else {
            *//*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             *//*
        }
    }

    //Sending request to the API server.
    private void apiRequest(){

       String url = "https://10.0.2.2:5001/api/user";
       HurlStack urlAfter = transUrl(url);

        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject users = response.getJSONObject(i);

                        //String id = ("id: "+users.getString("id"));
                        String numerPhone = ("Phone: "+users.getString("phoneNumber"));
                        System.out.println(numerPhone);

                        //UserData udata = new UserData(id, name, username, email, address, phone, website, company,eta);
                        // userData.add(udata);
                    }

                    mTextView.setText(response.toString(5));

                } catch (JSONException e) {
                    mTextView.setText(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText(error.toString());
            }
        });

        final RequestQueue requestQueue = Volley.newRequestQueue(this,  urlAfter);
        requestQueue.add(jsonObjectRequest);
    }

    protected HurlStack transUrl(String url){
        //HurlStack an interface for transforming URLs before use.
        //The certificate from the API is added and processed here.
        HurlStack hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };
        return hurlStack;
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

    // Let's assume your server app is hosting inside a server machine
    // which has a server certificate in which "Issued to" is "localhost",for example.
    // Then, inside verify method you can verify "localhost".
    // If not, you can temporarily return true
    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("localhost", session);
            }
        };
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = new BufferedInputStream(getAssets().open("Certyfikaty.cer"));


        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
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
*/
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
