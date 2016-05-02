package com.edu.nikita.collage.Loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.edu.nikita.collage.Responses.BaseResponse;

import java.io.IOException;

/**
 * Created by Nikita on 27.04.2016.
 * Абстрактный AsyncTaskLoader для загрузки данных и сохранению их после загрузки,
 * для загрузки данных переопределить метод apiCall, для сохранения в классе
 * наследуемом от Response нужно переопределить метод Save
 */
public abstract class BaseLoader extends AsyncTaskLoader<BaseResponse> {

    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public BaseResponse loadInBackground() {
        try {
            //Выполняем запрос, получаем данные
            BaseResponse response = apiCall();
            if (response.getRequestResult() == BaseResponse.RequestResult.SUCCESS) {
                //Если успешно то сохраняем данные
                response.save(getContext());
                onSuccess();
            } else {
                onError();
            }
            return response;
        } catch (IOException e) {
            onError();
            return new BaseResponse();
        }
    }

    protected void onSuccess() {
    }

    protected void onError() {
    }

    /**
     * Метод для загрузки данных
     * @return Ответ
     * @throws IOException
     */
    protected abstract BaseResponse apiCall() throws IOException;
}