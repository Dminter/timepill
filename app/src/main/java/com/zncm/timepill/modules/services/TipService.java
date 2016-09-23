package com.zncm.timepill.modules.services;


import com.zncm.timepill.data.base.tips.TipData;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface TipService {

    @GET("/tip")
    void getTips(Callback<List<TipData>> callback);

    @POST("/tip/read/{tip_id}")
    void readTips(@Path("tip_id") int tip_id, Callback<Response> callback);

}
