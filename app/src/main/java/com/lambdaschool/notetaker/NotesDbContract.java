package com.lambdaschool.notetaker;

import android.provider.BaseColumns;

public class NotesDbContract {
    public static class NotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";

        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FB_ID = "firebase_id";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                                                      " ( " +
                                                      _ID + " TEXT PRIMARY KEY, " +
                                                      COLUMN_NAME_FB_ID + " TEXT, " +
                                                      COLUMN_NAME_CONTENT + " TEXT, " +
                                                      COLUMN_NAME_TITLE + " TEXT, " +
                                                      COLUMN_NAME_TIMESTAMP + " INTEGER);";

        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}
