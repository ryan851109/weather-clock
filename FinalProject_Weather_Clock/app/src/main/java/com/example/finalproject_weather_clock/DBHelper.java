package com.example.finalproject_weather_clock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class DBHelper extends SQLiteOpenHelper {

    //TASK 1: DEFINE THE DATABASE AND TABLE
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PROGECT";
    private static final String DATABASE_TABLE = "WEATHER_CLOCK";


    //TASK 2: DEFINE THE COLUMN NAMES FOR THE TABLE
    private static final String KEY_NUMBER = "NUMBER";
    private static final String KEY_ON_OFF = "ON_OFF";
    private static final String KEY_TIME = "TIME";
    private static final String KEY_HOUR = "HOUR";
    private static final String KEY_MINUTE = "MINUTE";
    private static final String KEY_WEATHER = "WEATHER";

    private static int taskCount;

    public DBHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database){
        String table = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_NUMBER + " TEXT PRIMARY KEY, "
                + KEY_ON_OFF + " TEXT , "
                + KEY_TIME + " TEXT, "
                + KEY_HOUR + " TEXT, "
                + KEY_MINUTE + " TEXT, "
                + KEY_WEATHER + " TEXT " + ")";
        database.execSQL (table);
        taskCount = this.getTaskCount();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }

    //********** DATABASE OPERATIONS:  ADD, EDIT, DELETE


    public void addToDoItem(Info task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        taskCount++;
        //values.put(KEY_NUMBER, String.valueOf(taskCount));
        values.put(KEY_NUMBER, task.getnumber());
        taskCount = Integer.parseInt(task.getnumber());

        values.put(KEY_ON_OFF, task.geton_off());

        values.put(KEY_TIME, task.gettime());

        values.put(KEY_HOUR, task.gethour());

        values.put(KEY_MINUTE, task.getmin());

        values.put(KEY_WEATHER, task.getweather());

        // INSERT THE ROW IN THE TABLE
        db.insert(DATABASE_TABLE, null, values);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    public void editTaskItem(Info task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NUMBER, task.getnumber());
        values.put(KEY_ON_OFF, task.geton_off());
        values.put(KEY_TIME, task.gettime());
        values.put(KEY_HOUR, task.gethour());
        values.put(KEY_MINUTE, task.getmin());
        values.put(KEY_WEATHER, task.getweather());

        db.update(DATABASE_TABLE, values, KEY_NUMBER + " = ?",
                new String[]{
                        String.valueOf(task.getnumber())
                });
        db.close();
    }

    public void editTaskItem2(Info task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NUMBER, String.valueOf(Integer.parseInt(task.getnumber())-1) );
        values.put(KEY_ON_OFF, task.geton_off());
        values.put(KEY_TIME, task.gettime());
        values.put(KEY_HOUR, task.gethour());
        values.put(KEY_MINUTE, task.getmin());
        values.put(KEY_WEATHER, task.getweather());

        db.update(DATABASE_TABLE, values, KEY_NUMBER + " = ?",
                new String[]{
                        String.valueOf(task.getnumber())
                });
        db.close();
    }

    public Info getToDo_Task(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DATABASE_TABLE,
                new String[]{KEY_NUMBER,KEY_ON_OFF,KEY_TIME, KEY_HOUR, KEY_MINUTE, KEY_WEATHER},
                KEY_NUMBER + "=?",
                new String[]{String.valueOf(number)},
                null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        Info task = new Info(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));
        db.close();
        return task;
    }

    public void deleteTaskItem (Info task){
        SQLiteDatabase database = this.getReadableDatabase();

        taskCount--;
        // DELETE THE TABLE ROW
        database.delete(DATABASE_TABLE, KEY_NUMBER + " = ?",
                new String[]
                        {String.valueOf(task.getnumber())});
        database.close();
    }

    public int getTaskCount() {
        return taskCount;
    }

    public ArrayList<Info> getAllTaskItems() {
        ArrayList<Info> taskList = new ArrayList<Info>();
        String queryList = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(queryList, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()){
            do {
                Info task = new Info();
                task.setnumber(cursor.getString(0));
                task.seton_off(cursor.getString(1));
                task.settime(cursor.getString(2));
                task.sethour(cursor.getString(3));
                task.setmin(cursor.getString(4));
                task.setweather(cursor.getString(5));

                //ADD TO THE QUERY LIST
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }
}
