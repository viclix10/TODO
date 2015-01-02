
package com.amberved.todo.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TodoSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "todoListDatabase";

    private static final String TABLE_TODO = "todo_items";

    private static final String KEY_ID = "id";
    private static final String KEY_BODY = "text";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_DATE = "dueDate";

    public TodoSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_BODY + " TEXT,"
                    + KEY_PRIORITY + " INTEGER,"
                    + KEY_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    public void add(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BODY, todo.getText());
        values.put(KEY_PRIORITY, todo.getPriority());
        values.put(KEY_DATE, todo.getDueDate());
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    public Todo get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO,
                new String[] { KEY_ID, KEY_BODY, KEY_PRIORITY, KEY_DATE },
                KEY_ID + "= ?", new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Todo todo = new Todo(cursor.getString(1), cursor.getInt(2), cursor.getString(3));
        todo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
        return todo;
    }

    public List<Todo> getAll() {
        List<Todo> Todos = new ArrayList<Todo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo(cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                todo.setId(cursor.getInt(0));
                Todos.add(todo);
            } while (cursor.moveToNext());
        }

        return Todos;
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public int update(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BODY, todo.getText());
        values.put(KEY_PRIORITY, todo.getPriority());
        values.put(KEY_DATE, todo.getDueDate()  );
        int result = db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
        db.close();
        return result;
    }

    private static final String TAG = "TodoSQLiteHelper";

    public void delete(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });

        Log.d(TAG, "Trying to remove from database #" + todo.getId());

        db.close();
    }

}
