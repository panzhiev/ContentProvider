package com.tim.contentprovider;


import com.tim.contentprovider.db.PersonContract;

/**
 * Created by Tim on 25.01.2017.
 */

public class Config {

    //создание final полей (ключей) будущей БД
    public static final String DB_NAME = "Person Data Base"; // Ключ базы

    public static final String TABLE_PERSON= "Person";       // Ключ таблицы

    //запрос в БД на создание таблицы
    public static final String COMMAND_CREATE = "create table "
            + TABLE_PERSON + " ("
            + PersonContract.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //id с автоинкрементом
            + PersonContract.KEY_NAME + " TEXT, "
            + PersonContract.KEY_SURNAME + " TEXT, "
            + PersonContract.KEY_PHONE + " TEXT, "
            + PersonContract.KEY_MAIL + " TEXT, "
            + PersonContract.KEY_SKYPE + " TEXT, "
            + PersonContract.KEY_PROFILE + " TEXT "
            + ");";

    // запрос в БД на удаление таблицы
    public static final String COMMAND_DELETE = "DROP TABLE IF EXISTS " + TABLE_PERSON;
    //версия БД
    public static final int DB_VERSION = 26012017;
}
