package com.edu.nikita.collage.Loader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.edu.nikita.collage.ImageModel;
import com.edu.nikita.collage.Responses.BaseResponse;
import com.edu.nikita.collage.Responses.ImagesModelResponse;
import com.edu.nikita.collage.Retrofit.PhotosLinkResponse;
import com.edu.nikita.collage.Retrofit.ApiFactory;
import com.edu.nikita.collage.Retrofit.PhotoList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Лоадер для загрузки списка фотографий пользователя,
 * возвращает список изображений отсортрованных по количеству лайков
 */
public class PhotoListLoader extends BaseLoader{

    private String accessToken;
    private String user;
    private int count = 20;

    /**
     * Инициализация лоадера для загрузки списка ссылок на фото пользователя
     * @param context контекст
     * @param user_id id пользователя
     * @param access_token токен
     */
    public PhotoListLoader(Context context, @NonNull String user_id,@NonNull String access_token,int count_in)
    {
        super(context);
        user = user_id;
        accessToken = access_token;
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
        ArrayList<PhotosLinkResponse.Data> result = new ArrayList<>();

        //Получаем сервис ретрофита
        PhotoList service = ApiFactory.getPhotoList();
        //Формируем запрос
        Call<PhotosLinkResponse> call = service.getPhotoList(user,accessToken ,String.valueOf(count));
        //Получаем ответ
        Response<PhotosLinkResponse> response = call.execute();
        PhotosLinkResponse list = response.body();
        if(list == null) {
            BaseResponse resultResponse = new BaseResponse().setRequestResult(BaseResponse.RequestResult.ERROR).setAnswer(null);
            if (response.code() == 400) {
                resultResponse.errorCode = 400;
            }
            return resultResponse;
        }
        //Удаляем из ответа видео
        removeMediaVideo(list.getData());

        //Сохраняем ответ в результирующий список
        result.addAll(list.getData());

        //Пока есть url для следующих записей пвторяем запросы сохраняя данные
        while (list.getNextUrl() != null)
        {
            service = ApiFactory.getPhotoListWithMaxId();
            Call<PhotosLinkResponse> curCall = service.getPhotoListWithMaxId(user, accessToken,String.valueOf(count),list.getMaxId());
            list = curCall.execute().body();
            //Удаляем записи с видео
            removeMediaVideo(list.getData());
            //Добавляем в результирующий список
            result.addAll(list.getData());
        }


        //Формируем из результирующего списка список с данными о изображении
        ArrayList<ImageModel> images = fromDataToImageModel(result);

        //Сорттируем по лайкам
        Collections.sort(images);

        //Возвращаем список пользователей найденных по запросу
        return new ImagesModelResponse().setRequestResult(BaseResponse.RequestResult.SUCCESS).setAnswer(images);
    }

    /**
     * Удаляет записи с видео из списка
     * @param list Списк с данными полученными от сервера
     */
    public void removeMediaVideo(@NonNull List<PhotosLinkResponse.Data> list)
    {
        for(int i = list.size()-1; i >= 0;i--)
        {
            if(list.get(i).getVideos() != null)
                list.remove(i);
        }
    }


    /**
     * Из списка Data делает список ImageModel
     * @param input список элементов Data
     * @return список элементов ImageModel
     */
    static public ArrayList<ImageModel> fromDataToImageModel(ArrayList<PhotosLinkResponse.Data> input)
    {
        ArrayList<ImageModel> result = new ArrayList<>(input.size());
        for(PhotosLinkResponse.Data tmpEntry: input)
        {
            result.add(new ImageModel(tmpEntry.getImageUrl(),tmpEntry.getImageId(),
                    tmpEntry.getWidth(),tmpEntry.getHeight(),tmpEntry.getLikesCount()));
        }
        return result;
    }

}
