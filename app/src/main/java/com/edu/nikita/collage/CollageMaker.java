package com.edu.nikita.collage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.edu.nikita.collage.Retrofit.PhotosLinkResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;


/**
 * Created by Nikita on 29.04.2016.
 * Класс для создания коллажа
 */
public class CollageMaker {
    /**
     * Общее количество изображений
     */
    int numOfImages;
    /**
     * Количество изобраений уже загруженных
     */
    int count;
    /**
     * Ширина, высота коллажа
     */
    CollageWH collageWH;
    /**
     * Результирующее изображение коллажа
     */
    Bitmap resultBitmap;
    /**
     * Канвас для отрисовки изображений
     */
    Canvas canvas;

    /**
     * Текущие характеристики коллажа
     */
    CollageWH tmp;
    /**
     * ImageView куда следует загрузить коллаж для просмотра
     */
    ImageView imageView;

    public CollageMaker(ImageView image)
    {
        imageView = image;
    }

    public void ininiate(ArrayList<ImageModel> list)
    {
        collageWH = new CollageWH();
        collageWH.calculateWidtAndHeight(list);
        count = list.size();
        numOfImages= list.size();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        resultBitmap = Bitmap.createBitmap(collageWH.width, collageWH.height, conf);
        canvas = new Canvas(resultBitmap);
        tmp = new CollageWH();
    }


    /**
     * Очередное изображение загрузилось, добавляем его в наше результирующее изображение
     * @param bitmap ссылка на загруженные изобрадения
     * @param from Откуда была загрузка
     */
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        oneImageLoad();
        canvas.drawBitmap(bitmap,tmp.width,tmp.height,null);
        tmp.width+=bitmap.getWidth();
        if(tmp.width == collageWH.width) {
            tmp.width = 0;
            tmp.height+=bitmap.getHeight();
        }
        if(count <= 0 && imageView.getVisibility() == View.VISIBLE)
        {
            imageView.setImageBitmap(resultBitmap);
        }
    }

    /**
     * Изоражение не загружено
     * @param errorDrawable
     */
    public void onBitmapFailed(Drawable errorDrawable) {
        oneImageLoad();
    }



    public CollageWH getCollageWH() {
        return collageWH;
    }

    public void oneImageLoad()
    {
        count--;
    }

    /**
     * Класс для характеристик коллажа
     */
    public class CollageWH{

        public int width  = 0 ;
        public int height = 0;

        /**
         * Метод высчитывает и сохраняет данные о высоте и ширине коллажа
         * реализованы самые обычные варианты с количество изображений не больше 4
         * при количестве больше 4 ширина и высота быдет 0
         * @param imageModels
         */
        public void calculateWidtAndHeight(final ArrayList<ImageModel> imageModels)
        {
            width = 0;
            height = 0;
            switch (imageModels.size())
            {
                case 1:
                {
                    //Если изображение одно то размер коллажа будет равен размеру изображения
                    width = imageModels.get(0).getWidth();
                    height = imageModels.get(0).getHeight();
                    break;
                }
                case 2:
                {
                    //Если изображений два то рапологаем их друг над другом
                    width = Math.max(imageModels.get(0).getWidth(),imageModels.get(1).getWidth());
                    for(ImageModel entry:imageModels)
                    {
                        height += entry.getHeight();
                    }
                    break;
                }
                case 3:
                {
                    //Если изображений три то рапологаем их друг над другом
                    width = Math.max(Math.max(imageModels.get(0).getWidth(),
                            imageModels.get(1).getWidth()),imageModels.get(2).getWidth());
                    for(ImageModel entry:imageModels)
                    {
                        height += entry.getHeight();
                    }
                    break;
                }
                case 4:
                {
                    //Распологаем по два
                    width = imageModels.get(0).getWidth()+imageModels.get(1).getWidth();
                    height = imageModels.get(0).getHeight()+imageModels.get(3).getHeight();
                    break;
                }
            }
        }
    }
}
