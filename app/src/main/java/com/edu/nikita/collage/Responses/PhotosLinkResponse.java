package com.edu.nikita.collage.Responses;

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

        @SerializedName("likes")
        private Likes likes;
        @SerializedName("videos")
        private Videos videos;
        @SerializedName("images")
        private Images image;

        public Videos getVideos() {
            return videos;
        }

        @SerializedName("id")
        private String imageId;

        public int getLikesCount() {
            return likes.count;
        }

        public Images getImage() {
            return image;
        }

        public String getImageId() {
            return imageId;
        }

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

        public int getCount() {
            return count;
        }
    }

    private class Images {
        @SerializedName("low_resolution")
        private Image lowResImage;

        public Image getLowResImage() {
            return lowResImage;
        }
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

        public String getLink() {
            return link;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private class Videos {

    }
}
