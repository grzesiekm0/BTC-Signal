package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/*
 * Define an implementation of ContentProvider that stubs out
 * all methods
 */
public class StubProvider extends ContentProvider {
    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for table 3 are shown.
         */

        /*
         * Sets the integer value for multiple rows in table 3 to 1. Notice that no wildcard is used
         * in the path
         */
        uriMatcher.addURI("com.example.app.provider", "Alert", 1);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.example.app.provider/table3/3" matches, but
         * "content://com.example.app.provider/table3 doesn't.
         */
        uriMatcher.addURI("com.example.app.provider", "Alert/#", 2);
    }
    private SQLiteDatabase db;
    /*
     * Always return true, indicating that the
     * provider loaded correctly.
     */
    @Override
    public boolean onCreate() {

        this.db = DataBaseHelper.getInstance(getContext()).getDb();
        return true;
    }
    /*
     * Return no type for MIME type
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Find the MIME type of the results... multiple results or a single result
        switch (uriMatcher.match(uri)) {
            case 1:
                return AlertContract.Alert.CONTENT_TYPE;
            case 2:
                return AlertContract.Alert.CONTENT_ITEM_TYPE;
            default: throw new IllegalArgumentException("Invalid URI!");
        }
    }
    /*
     * query() always returns no results
     *
     */
    @Nullable
    @Override
    public Cursor query(@NonNull
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        Cursor c;
        switch (uriMatcher.match(uri)) {
            // Query for multiple article results
            case 1:
                c = db.query(AlertContract.Alert.NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            // Query for single article result
            case 2:
                long _id = ContentUris.parseId(uri);
                c = db.query(AlertContract.Alert.NAME,
                        projection,
                        AlertContract.Alert.COL_ID + "=?",
                        new String[] { String.valueOf(_id) },
                        null,
                        null,
                        sortOrder);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Tell the cursor to register a content observer to observe changes to the
        // URI or its descendants.
        assert getContext() != null;
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
    /*
     * insert() always returns null (no URI)
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri;
        long _id;

        switch (uriMatcher.match(uri)) {
            case 1:
                _id = db.insert(AlertContract.Alert.NAME, null, values);
                returnUri = ContentUris.withAppendedId(AlertContract.Alert.CONTENT_URI, _id);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        assert getContext() != null;
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }
    /*
     * delete() always returns "no rows affected" (0)
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rows;
        switch (uriMatcher.match(uri)) {
            case 1:
                rows = db.delete(AlertContract.Alert.NAME, selection, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        if (rows != 0) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
    /*
     * update() always returns "no rows affected" (0)
     */
    @Override
    public int update(
            Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs) {
        int rows;
        switch (uriMatcher.match(uri)) {
            case 1:
                rows = db.update(AlertContract.Alert.NAME, values, selection, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        if (rows != 0) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
}