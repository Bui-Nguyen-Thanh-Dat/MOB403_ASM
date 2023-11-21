package com.example.asm.Interface;

import com.example.asm.Model.Comic;
import com.example.asm.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @POST("/api/user/")
    Call<User> getUser(@Query("email") String email,@Query("password") String password);

    @POST("api/user/addUser")
    Call<List<User>> postUser(@Body User user);

    @DELETE("api/user/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @PUT("api/user/{id}")
    Call<User> updateUser(@Path("id") String id,@Body User user );


}
