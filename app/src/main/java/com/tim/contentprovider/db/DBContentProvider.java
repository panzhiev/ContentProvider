package com.tim.contentprovider.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.tim.contentprovider.Config;

import java.io.FileNotFoundException;

/**
 * Created by Tim on 25.01.2017.
 */

public class DBContentProvider extends ContentProvider{

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static final String AUTHORITY = "com.tim.contentprovider";
    static final String PERSON_PATH = "person";

    //Общий Uri
    public static final Uri PERSONS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PERSON_PATH);

    private static final int URI_PERSON = 1;
    private static final int URI_PERSON_ID = 2;

    //Типы данных (MIME типы)
    //набор строк
    static final String PERSON_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PERSON_PATH;

    //одна строка
    static final String PERSON_CONTENT_ITEM_TYPE = "vod.android.cursor.item/vod." + AUTHORITY + "." + PERSON_PATH;

    private DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        uriMatcher.addURI(AUTHORITY, PERSON_PATH, URI_PERSON);
        uriMatcher.addURI(AUTHORITY, PERSON_PATH + "/#", URI_PERSON_ID);

        dbHelper = new DBHelper(getContext());
        Toast.makeText(getContext(), "Call provider", Toast.LENGTH_LONG).show();
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case URI_PERSON: return PERSON_CONTENT_TYPE;
            case URI_PERSON_ID: return PERSON_CONTENT_ITEM_TYPE;
            default: break;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName;
        String id;

        switch (uriMatcher.match(uri))
        {
            case URI_PERSON: //общий URI
                //если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)){
                sortOrder = PersonContract.KEY_NAME + " ASC";
            }
            tableName = Config.TABLE_PERSON;
            break;
            case URI_PERSON_ID: // URI with Id
                id = uri.getLastPathSegment();
                //добавляем id к условию выборки
                if (TextUtils.isEmpty(selection)){
                    selection = PersonContract.KEY_ID + " = " + id;
                } else {
                selection = selection + " AND " + PersonContract.KEY_ID + " = " + id;
            }
            tableName = Config.TABLE_PERSON;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI" + uri);
        }

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        //просим ContentResolver уведомлять этот курсор
        //об изменениях данных в PERSONS_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(), PERSONS_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName;
        Uri resultUri;
        if (uriMatcher.match(uri) == URI_PERSON)
        {
            tableName = Config.TABLE_PERSON;
            resultUri = PERSONS_CONTENT_URI;
        }
        else
            throw new IllegalArgumentException("Wrong URI: " + uri);
        db = dbHelper.getWritableDatabase();
        String nullColumn = "";
        long rowID = db.insert(tableName, nullColumn, values);
        resultUri = ContentUris.withAppendedId(resultUri, rowID);
        //уведомлять ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id;
        String tableName;
        switch (uriMatcher.match(uri)){
            case URI_PERSON: tableName = Config.TABLE_PERSON;
                break;
            case URI_PERSON_ID:
                tableName = Config.TABLE_PERSON;
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    selection = PersonContract.KEY_ID + " = " + id;
                } else {
                    selection = selection + " AND " + PersonContract.KEY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id;
        String tableName;
        switch (uriMatcher.match(uri)){
            case URI_PERSON: tableName = Config.TABLE_PERSON;
                break;
            case URI_PERSON_ID: tableName = Config.TABLE_PERSON;
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    selection = PersonContract.KEY_ID + " = " + id;
                } else {
                    selection = selection + " AND " + PersonContract.KEY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return super.openFile(uri, mode);
    }
}
