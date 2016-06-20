package com.edu.nikita.collage.Retrofit;

import com.edu.nikita.collage.Pojo.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Nikita on 05.06.2016.
 */
public interface GetAccessToken {
    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<TokenResponse> getAccessToken(@Field("client_id") String client_id, @Field("client_secret") String client_secret,
                                       @Field("redirect_uri") String redirect_uri, @Field("grant_type") String grant_type,
                                       @Field("code") String code);
}
