package com.zncm.timepill.modules.services;


import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.data.base.note.NoteTopicData;
import com.zncm.timepill.data.base.note.Pager;
import com.zncm.timepill.data.base.relation.RelationPager;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RelationService {

    @GET("/relation/{user_id}")
    public void getRelation(@Path("user_id") int user_id, Callback<UserData> callback);

    @GET("/relation/reverse")
    void getAttentionMe(@Query("page") int page, @Query("page_size") int page_size, Callback<RelationPager<UserData>> callback);

    @GET("/relation")
    void getMeAttention(@Query("page") int page, @Query("page_size") int page_size, Callback<RelationPager<UserData>> callback);


    @POST("/relation/{user_id}")
    public void addRelation(@Path("user_id") int user_id, Callback<UserData> callback);

    @DELETE("/relation/{user_id}")
    public void deleteRelation(@Path("user_id") int user_id, Callback<Response> callback);

}
