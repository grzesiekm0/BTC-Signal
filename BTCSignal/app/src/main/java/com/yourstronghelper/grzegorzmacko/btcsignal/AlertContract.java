package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.net.Uri;

public class AlertContract {
    /**
     * Define all local entities in this class.
     */
        // ContentProvider information
        public static final String CONTENT_AUTHORITY = "com.example.sync";
        static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        static final String PATH_ARTICLES = "alert";

        // Database information
        static final String DB_NAME = "database.db";
        static final int DB_VERSION = 1;


        /**
         * This represents our SQLite table for our articles.
         */
        public static abstract class Alert {
            public static final String NAME = "alert";
            public static final String COL_ID = "AlertId";
            public static final String COL_EXCHANGE = "Exchange";
            public static final String COL_CURRENCY = "Currency";
            public static final String COL_COURSE = "Course";
            public static final String COL_ENABLE_ALARM = "EnableAlarm";


            // ContentProvider information for articles
            public static final Uri CONTENT_URI =
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLES).build();
            public static final String CONTENT_TYPE =
                    "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_ARTICLES;
            public static final String CONTENT_ITEM_TYPE =
                    "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ARTICLES;
        }

}
