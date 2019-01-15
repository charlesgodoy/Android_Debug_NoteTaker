package com.lambdaschool.notetaker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotesFirebaseDao {
    private static final String BASE_URL = "https://notesdemoproject.firebaseio.com/";
    private static final String NOTES = "notes/";
    private static final String URL_ENDING = ".json";

    private static final String USER_INFO = BASE_URL + USER_ID + URL_ENDING;
    private static final String USER_NOTES = BASE_URL + USER_ID + NOTES + URL_ENDING;
    private static final String USER_SPECIFIC_NOTE = BASE_URL + USER_ID + NOTES + "%s/" + URL_ENDING; //use string.format to add id

    public static ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        final String result = NetworkAdapter.httpRequest(USER_NOTES, NetworkAdapter.GET);
        Log.i("Firebase DAO", result);
        try {
            JSONObject topLevel = new JSONObject(result);
            JSONArray noteIds = topLevel.names();
            for(int i = 0; i < noteIds.length(); ++i) {
                final String name = noteIds.getString(i);
                notes.add(new Note(
                        topLevel.getJSONObject(name),
                        name)
                         );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notes;
    }

    public static String createNote(Note note) {
        final String result = NetworkAdapter.httpRequest(USER_NOTES, NetworkAdapter.POST, note.toJsonString());

        try {
            return new JSONObject(result).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void deleteNote(String id) {
        NetworkAdapter.httpRequest(String.format(USER_SPECIFIC_NOTE, id), NetworkAdapter.DELETE);
    }

    public static void updateNote(String id, Note newNote) {
        NetworkAdapter.httpRequest(String.format(USER_SPECIFIC_NOTE, id), NetworkAdapter.PUT, newNote.toJsonString());
    }
}
