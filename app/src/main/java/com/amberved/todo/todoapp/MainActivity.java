package com.amberved.todo.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private final int REQUEST_CODE = 100;

    ArrayList<Todo> mTodosArrayList;
    TodoAdapter mAdapter;
    ListView mListViewTodos;
    TodoSQLiteHelper mTodoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTodoDatabase = new TodoSQLiteHelper(this);
        mTodosArrayList = (ArrayList) mTodoDatabase.getAll();
        mAdapter = new TodoAdapter(this, mTodosArrayList);

        mListViewTodos = (ListView) findViewById(R.id.lvItems);
        mListViewTodos.setAdapter(mAdapter);

        setupListViewLongClickListener();
        setupListViewClickListener();

    }

    private void setupListViewLongClickListener() {
        mListViewTodos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                Toast.makeText(MainActivity.this, "onLongClick", Toast.LENGTH_SHORT).show();

                Todo todo = mTodosArrayList.get(pos);
                mTodoDatabase.delete(todo);

                mTodosArrayList.remove(pos);
                mAdapter.notifyDataSetChanged();
                return true;

            }
        });
    }

    private void setupListViewClickListener() {
        mListViewTodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {

                Intent i = new Intent(MainActivity.this, AddEditItemActivity.class);

                Todo todo = mTodosArrayList.get(pos);
                mTodoDatabase.delete(todo);
                mTodosArrayList.remove(pos);
                mAdapter.notifyDataSetChanged();

                i.putExtra("todo", todo);
                startActivityForResult(i, REQUEST_CODE);

                if (BuildConfig.DEBUG)
                    Toast.makeText(MainActivity.this, "OnClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_menu_settings) {
            return true;
        } else if (id == R.id.main_menu_add) {
            Todo todo = new Todo("", 0, "");

            Intent i = new Intent(MainActivity.this, AddEditItemActivity.class);
            i.putExtra("todo", todo);
            startActivityForResult(i, REQUEST_CODE);

            return true;
        } else if (id == R.id.main_menu_exit) {

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            Todo todo = (Todo) data.getSerializableExtra("todo");

            String todoText = todo.getText().trim();

            if (!todoText.isEmpty()) {
                mAdapter.add(todo);
                mAdapter.notifyDataSetChanged();
                mTodoDatabase.add(todo);
                if (BuildConfig.DEBUG)
                    Toast.makeText(this, "New Todo" + todoText, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
