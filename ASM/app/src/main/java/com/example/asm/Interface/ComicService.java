package com.example.asm.Interface;

import com.example.asm.Model.Comic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ComicService {
    @GET("/api/comic/")
    Call<List<Comic>> getComic();

    @POST("api/comic/")
    Call<Comic> postComic(@Body Comic comic);

    @DELETE("api/comic/{id}")
    Call<Void> deleteComic(@Path("id") String id);

    @PUT("api/comic/{id}")
    Call<Comic> updateComic(@Path("id") String id, @Body Comic comic);
}
