package com.tim.contentprovider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tim.contentprovider.Config;

/**
 * Created by Tim on 25.01.2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context) {
        //вызов суперкласса SQLiteOpenHelper для обращения к базе данных
        super(context, Config.DB_NAME, null, Config.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Config.COMMAND_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(Config.COMMAND_DELETE);
        this.onCreate(sqLiteDatabase);
    }
}
