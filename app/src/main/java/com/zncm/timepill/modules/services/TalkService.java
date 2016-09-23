package com.zncm.timepill.modules.services;


import com.zncm.timepill.data.base.note.NoteComment;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.data.base.note.NoteTopicData;
import com.zncm.timepill.data.base.note.Pager;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface TalkService {
    @POST("/app/users/{user_id}/messages")
    void sendMsg(@Path("user_id") int user_id, @Query("content") String content, Callback<Response> callback);
}
