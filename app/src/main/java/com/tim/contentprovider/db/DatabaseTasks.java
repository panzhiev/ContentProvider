package com.tim.contentprovider.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Tim on 08.02.2017.
 */

public class DatabaseTasks extends AsyncTask<Object, Void, Void> {

    private final Context mContext;
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

        switch ((Integer) params[0]) {
            case DELETE_BY_ID:

                break;
            case UPDATE:

                break;
            case QUERY_BY_ID:
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
