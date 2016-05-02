package com.edu.nikita.collage.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.edu.nikita.collage.Table.CollageDB;
import com.edu.nikita.collage.Table.SQliteHelper;

/**
 * Created by Nikita on 29.04.2016.
 * Контент провайдер для работы с базой данных.
 */
public class MyContentProvider extends ContentProvider {

    private static final int IMAGES_TABLE = 1;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(SQliteHelper.CONTENT_AUTHORITY, CollageDB.Requests.TABLE_NAME, IMAGES_TABLE);
    }

    private SQliteHelper mSqliteHelper;


    @Override
    public boolean onCreate() {
        mSqliteHelper = new SQliteHelper(getContext());
        return true;
    }


    @NonNull
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case IMAGES_TABLE:
                return CollageDB.Requests.TABLE_NAME;
            default:
                return "";
        }
    }

    @Override
    @NonNull
    public Cursor query(@NonNull Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        Log.d("Provider"," query");
        SQLiteDatabase database = mSqliteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new UnsupportedOperationException("No such table to query");
        } else {
            return database.query(table,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        }
    }

    @Override
    @NonNull public Uri insert(@NonNull Uri uri, @NonNull ContentValues values) {
        Log.d("Provider "," insert");
        SQLiteDatabase database = mSqliteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new UnsupportedOperationException("No such table to query");
        }
        else {
            long id = database.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return ContentUris.withAppendedId(uri, id);
        }
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        Log.d("Provider ","bulkInsert");
        SQLiteDatabase database = mSqliteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new UnsupportedOperationException("No such table to query");
        }
        else {
            int numInserted = 0;
            database.beginTransaction();
            try {
                for (ContentValues contentValues : values) {
                    long id = database.insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                    //Вставить values если есть конфликт заменить
                    if (id > 0) {
                        numInserted++;
                    }
                }
                database.setTransactionSuccessful();
            }
            finally {
                database.endTransaction();
            }
            return numInserted;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.d("Provider "," delete ");
        SQLiteDatabase database = mSqliteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new UnsupportedOperationException("No such table to query");
        }
        else {
            return database.delete(table, selection, selectionArgs);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @NonNull ContentValues values,
                      String selection, String[] selectionArgs) {
        Log.d("Provider ","update");
        SQLiteDatabase database = mSqliteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new UnsupportedOperationException("No such table to query");
        }
        else {
            return database.update(table, values, selection, selectionArgs);
        }
    }
}