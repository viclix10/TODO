package com.amberved.todo.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private final int REQUEST_CODE = 100;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);

        //Read Previous Setting from file
        readItems();

        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
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
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    //Click to allow editing of the string in the list, using a new Activity
    private void setupListViewClickListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                i.putExtra("todoText", items.get(pos).toString());
                i.putExtra("pos", pos);

                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    // Get the edited todo and store it
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String todoText = data.getExtras().getString("todoText").trim();
            int pos = data.getIntExtra("pos", 0);
            if (!todoText.isEmpty()) {
                items.set(pos, todoText);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
            //DebugOnly
            //Toast.makeText(this, todoText, Toast.LENGTH_SHORT).show();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Add a new Item to the list on button pressed
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString().trim();
        if (!itemText.isEmpty()) {
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File (filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e)
        {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File (filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
