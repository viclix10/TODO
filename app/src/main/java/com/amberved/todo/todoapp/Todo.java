package com.amberved.todo.todoapp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Todo implements Serializable {

    private String mText;
    private int mPriority;
    private long mId;
    private String mDueDate;

    private static final long serialVersionUID = 5177222050535318633L;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getDueDate()
    {
        return mDueDate;
    }

    public void setDueDate(Date dueDate){
        this.mDueDate = dateToString(dueDate);
    }

    public boolean isDueDateSet(){
        if(mDueDate != null)
            return true;
        return false;
    }

    public void removeDueDate(){
        mDueDate = null;
    }

    public Todo(){
        super();
        this.mPriority = 0;
    }

    public Todo(String text, int priority, String dueDate) {
        super();

        this.mText = text;
        this.mPriority = priority;
        this.mDueDate = dueDate;
    }

    public Todo(String text, int priority, Date dueDate) {
        super();

        this.mText = text;
        this.mPriority = priority;
        this.mDueDate = dateToString(dueDate);
    }

    private Date stringToDate(String dateStr)
    {
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(dateStr);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String dateToString (Date date)
    {
        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateStr = dateFormat.format(date);
        return dateStr;
    }
}
