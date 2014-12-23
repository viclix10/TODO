package com.amberved.todo.todoapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private final int REQUEST_CODE = 100;

    ArrayList<Todo> items;
    ArrayAdapter<Todo> itemsAdapter;
    ListView lvItems;

    // DAO
    private TodoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);

        // Create DAO object
        dao = new TodoDAO(this);
        //Read Previous Settings
        readItems();

        itemsAdapter = new ArrayAdapter<Todo>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        //Is this the correct way to make list scroll to bottom
        lvItems.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvItems.setStackFromBottom(true);

        //Setup Listeners for list view
        setupListViewLongClickListener();
        setupListViewClickListener();
    }

    //Long Click to clear item from the list
    private void setupListViewLongClickListener() {
        lvItems.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                Todo todo = items.get(pos);
                dao.deleteTodo(todo.getId());
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    //Click to allow editing of the string in the list, using a new Activity
    private void setupListViewClickListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Intent i = new Intent(MainActivity.this, AddEditItemActivity.class);

                Todo todo = new Todo(items.get(pos).getText(), items.get(pos).getDueDate());

                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();

                i.putExtra("todo", todo);

                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    // Get the edited todo and store it
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            Todo todo = (Todo) data.getSerializableExtra("todo");

            String todoText = todo.getText().trim();

            if (!todoText.isEmpty()) {
                items.add(todo);
                itemsAdapter.notifyDataSetChanged();
                dao.createTodo(todo);
                //DebugOnly
                Toast.makeText(this, "deb"+todoText, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_menu_settings) {
            return true;
        } else if (id == R.id.main_menu_add) {
            // Create an intent
            Todo todo = new Todo();

            Intent i = new Intent(this, AddEditItemActivity.class);
            i.putExtra("todo", todo);

            // Start activity
            startActivityForResult(i, REQUEST_CODE);

            return true;
        } else if (id == R.id.main_menu_exit) {

            //Close the database
            dao.close();

            // Finish this activity
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void readItems() {
        items = (ArrayList<Todo>) dao.getTodos();
    }

}
