package com.edu.nikita.collage.Table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.edu.nikita.collage.ImageModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 29.04.2016.
 * Класс для упрощения работы бд
 */
public class CollageDB {


    public static final Uri URI = SQliteHelper.BASE_CONTENT_URI.buildUpon().appendPath(Requests.TABLE_NAME).build();

    public static void save(Context context, @NonNull ImageModel entry) {
        context.getContentResolver().insert(URI, toContentValues(entry));
    }

    public static void save(Context context, @NonNull ArrayList<ImageModel> list) {

        ContentValues[] values = new ContentValues[list.size()];

        for (int index = 0; index < list.size();index++ ) {
            values[index] = toContentValues(list.get(index));
        }

        context.getContentResolver().bulkInsert(URI, values);
    }

    /**
     * Создает обьект ImageModel из текущей позициикурсора
     * @param cursor
     * @return
     */
    @NonNull
    public static ImageModel fromCursor(@NonNull Cursor cursor) {
        try {
            String id = cursor.getString(cursor.getColumnIndex(Columns.id));
            String url = cursor.getString(cursor.getColumnIndex(Columns.url));
            int width = cursor.getInt(cursor.getColumnIndex(Columns.width));
            int height = cursor.getInt(cursor.getColumnIndex(Columns.height));
            int likes = cursor.getInt(cursor.getColumnIndex(Columns.likes));
            return new ImageModel(url, id, width, height, likes);
        }catch (CursorIndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
            return new ImageModel(null,null,0,0,0);
        }
    }

    /**
     * Список данных о изображеиях из курсора
     * @param cursor курсор в котором содержаьтся данные , после выполнения метода курсор будет закрыт
     * @return Список данных о изображениях
     */
    @NonNull
    public static ArrayList<ImageModel> listFromCursor(@NonNull Cursor cursor) {

        ArrayList<ImageModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        try {
            do {
                list.add(fromCursor(cursor));
            } while (cursor.moveToNext());
            return list;
        } finally {
            cursor.close();
        }
    }

    public static void clear(Context context) {

        context.getContentResolver().delete(URI, null, null);
    }


    @NonNull
    public static ContentValues toContentValues(@NonNull ImageModel input) {

        ContentValues values = new ContentValues();

        values.put(Columns.id, input.getId());
        values.put(Columns.url,input.getUrl());
        values.put(Columns.width, input.getWidth());
        values.put(Columns.height, input.getHeight());
        values.put(Columns.likes, input.getLikes() );

        return values;
    }


    /**
     * Названия стоолбцов используемых в базе данных
     */
    public interface Columns {
        String  id = "mId";
        String url = "url";
        String width = "width";
        String height = "height";
        String likes = "likes";
    }


    /**
     * Запросы к бд
     */
    public interface Requests {

        String TEXT_TYPE = " TEXT";
        String COMMA_SEP = ",";
        String INT_TYPE = " INT";
        String TABLE_NAME = CollageDB.class.getSimpleName();

        String CREATION_REQUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                Columns.id + INT_TYPE+COMMA_SEP+
                Columns.url + TEXT_TYPE+COMMA_SEP+
                Columns.width + INT_TYPE+COMMA_SEP+
                Columns.height + INT_TYPE+COMMA_SEP+
                Columns.likes + INT_TYPE+");";

        String DROP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
