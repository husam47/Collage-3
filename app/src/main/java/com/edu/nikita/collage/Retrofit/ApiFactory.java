package com.edu.nikita.collage.Retrofit;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.OPTIONS;

/**
 * Created by Nikita on 17.04.2016.
 */
public class ApiFactory {


    public  static final String BASE_URL_API = "https://api.instagram.com/v1/";



    @NonNull
    public static UserSearch getUserSearch()
    {
        return new Retrofit.Builder().
                baseUrl(BASE_URL_API).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(UserSearch.class);
    }

    @NonNull
    public  static  PhotoList getPhotoList()
    {
        return new Retrofit.Builder().
                baseUrl(BASE_URL_API).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(PhotoList.class);
    }

    @NonNull
    public static PhotoList getPhotoListWithMaxId()
    {
        return new Retrofit.Builder().
                baseUrl(BASE_URL_API).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(PhotoList.class);
    }



}
