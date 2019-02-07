package com.lambdaschool.notetaker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    public static final String EDIT_NOTE_KEY = "edit_note";



    EditText editTitle, editContent;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);

        note = (Note) getIntent().getSerializableExtra("editNote");
        if(note == null) {
            note = new Note(Note.NO_ID);
        }

        editTitle.setText(note.getTitle());
        editContent.setText(note.getContent());
    }

    @Override
    public void setTheme(int resid) {
        super.setTheme(resid);
    }

    @Override
    public void onBackPressed() {
        prepResult();
        super.onBackPressed();
    }

    private void prepResult() {
        note.setTitle(editTitle.getText().toString());
        note.setContent(editContent.getText().toString());
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EDIT_NOTE_KEY, note);
        setResult(Activity.RESULT_OK, resultIntent);
    }
}
