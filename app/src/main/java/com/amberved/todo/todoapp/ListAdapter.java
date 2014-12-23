package com.amberved.todo.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * List adapter for storing TODOs data
 */
public class ListAdapter extends ArrayAdapter {

    // List context
    private final Context context;

    // List values
    private final List todoList;

    public ListAdapter(Context context, List todoList) {
        super(context, R.layout.activity_main, todoList);
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_main, parent, false);
        TextView todoText = (TextView) rowView.findViewById(R.id.lvItems);
        String todo_value = ((Todo)todoList.get(position)).getText();
        todoText.setText(todo_value);
        return rowView;
    }

}
