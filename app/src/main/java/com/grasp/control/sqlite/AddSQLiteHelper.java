package com.grasp.control.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zhujingju on 2017/8/28.
 */

public class AddSQLiteHelper extends SQLiteOpenHelper {
   //uid 设备 id    sid  摄像头id   mindex 位置
    public   String CREATE_NEW = "create table AddEquipment ("
            + "id integer primary key autoincrement, "
            + "uid text, "
            + "sid text, "
            + "mindex text)"
            ;

    public AddSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEW);
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
