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

/**
 * This is the ContentProvider that will be used by our SyncAdapter to sync local data.
 */
public class AlertProvider extends ContentProvider {
    // Use ints to represent different queries
    private static final int ARTICLE = 1;
    private static final int ARTICLE_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        // Add all our query types to our UriMatcher
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AlertContract.CONTENT_AUTHORITY, AlertContract.PATH_ARTICLES, ARTICLE);
        uriMatcher.addURI(AlertContract.CONTENT_AUTHORITY, AlertContract.PATH_ARTICLES + "/#", ARTICLE_ID);
    }

    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        this.db = DataBaseHelper.getInstance(getContext()).getDb();
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Find the MIME type of the results... multiple results or a single result
        switch (uriMatcher.match(uri)) {
            case ARTICLE:
                return AlertContract.Alert.CONTENT_TYPE;
            case ARTICLE_ID:
                return AlertContract.Alert.CONTENT_ITEM_TYPE;
            default: throw new IllegalArgumentException("Invalid URI!");
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor c;
        switch (uriMatcher.match(uri)) {
            // Query for multiple article results
            case ARTICLE:
                c = db.query(AlertContract.Alert.NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            // Query for single article result
            case ARTICLE_ID:
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

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri;
        long _id;

        switch (uriMatcher.match(uri)) {
            case ARTICLE:
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

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        switch (uriMatcher.match(uri)) {
            case ARTICLE:
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

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        switch (uriMatcher.match(uri)) {
            case ARTICLE:
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
}
