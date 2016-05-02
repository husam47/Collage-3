package com.edu.nikita.collage.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nikita on 28.04.2016.
 * Овет от инсаграмма при запросе медиа контента пользователя
 */
public class PhotosLinkResponse {


    @SerializedName("pagination")
    private Pagination pagination;
    @SerializedName("data")
    private List<Data> data;


    public List<Data> getData() {
        return data;
    }

    public String getNextUrl() {
        return pagination.getNextUrl();
    }

    public String getMaxId (){return pagination.getNextMaxId();}




    private class Pagination {
        @SerializedName("next_url")
        private String nextUrl;
        @SerializedName("next_max_id")
        private String nextMaxId;

        public String getNextUrl() {
            return nextUrl;
        }

        public String getNextMaxId() {
            return nextMaxId;
        }
    }

    public class Data implements Comparable<PhotosLinkResponse.Data> {


        public int getLikesCount() {
            return likes.count;
        }
        public String getImageUrl()
        {
            return image.lowResImage.link;
        }

        public String getImageId(){return imageId;}
        public int getWidth(){return image.lowResImage.width;}
        public int getHeight(){return image.lowResImage.height;}
        public Videos getVideos() {
            return videos;
        }


        @SerializedName("likes")
        private Likes likes;
        @SerializedName("videos")
        private Videos videos;
        @SerializedName("images")
        private Images image;



        @SerializedName("id")
        private String imageId;



        @Override
        public int compareTo(Data another) {
            int R = another.getLikesCount();
            if(likes.count > R)
                return 1;
            if(likes.count < R)
                return -1;
            return 0;
        }
    }

    private class Likes {
        @SerializedName("count")
        private int count;
    }

    private class Images {
        @SerializedName("low_resolution")
        private Image lowResImage;
    }

    /**
     * Изображение в низком разрещении
     */
    public class Image {
        @SerializedName("url")
        private String link;

        @SerializedName("width")
        private int width;

        @SerializedName("height")
        private int height;

    }

    private class Videos {

    }
}
