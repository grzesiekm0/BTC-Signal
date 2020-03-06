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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */

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
            } catch (RemoteException | OperationApplicationException ex) {
                Log.e(TAG, "Error synchronizing!", ex);
                syncResult.stats.numAuthExceptions++;
            }

            Log.w(TAG, "Finished synchronization!");
        }

        /**
         * Performs synchronization of our pretend news feed source.
         * @param syncResult Write our stats to this
         */
        private void syncNewsFeed(SyncResult syncResult) throws IOException, JSONException, RemoteException, OperationApplicationException {
            final String rssFeedEndpoint = "http://www.examplejsonnews.com";

            // We need to collect all the network items in a hash table
            Log.i(TAG, "Fetching server entries...");
            Map<String, Alert> networkEntries = new HashMap<>();

            // Parse the pretend json news feed
            String jsonFeed = download(rssFeedEndpoint);
            JSONArray jsonArticles = new JSONArray(jsonFeed);
            for (int i = 0; i < jsonArticles.length(); i++) {
                Alert article = AlertParser.parse(jsonArticles.optJSONObject(i));
                networkEntries.put(article.getAlertId(), article);
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
            String dupa;
            Alert found;
            for (int i = 0; i < c.getCount(); i++) {
                syncResult.stats.numEntries++;

                // Create local article entry
                id = c.getString(c.getColumnIndex(AlertContract.Alert.COL_ID));
                title = c.getString(c.getColumnIndex(AlertContract.Alert.COL_EXCHANGE));
                content = c.getString(c.getColumnIndex(AlertContract.Alert.COL_COURSE));
                link = c.getString(c.getColumnIndex(AlertContract.Alert.COL_CURRENCY));
                dupa = c.getString(c.getColumnIndex(AlertContract.Alert.COL_ENABLE_ALARM));


                // Try to retrieve the local entry from network entries
                found = networkEntries.get(id);
                if (found != null) {
                    // The entry exists, remove from hash table to prevent re-inserting it
                    networkEntries.remove(id);

                    // Check to see if it needs to be updated
                    if (!title.equals(found.getExchange())
                            || !content.equals(found.getCourse())
                            || !link.equals(found.getCurrency())) {
                        // Batch an update for the existing record
                        Log.i(TAG, "Scheduling update: " + title);
                        batch.add(ContentProviderOperation.newUpdate(AlertContract.Alert.CONTENT_URI)
                                .withSelection(AlertContract.Alert.COL_ID + "='" + id + "'", null)
                                .withValue(AlertContract.Alert.COL_EXCHANGE, found.getExchange())
                                .withValue(AlertContract.Alert.COL_COURSE, found.getCourse())
                                .withValue(AlertContract.Alert.COL_CURRENCY, found.getCurrency())
                                .withValue(AlertContract.Alert.COL_ENABLE_ALARM, found.getEnableAlarm())
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
            for (Alert article : networkEntries.values()) {
                Log.i(TAG, "Scheduling insert: " + article.getExchange());
                batch.add(ContentProviderOperation.newInsert(AlertContract.Alert.CONTENT_URI)
                        .withValue(AlertContract.Alert.COL_ID, article.getAlertId())
                        .withValue(AlertContract.Alert.COL_EXCHANGE, article.getExchange())
                        .withValue(AlertContract.Alert.COL_COURSE, article.getCourse())
                        .withValue(AlertContract.Alert.COL_CURRENCY, article.getCurrency())
                        .withValue(AlertContract.Alert.COL_ENABLE_ALARM, article.getEnableAlarm())
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
        private String download(String url) throws IOException {
            // Ensure we ALWAYS close these!
            HttpURLConnection client = null;
            InputStream is = null;

            try {
                // Connect to the server using GET protocol
                URL server = new URL(url);
                client = (HttpURLConnection)server.openConnection();
                client.connect();

                // Check for valid response code from the server
                int status = client.getResponseCode();
                is = (status == HttpURLConnection.HTTP_OK)
                        ? client.getInputStream() : client.getErrorStream();

                // Build the response or error as a string
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                for (String temp; ((temp = br.readLine()) != null);) {
                    sb.append(temp);
                }

                return sb.toString();
            } finally {
                if (is != null) { is.close(); }
                if (client != null) { client.disconnect(); }
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
                    AlertContract.CONTENT_AUTHORITY, b);
        }
    }