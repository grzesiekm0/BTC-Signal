package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import android.content.res.AssetManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/*import javax.net.ssl.TrustManagerFactory;*/
import org.apache.http.conn.ssl.SSLSocketFactory;
/**
 * This is used by the Android framework to perform synchronization. IMPORTANT: do NOT create
 * new Threads to perform logic, Android will do this for you; hence, the name.
 *
 * The goal here to perform synchronization, is to do it efficiently as possible. We use some
 * ContentProvider features to batch our writes to the local data source. Be sure to handle all
 * possible exceptions accordingly; random crashes is not a good user-experience.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SYNC_ADAPTER";

    /**
     * This gives us access to our local data source.
     */
    private final ContentResolver resolver;


    public SyncAdapter(Context c, boolean autoInit) {
        this(c, autoInit, false);
    }

    public SyncAdapter(Context c, boolean autoInit, boolean parallelSync) {
        super(c, autoInit, parallelSync);
        this.resolver = c.getContentResolver();
    }

    /**
     * This method is run by the Android framework, on a new Thread, to perform a sync.
     * @param account Current account
     * @param extras Bundle extras
     * @param authority Content authority
     * @param provider {@link ContentProviderClient}
     * @param syncResult Object to write stats to
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.w(TAG, "Starting synchronization...");

        try {
            // Synchronize our news feed
            syncNewsFeed(syncResult);

            // Add any other things you may want to sync

        } catch (IOException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numIoExceptions++;
        } catch (JSONException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numParseExceptions++;
        } catch (RemoteException|OperationApplicationException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numAuthExceptions++;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        Log.w(TAG, "Finished synchronization!");
    }

    /**
     * Performs synchronization of our pretend news feed source.
     * @param syncResult Write our stats to this
     */
    private void syncNewsFeed(SyncResult syncResult) throws IOException, JSONException, RemoteException, OperationApplicationException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final String rssFeedEndpoint = "https://10.0.2.2:5001/api/alert";

        // We need to collect all the network items in a hash table
        Log.i(TAG, "Fetching server entries...");
        Map<String, Alert> networkEntries = new HashMap<>();

        // Parse the pretend json news feed
        String jsonFeed = download(rssFeedEndpoint);
        JSONArray jsonArticles = new JSONArray(jsonFeed);
        for (int i = 0; i < jsonArticles.length(); i++) {
            Alert alert = AlertParser.parse(jsonArticles.optJSONObject(i));
            networkEntries.put(alert.getAlertId(), alert);
            System.out.println("Dane "+alert.getAlertId()
            );
        }

        // Create list for batching ContentProvider transactions
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();

        // Compare the hash table of network entries to all the local entries
        Log.i(TAG, "Fetching local entries...");
        Cursor c = resolver.query(AlertContract.Alert.CONTENT_URI, null, null, null, null, null);
        assert c != null;
        c.moveToFirst();

        String id;
        String title;
        String content;
        String link;
        Alert found;
        for (int i = 0; i < c.getCount(); i++) {
            syncResult.stats.numEntries++;

            // Create local article entry
            id = c.getString(c.getColumnIndex(AlertContract.Alert.COL_ID));
            title = c.getString(c.getColumnIndex(AlertContract.Alert.COL_EXCHANGE));
            content = c.getString(c.getColumnIndex(AlertContract.Alert.COL_CURRENCY));
            link = c.getString(c.getColumnIndex(AlertContract.Alert.COL_COURSE));

            // Try to retrieve the local entry from network entries
            found = networkEntries.get(id);
            if (found != null) {
                // The entry exists, remove from hash table to prevent re-inserting it
                networkEntries.remove(id);

                // Check to see if it needs to be updated
                if (!title.equals(found.getExchange())
                        || !content.equals(found.getCurrency())
                        || !link.equals(found.getCourse())) {
                    // Batch an update for the existing record
                    Log.i(TAG, "Scheduling update: " + title);
                    batch.add(ContentProviderOperation.newUpdate(AlertContract.Alert.CONTENT_URI)
                            .withSelection(AlertContract.Alert.COL_ID + "='" + id + "'", null)
                            .withValue(AlertContract.Alert.COL_EXCHANGE, found.getExchange())
                            .withValue(AlertContract.Alert.COL_CURRENCY, found.getCurrency())
                            .withValue(AlertContract.Alert.COL_COURSE, found.getCourse())
                            .build());
                    syncResult.stats.numUpdates++;
                }
            } else {
                // Entry doesn't exist, remove it from the local database
                Log.i(TAG, "Scheduling delete: " + title);
                batch.add(ContentProviderOperation.newDelete(AlertContract.Alert.CONTENT_URI)
                        .withSelection(AlertContract.Alert.COL_ID + "='" + id + "'", null)
                        .build());
                syncResult.stats.numDeletes++;
            }
            c.moveToNext();
        }
        c.close();

        // Add all the new entries
        for (Alert alert : networkEntries.values()) {
            Log.i(TAG, "Scheduling insert: " + alert.getExchange());
            batch.add(ContentProviderOperation.newInsert(AlertContract.Alert.CONTENT_URI)
                    .withValue(AlertContract.Alert.COL_ID, alert.getAlertId())
                    .withValue(AlertContract.Alert.COL_EXCHANGE, alert.getExchange())
                    .withValue(AlertContract.Alert.COL_CURRENCY, alert.getCurrency())
                    .withValue(AlertContract.Alert.COL_COURSE, alert.getCourse())
                    .build());
            syncResult.stats.numInserts++;
        }

        // Synchronize by performing batch update
        Log.i(TAG, "Merge solution ready, applying batch update...");
        resolver.applyBatch(AlertContract.CONTENT_AUTHORITY, batch);
        resolver.notifyChange(AlertContract.Alert.CONTENT_URI, // URI where data was modified
                null, // No local observer
                false); // IMPORTANT: Do not sync to network
    }

    /**
     * A blocking method to stream the server's content and build it into a string.
     * @param url API call
     * @return String response
     */


    private String download(String url) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
     /*   // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
        InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL("https://certs.cac.washington.edu/CAtest/");
        HttpsURLConnection urlConnection =
                (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        InputStream in = urlConnection.getInputStream();
        copyInputStreamToOutputStream(in, System.out);
        InputStream in;
        OutputStream out;
        IOUtils.copy(in,out);
        in.close();
        out.close();
*/
           // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
        InputStream caInput = new BufferedInputStream(getContext().getAssets().open("cert.cer"));
        //InputStream caInput = new BufferedInputStream(getContext().getAssets().open("cert.cer"));

        //InputStream caInput = new BufferedInputStream(new FileInputStream("Certyfikaty.cer"));
        //System.out.println(new File(".").getAbsolutePath());

        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);



        //test
        // Ensure we ALWAYS close these!
        HttpsURLConnection urlConnection = null;
        InputStream is = null;
        try {

// Create an HostnameVerifier that hardwires the expected hostname.
// Note that is different than the URL's hostname:
// example.com versus example.org
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("localhost", session);
            }
        };

        URL url1 = new URL(url);
         urlConnection =
                (HttpsURLConnection)url1.openConnection();
        urlConnection.setHostnameVerifier(hostnameVerifier);
        urlConnection.setSSLSocketFactory(context.getSocketFactory());

        //int status = urlConnection.getResponseCode();
         is = urlConnection.getInputStream();
        //copyInputStreamToOutputStream(in, System.out);
        //InputStream in;
      //  OutputStream out;
        //IOUtils.copy(is,System.out);
        //in.close();
       // System.out.close();
        ///test


           /* // Connect to the server using GET protocol
            URL server = new URL(url);


            client =
                    (HttpsURLConnection)server.openConnection();
            client.setSSLSocketFactory(context.getSocketFactory());
            InputStream in = client.getInputStream();*/



            /*client = (HttpsURLConnection)server.openConnection();
            //((HttpsURLConnection) client).setSSLSocketFactory(context.getSocketFactory());
            client.setSSLSocketFactory(context.getSocketFactory());
            ((HttpsURLConnection) client).setSSLSocketFactory();*/
            /*HttpsURLConnection urlConnection = (HttpsURLConnection)server.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            InputStream in = urlConnection.getInputStream();*/

            //client.connect();

            // Check for valid response code from the server
            int status = urlConnection.getResponseCode();
            /*is = (status == HttpsURLConnection.HTTP_OK)
                    ? urlConnection.getInputStream() : urlConnection.getErrorStream();*/

            // Build the response or error as a string
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
           // String temp1 = br.readLine();
            for (String temp; ((temp = br.readLine()) != null);) {
                sb.append(temp);
            }

            return sb.toString();
        } finally {
            if (is != null) { is.close(); }
            if (urlConnection != null) { urlConnection.disconnect(); }
        }
    }

    /**
     * Manual force Android to perform a sync with our SyncAdapter.
     */
    public static void performSync() {
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(AccountGeneral.getAccount(),
                ArticleContract.CONTENT_AUTHORITY, b);
    }
}