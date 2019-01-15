package com.lambdaschool.notetaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class NotesDbDao {
    private static SQLiteDatabase db;

    public static void initializeInstance(Context context) {
        if (db == null) {
            NotesDbHelper helper = new NotesDbHelper(context);
            db = helper.getWritableDatabase();
        }
    }

    public static Note readNote(String id) {
        if (db != null) {
            Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s'",
                                                      NotesDbContract.NotesEntry.COLUMN_NAME_FB_ID,
                                                      id),
                                        null);
            Note note;
            if (cursor.moveToNext()) {
                note = getNoteFromCursor(cursor);
            } else {
                note = null;
            }
            cursor.close();
            return note;

        } else {
            return null;
        }
    }

    @NonNull
    private static Note getNoteFromCursor(Cursor cursor) {
        int  index;
        Note note;
        index = cursor.getColumnIndexOrThrow(NotesDbContract.NotesEntry.COLUMN_NAME_TITLE);
        String title = cursor.getString(index);

        index = cursor.getColumnIndexOrThrow(NotesDbContract.NotesEntry.COLUMN_NAME_CONTENT);
        String content = cursor.getString(index);

        index = cursor.getColumnIndexOrThrow(NotesDbContract.NotesEntry.COLUMN_NAME_FB_ID);
        String retrievedId = cursor.getString(index);

        index = cursor.getColumnIndexOrThrow(NotesDbContract.NotesEntry.COLUMN_NAME_TIMESTAMP);
        long timestamp = cursor.getLong(index);

        note = new Note(title, content, retrievedId, timestamp);
        return note;
    }

    public static ArrayList<Note> readAllNotes() {
        if (db != null) {
            Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s;",
                                                      NotesDbContract.NotesEntry.TABLE_NAME),
                                        null);

            ArrayList<Note> notesList = new ArrayList<>();
            while (cursor.moveToNext()) {
                notesList.add(getNoteFromCursor(cursor));
            }
            cursor.close();
            return notesList;

        } else {
            return new ArrayList<>();
        }
    }

    public static void createNote(Note note) {
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(NotesDbContract.NotesEntry.COLUMN_NAME_CONTENT, note.getContent());
            values.put(NotesDbContract.NotesEntry.COLUMN_NAME_TIMESTAMP, note.getTimestamp());
            values.put(NotesDbContract.NotesEntry.COLUMN_NAME_TITLE, note.getTitle());
            values.put(NotesDbContract.NotesEntry.COLUMN_NAME_FB_ID, note.getId());

            long resultId = db.insert(NotesDbContract.NotesEntry.TABLE_NAME, null, values);

            /*db.execSQL(String.format("INSERT INTO %s " +
                                      "(%s, %s, %s, %s) " +
                                      "VALUES ('%s', '%s', '%s', %s);",

                                      NotesDbContract.NotesEntry.TABLE_NAME,

                                      NotesDbContract.NotesEntry.COLUMN_NAME_FB_ID,
                                      NotesDbContract.NotesEntry.COLUMN_NAME_TITLE,
                                      NotesDbContract.NotesEntry.COLUMN_NAME_CONTENT,
                                      NotesDbContract.NotesEntry.COLUMN_NAME_TIMESTAMP,

                                      note.getId(),
                                      note.getTitle(),
                                      note.getContent(),
                                      note.getTimestamp()));*/
        }
    }

    public static void updateNote(Note note) {
        if (db != null) {
            String whereClause = String.format("%s = '%s'",
                                               NotesDbContract.NotesEntry.COLUMN_NAME_FB_ID,
                                               note.getId());

            final Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s",
                                                            NotesDbContract.NotesEntry.TABLE_NAME,
                                                            whereClause),
                                              null);

            if(cursor.getCount() == 1) {
                ContentValues values = new ContentValues();
                values.put(NotesDbContract.NotesEntry.COLUMN_NAME_CONTENT, note.getContent());
                values.put(NotesDbContract.NotesEntry.COLUMN_NAME_TIMESTAMP, note.getTimestamp());
                values.put(NotesDbContract.NotesEntry.COLUMN_NAME_TITLE, note.getTitle());

                final int affectedRows = db.update(NotesDbContract.NotesEntry.TABLE_NAME, values, whereClause, null);
            }
        }
    }

    public static void deleteNote(Note note) {
        if (db != null) {
            String whereClause = String.format("%s = '%s'",
                                               NotesDbContract.NotesEntry.COLUMN_NAME_FB_ID,
                                               note.getId());

            int affectedRows = db .delete(NotesDbContract.NotesEntry.TABLE_NAME, whereClause, null);
        }
    }


    public static ArrayList<Note> updateCache(ArrayList<Note> fbNotes) {
        // read all notes
        final ArrayList<Note> cacheNotes = readAllNotes();

        // check each note
        for(Note fbNote: fbNotes) {
            boolean noteFound = false;
            for(Note cacheNote: cacheNotes) {
                if(fbNote.getId().equals(cacheNote.getId())) {
                    // if note does exist, check for timestamp
                    if(fbNote.getTimestamp() > cacheNote.getTimestamp()) {
                        // if fb is newer update cache
                        updateNote(fbNote);
                    } else {
                        // else keep cache
                    }
                    noteFound = true;
                }
            }
            if(!noteFound) {
                // if note doesn't exist in cache, add it
                createNote(fbNote);
            }
        }

        return readAllNotes();
    }
}
