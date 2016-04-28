package com.edu.nikita.collage.Loader;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;

import com.edu.nikita.collage.Responses.BaseResponse;
import com.edu.nikita.collage.Responses.PhotosLinkResponse;
import com.edu.nikita.collage.Responses.ResponseSearchUser;
import com.edu.nikita.collage.Retrofit.ApiFactory;
import com.edu.nikita.collage.Retrofit.PhotoList;
import com.edu.nikita.collage.Retrofit.UserSearch;

import java.io.IOException;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;

/**
 * Лоадер для загрузки списка пользователей c нужным ником, ник и client_id передаются в конструкторе
 */
public class PhotoListLoader extends BaseLoader{

    private String client;
    private String user;
    private int count = 20;

    /**
     * Инициализация лоадера для загрузки списка ссылок на фото пользователя
     * @param context контекст
     * @param user_id id пользователя
     * @param client_id id клиента прилжения
     */
    public PhotoListLoader(Context context, @NonNull String user_id,@NonNull String client_id,int count_in)
    {
        super(context);
        user = user_id;
        client = client_id;
        if(count_in >= 0)
            count = count_in;
    }



    /**
     * Выполняет запрос в instagram с id переданном в конструкторе класса возвращает список ссылок на фото пользователя
     * @return список фото пользователя
     * @throws IOException
     */
    @Override
    protected BaseResponse apiCall() throws IOException {
        List<PhotosLinkResponse.Data> result = new ArrayList<>();

        //Получаем сервис ретрофита
        PhotoList service = ApiFactory.getPhotoList();
        //Формируем запрос
        Call<PhotosLinkResponse> call = service.getPhotoList(user, client,String.valueOf(count));
        //Получаем ответ
        PhotosLinkResponse list = call.execute().body();
        //Т.к в каждом ответе есть url на следующие записи, инициализируем StringBuilder для удаления BASE_URL_API
        //из url, при составлении запроса он будет вставлен
        StringBuilder stringBuilder = new StringBuilder();

        //Пока есть url для следующих записей пвторяем запросы сохраняя данные
        while (list != null && list.getNextUrl() != null)
        {
            //Удаляем записи с видео
            removeMediaVideo(list.getData());
            //Добавляем в результирующий список
            result.addAll(list.getData());

            if(stringBuilder.length() > 0)
                stringBuilder.delete(0,stringBuilder.length()-1);
            stringBuilder.append(list.getNextUrl());
            stringBuilder.delete(0,stringBuilder.lastIndexOf(ApiFactory.BASE_URL_API));

            Call<PhotosLinkResponse> curCall = service.getPhotoListWithMaxId(stringBuilder.toString());
            list = curCall.execute().body();
        }

        Collections.sort(result);

        //Возвращаем список пользователей найденных по запросу
        return new BaseResponse().setRequestResult(BaseResponse.RequestResult.SUCCESS).setAnswer(result);
    }

    /**
     * Удаляет записи с видео из списка
     * @param list Списк с данными полученными от сервера
     */
    public void removeMediaVideo(@NonNull List<PhotosLinkResponse.Data> list)
    {
        for(PhotosLinkResponse.Data entry : list)
        {
            if(entry.getVideos() != null)
                list.remove(entry);
        }
    }
}
