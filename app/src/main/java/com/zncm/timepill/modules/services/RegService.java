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

public interface RegService {
    @POST("/users")
    void reg(@Query("name") String name, @Query("email") String email, @Query("password") String password, Callback<Response> callback);
}
