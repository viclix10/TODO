package com.amberved.todo.todoapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditItemActivity extends ActionBarActivity {

    //Stores the position of todoItem under edit.
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String value = getIntent().getStringExtra("todoText");
        pos = getIntent().getIntExtra("pos", 0);

        //DebugOnly
        //Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

        EditText etNewItem = (EditText) findViewById(R.id.etTodoText);
        etNewItem.setText(value);
        //Sets the cursor at the end of text
        etNewItem.setSelection(etNewItem.getText().length());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onClickSaveButton(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etTodoText);
        String itemText = etNewItem.getText().toString();
        Intent newTodo = new Intent();

        newTodo.putExtra("todoText", itemText);
        newTodo.putExtra("pos", pos);

        setResult(RESULT_OK, newTodo);
        finish();

    }
}
