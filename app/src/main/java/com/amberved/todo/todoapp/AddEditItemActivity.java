package com.amberved.todo.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddEditItemActivity extends ActionBarActivity {

    private TextView mDueDateTextView;
    private ImageView mTodoLogoImageView;

    private Todo todo;

    private static int RESULT_LOAD_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_item);

        todo = (Todo) getIntent().getSerializableExtra("todo");

        EditText etNewItem = (EditText) findViewById(R.id.etTodoText);
        etNewItem.setText(todo.getText());
        EditText etNewPriority = (EditText) findViewById(R.id.etPriority);
        etNewPriority.setText(Integer.toString(todo.getPriority()));
        mDueDateTextView = (TextView) findViewById(R.id.etDueDate);
        mDueDateTextView.setText(todo.getDueDate());

        mTodoLogoImageView = (ImageView) findViewById(R.id.imTodoLogo);

        etNewItem.setSelection(etNewItem.getText().length());
        etNewPriority.setSelection(etNewPriority.getText().length());

        setupDueDateListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_edit_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickSaveButton(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etTodoText);
        EditText etNewPriority = (EditText) findViewById(R.id.etPriority);
        EditText etDueDate = (EditText) findViewById(R.id.etDueDate);

        String todoText = etNewItem.getText().toString();
        String todoPriority = etNewPriority.getText().toString();
        String todoDueDate = etDueDate.getText().toString();

        Intent newTodo = new Intent();
        if (todoPriority.trim().isEmpty())
        {
            todoPriority = "0";
        }
        Todo todo = new Todo(todoText, Integer.valueOf(todoPriority), todoDueDate);
        newTodo.putExtra("todo", todo);

        setResult(RESULT_OK, newTodo);
        finish();
    }

    public void onClickDueDateButton(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            AddEditItemActivity addItemActivity = (AddEditItemActivity) getActivity();
            TextView tvDueDate = (TextView) addItemActivity.findViewById(R.id.etDueDate);

            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date item_date = cal.getTime();

            addItemActivity.todo.setDueDate(item_date);

            tvDueDate.setText(DateFormat.getDateFormat(getActivity()).format(item_date));
        }
    }

    private void setupDueDateListener(){
        mDueDateTextView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                todo.removeDueDate();
                mDueDateTextView.setText("");
                return false;
            }
        });
    }

    public void onClickImageViewTodoLogo(View v)
    {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imTodoLogo);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            if (BuildConfig.DEBUG)
                Toast.makeText(this, "ImagePath#"+picturePath, Toast.LENGTH_SHORT).show();

        }
    }

}
