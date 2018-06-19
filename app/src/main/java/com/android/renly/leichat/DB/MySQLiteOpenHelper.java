package com.android.renly.leichat.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    //单例模式
    private static final String DB_NAME = "leichat.db";
    public static final String TABLE_NAME_User = "User";
    public static final String TABLE_NAME_Message = "Message";
    private static final int VERSION = 1;
    private static MySQLiteOpenHelper instance;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteOpenHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    public synchronized static MySQLiteOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new MySQLiteOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //数据库创建
        db.execSQL("create table if not exists " + TABLE_NAME_User + " (_id integer primary key autoincrement, " +
                "name char(20), password char(20))");

        db.execSQL("create table if not exists " + TABLE_NAME_Message + " (M_ID integer primary key autoincrement, " +
        "M_content Text,M_Time Date,M_Status bit,M_messageType integer,M_fromUserID integer,M_toUserID integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //数据库升级
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
