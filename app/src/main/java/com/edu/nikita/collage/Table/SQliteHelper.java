package com.edu.nikita.collage.Table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by Nikita on 29.04.2016.
 */
public class SQliteHelper extends SQLiteOpenHelper {


    public static final String CONTENT_AUTHORITY = "ru.nikita.edu.collage";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String DATABASE_NAME = "ru.nikita.database.db";

    private static final int DATABASE_VERSION = 1;

    public SQliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CollageDB.Requests.CREATION_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CollageDB.Requests.DROP_REQUEST);
        onCreate(db);
    }
}
