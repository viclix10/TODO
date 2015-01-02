package com.amberved.todo.todoapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private static class ViewHolder {
        TextView name;
        TextView priority;
        TextView dueDate;
    }

    public TodoAdapter(Context context, ArrayList<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Todo todo = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.todo_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.priority = (TextView) convertView.findViewById(R.id.tvPriority);
            viewHolder.dueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(todo.getText());
        viewHolder.priority.setText(Integer.toString(todo.getPriority()));
        viewHolder.dueDate.setText(todo.getDueDate());

        return convertView;
    }
}

