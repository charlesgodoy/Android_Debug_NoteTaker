package com.lambdaschool.notetaker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.ArrayList;

public class NoteViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Note>> noteList;
    private NoteRepository repo;

    public LiveData<ArrayList<Note>> getNotesList(Context context) {
        if(noteList == null) {
            loadList(context);
        }
        return noteList;
    }

    private void loadList(Context context) {
        repo = new NoteRepository();
        noteList = repo.getNotes(context);
    }

    public void addNote(Note note, Context context) {
        if(noteList != null) {
            repo.addNote(note);
//            noteList = repo.getNotes(context);
        }
    }
}
