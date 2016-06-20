package com.edu.nikita.collage.Retrofit;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.OPTIONS;

/**
 * Created by Nikita on 17.04.2016.
 * Фабрика запросов
 */
public class ApiFactory {


    public static final String BASE_URL_API = "https://api.instagram.com/v1/";


    /**
     * Запрос на список полльзователей
     *
     * @return
     */
    @NonNull
    public static UserSearch getUserSearch() {
        return new Retrofit.Builder().
                baseUrl(BASE_URL_API).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(UserSearch.class);
    }

    /**
     * Запрос на медиа файлы пользователя
     *
     * @return
     */
    @NonNull
    public static PhotoList getPhotoList() {
        return new Retrofit.Builder().
                baseUrl(BASE_URL_API).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(PhotoList.class);
    }


    /**
     * Запрос на медиа файлы с указанием max_id
     *
     * @return
     */

    @NonNull
    public static PhotoList getPhotoListWithMaxId() {
        return new Retrofit.Builder().
                baseUrl(BASE_URL_API).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(PhotoList.class);
    }


    /**
     * Запрос accessToken
     */

    @NonNull
    public static GetAccessToken getAccessToken()
    {
        return new Retrofit.Builder().
                baseUrl(BASE_URL_API).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(GetAccessToken.class);
    }
}
