package com.edu.nikita.collage.Responses;


import android.content.Context;

import com.edu.nikita.collage.ImageModel;
import com.edu.nikita.collage.Table.CollageDB;

import java.util.ArrayList;


/**
 * Created by Nikita on 29.04.2016.
 */
public class ImagesModelResponse extends BaseResponse {


    /**
     * Сохраненеи в базу данных о изображениях, чтобы в случае повторного обращения можно было не выполнять запрос снова
     * @param context
     */
    @Override
    public void save(Context context) {
        //Очищаем бд от предыдущих данных, если они есть
        CollageDB.clear(context);
        ArrayList<ImageModel> list = getTypedAnswer();
        if (list != null) {
            //Сохраняем в базу данных
            CollageDB.save(context,list);
        }
    }
}
