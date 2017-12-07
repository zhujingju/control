package com.grasp.control.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhujingju on 2017/8/28.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String CREATE_NEWS = "create table TabPlay ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "mindex text, "
            + "uid text, "
            + "path text)"
            ;

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);
//        this.db=db;
    }
//    SQLiteDatabase db;
//    public void execSQL(String tabName){
//        String CREATE_NEWS = "create table La"+tabName+" ("
//                + "id integer primary key autoincrement, "
//                + "title text, "
//                + "index text, "
//                + "path text"
//                ;
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
