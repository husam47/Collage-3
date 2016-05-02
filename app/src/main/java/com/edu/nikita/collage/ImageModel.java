package com.edu.nikita.collage;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by Nikita on 29.04.2016.
 * Данные для изображения
 */
public class ImageModel extends Object implements Comparable<ImageModel> {

    public ImageModel(String url_,String id_,int width_,int height_,int likes_)
    {
        url = url_;
        id = id_;
        width = width_;
        height = height_;
        likes = likes_;
    }

    private String url;
    private String id;
    private int width;
    private int height;
    private int likes;


    @Override
    public int compareTo(@NonNull ImageModel another) {
        int l = likes;
        int r = another.likes;
        if(l < r)
            return 1;
        if(l > r)
            return -1;
        return 0;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUrl() {

        return url;
    }

    public String getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLikes() {
        return likes;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageModel))
            return false;
        else {

            return id.equals(((ImageModel) o).id);
        }
    }
}
