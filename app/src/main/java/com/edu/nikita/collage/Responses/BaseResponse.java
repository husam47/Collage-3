package com.edu.nikita.collage.Responses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Nikita on 27.04.2016.
 */
public class BaseResponse  {

    public int errorCode = -1;
    @Nullable
    private Object mAnswer;

    private RequestResult mRequestResult;

    /**
     * Конструткор базового класса для ответов, при реализации своего ответа
     * нужно переопределить функцию сохранения. По умолчанию состояние mRequestResult ==  ERROR.
     * При успешном выполнении запроса нужно выстовить  SUCCESS
     */
    public BaseResponse() {
        mRequestResult = RequestResult.ERROR;
    }

    @NonNull
    public RequestResult getRequestResult() {
        return mRequestResult;
    }

    public BaseResponse setRequestResult(RequestResult requestResult) {
        mRequestResult = requestResult;
        return this;
    }

    /**
     * Возврашаем приведенный с типу T список обьектов
     * @param <T> Тип обьектов ответа
     * @return Результат ответа
     */
    @Nullable
    public <T> T getTypedAnswer() {
        if (mAnswer == null) {
            return null;
        }
        //noinspection unchecked
        return (T) mAnswer;
    }

    public BaseResponse setAnswer(@Nullable Object answer) {
        mAnswer = answer;
        return this;
    }

    public void save(Context context) {
    }

    public enum RequestResult {

        SUCCESS,
        ERROR

    }
}
