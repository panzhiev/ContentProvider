package com.tim.contentprovider.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.tim.contentprovider.db.DBContentProvider;
import com.tim.contentprovider.db.PersonContract;

/**
 * Created by Tim on 08.02.2017.
 */

public class DatabaseTasks extends AsyncTask<Object, Void, Void> {

    private final Context mContext;
    public static final int INSERT = 0;
    public static final int DELETE_BY_ID = 1;
    public static final int UPDATE = 2;
    public static final int QUERY_BY_ID = 3;
    public static final int DELETE = 4;

    public DatabaseTasks(Context context) {
        mContext = context;
    }

    protected Void doInBackground(Object... params) {

        ContentResolver contentResolver = mContext.getContentResolver();
        ContentValues values;
        Integer id;

        switch ((Integer) params[0]) {
            case INSERT:
                values = (ContentValues) params[1];
                contentResolver.insert(DBContentProvider.PERSONS_CONTENT_URI, values);
                break;
            case DELETE_BY_ID:
                values = (ContentValues) params[1];
                contentResolver.delete(DBContentProvider.PERSONS_CONTENT_URI, PersonContract.KEY_NAME + " = ? ", new String[]{values.getAsString(PersonContract.KEY_NAME)});
                break;
            case UPDATE:
                id = (Integer) params[1];
                values = (ContentValues) params[2];
                contentResolver.update(DBContentProvider.PERSONS_CONTENT_URI.buildUpon().
                        appendPath(String.valueOf(id)).build(), values, null, null);
                contentResolver.notifyChange(DBContentProvider.PERSONS_CONTENT_URI, null);
                break;
            case QUERY_BY_ID:
                id = (Integer) params[1];
                contentResolver.query(DBContentProvider.PERSONS_CONTENT_URI.buildUpon().
                        appendPath(String.valueOf(id)).build(), null, null, null, null);
                break;
            case DELETE:
                contentResolver.insert(DBContentProvider.PERSONS_CONTENT_URI, null);
                break;
            default:
                break;
        }
        return null;
    }
}
