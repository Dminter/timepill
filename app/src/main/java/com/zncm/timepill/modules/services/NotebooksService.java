package com.zncm.timepill.modules.services;


import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.data.base.note.NoteData;
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

public interface NotebooksService {
    @GET("/notebooks/my")
    void getNotebooks(Callback<List<NoteBookData>> callback);

    @GET("/users/{id}/notebooks")
    void getNotebooksByUser(@Path("id") int user_id, Callback<List<NoteBookData>> callback);

    @GET("/notebooks/{notebook_id}/diaries")
    void getNotebooksDiaries(@Path("notebook_id") int notebook_id, @Query("page") int page, Callback<Pager<NoteData>> callback);

    @POST("/notebooks")
    void addNotebook(@Query("subject") String subject, @Query("description") String description, @Query("privacy") int privacy, @Query("expired") String expired, Callback<Response> callback);

    @PUT("/notebooks/{notebook_id}")
    void updateNotebook(@Path("notebook_id") int notebook_id, @Query("subject") String subject, @Query("description") String description, @Query("privacy") int privacy, Callback<Response> callback);

    @Multipart
    @POST("/notebooks/{notebook_id}/cover")
    public void setNotebookCover(@Path("notebook_id") int notebook_id, @Part("cover") TypedFile photo, Callback<Response> callback);

    @DELETE("/notebooks/{id}")
    void deleteNotebook(@Path("id") int notebook_id, Callback<Response> callback);
}
