package com.zncm.timepill.modules.services;


import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.data.base.note.NoteTopicData;
import com.zncm.timepill.data.base.note.NoteComment;
import com.zncm.timepill.data.base.note.Pager;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface NoteService {
    @Multipart
    @POST("/notebooks/{note_book_id}/diaries")
    void addNoteWithFile(@Path("note_book_id") Integer note_book_id, @Query("content") String content, @Part("photo") TypedFile photo, @Query("join_topic") String join_topic, Callback<Response> callback);

    @POST("/notebooks/{note_book_id}/diaries")
    void addNote(@Path("note_book_id") Integer note_book_id, @Query("content") String content, @Query("join_topic") String join_topic, Callback<Response> callback);

    @PUT("/diaries/{note_id}")
    void updateNote(@Path("note_id") int note_id, @Query("content") String content, @Query("notebook_id") Integer notebook_id, Callback<Response> callback);

    @GET("/diaries/today")
    void getNotesToday(@Query("page") int page, @Query("page_size") int page_size, Callback<Pager<NoteData>> callback);

    @GET("/diaries/follow")
    void getNotesFollow(@Query("page") int page, @Query("page_size") int page_size, Callback<Pager<NoteData>> callback);

    @GET("/topic/diaries")
    void getNotesTopic(@Query("page") int page, @Query("page_size") int page_size, Callback<Pager<NoteData>> callback);

    @GET("/topic")
    void getTodayTopic(Callback<NoteTopicData> callback);

    @GET("/diaries/{id}")
    void getNote(@Path("id") int diary_id, Callback<NoteData> callback);

    @DELETE("/diaries/{id}")
    void delete(@Path("id") int diary_id, Callback<Response> callback);

    @GET("/diaries/{id}/comments")
    void getNotesComments(@Path("id") int id, Callback<List<NoteComment>> callback);

    @POST("/diaries/{id}/comments")
    void notesComment(@Path("id") int id, @Query("content") String content, @Query("recipient_id") Integer recipient_id, Callback<Response> callback);

    @DELETE("/comments/{id}")
    void deleteComments(@Path("id") int id, Callback<Response> callback);


    @GET("/users/{user_id}/diaries")
    void getNotesByUser(@Path("user_id") int user_id, Callback<List<NoteData>> callback);


    @GET("/diaries/search")
    void search(@Query("keywords") String keywords, @Query("page") int page, Callback<Pager<NoteData>> callback);
}
