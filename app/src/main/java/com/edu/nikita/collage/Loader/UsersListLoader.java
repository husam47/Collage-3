package com.edu.nikita.collage.Loader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.edu.nikita.collage.Responses.BaseResponse;
import com.edu.nikita.collage.Responses.ResponseSearchUser;
import com.edu.nikita.collage.Retrofit.ApiFactory;
import com.edu.nikita.collage.Retrofit.UserSearch;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by Nikita on 27.04.2016.
 */
public class UsersListLoader extends BaseLoader {

    private String client;
    private String user;

    public UsersListLoader(Context context, @NonNull String username, @NonNull String client_id)
    {
        super(context);
        user = username;
        client = client_id;
    }

    /**
     * Выполняет запрос в instagram с id переданном в конструкторе класса возвращает список пользователей для ника переданного в конструкторе класса
     * @return список пользователей полученных от instagram, с никнеймом переданном в конструкторе
     * @throws IOException
     */
    @Override
    protected BaseResponse apiCall() throws IOException {
        UserSearch service = ApiFactory.getUserSearch();
        //Формируем запрос
        Call<ResponseSearchUser> call = service.search(user, client);
        //Получаем ответ
        ResponseSearchUser users = call.execute().body();
        //Возвращаем список пользователей найденных по запросу
        return new BaseResponse().setRequestResult(BaseResponse.RequestResult.SUCCESS).setAnswer(users.getUsers());
    }
}
