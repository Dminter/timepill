package com.zncm.timepill.modules.services;


import com.zncm.timepill.data.base.base.UserData;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface UserService {
    @GET("/users/my")
    void getMyInfo(Callback<UserData> callback);

    @GET("/users/{user_id}")
    void getUserInfo(@Path("user_id") int user_id, Callback<UserData> callback);

    @Multipart
    @POST("/users/icon")
    public void setUserCover(@Part("icon") TypedFile icon, Callback<Response> callback);

    @PUT("/users")
    void updateUser(@Query("name") String name, @Query("intro") String intro, Callback<Response> callback);

}
