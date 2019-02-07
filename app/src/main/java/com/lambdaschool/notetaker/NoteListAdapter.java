package com.lambdaschool.notetaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        ViewGroup parentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.showTitle);
            noteContent = itemView.findViewById(R.id.note_element_content);
            parentView = itemView.findViewById(R.id.note_element_parent_layout);
        }
    }

    private ArrayList<Note> dataList;
    private Context         context;
    private Activity        activity;

    NoteListAdapter(ArrayList<Note> dataList, Activity activity) {
        this.dataList = dataList;
        this.activity = activity;
    }

    public void replaceList(ArrayList<Note> newData) {
        this.dataList.clear();
        this.dataList.addAll(newData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(
                viewGroup.getContext())
                                  .inflate(
                                          R.layout.note_element_layout,
                                          viewGroup,
                                          false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Note data = dataList.get(i);

        viewHolder.noteTitle.setText(data.getTitle());
        // I removed the content length limit to better take advantage of the staggered grid
        /*String content;
        if (data.getContent().length() > 30) {
            content = data.getContent().substring(0, 30) + "...";
        } else {
            content = data.getContent();
        }*/
        /*if(i == 1) {
            viewHolder.parentView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.parentView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }*/

        viewHolder.noteContent.setText(data.getContent());
        viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra(EditActivity.EDIT_NOTE_KEY, data);
                activity.startActivityForResult(intent, MainActivity.EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
