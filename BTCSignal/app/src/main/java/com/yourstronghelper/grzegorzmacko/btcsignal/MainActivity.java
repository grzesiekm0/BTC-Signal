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
/**
 * Your SyncAdapter is good to go!
 *
 * Your SyncAdapter will run all on its own by Android if you specified it to sync
 * automatically and periodically. If not, you can force a sync using our performSync()
 * method we made.
 *
 * Use {@link android.database.ContentObserver} to get callbacks for data changes when
 * Android runs your SyncAdapter or when you manually run it.
 */
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
                ArticleContract.Articles.CONTENT_URI, // Uri to observe (our articles)
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
}
