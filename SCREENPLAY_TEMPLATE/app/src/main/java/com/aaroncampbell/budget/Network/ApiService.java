package com.aaroncampbell.budget.Network;

import com.aaroncampbell.budget.Models.TestPost;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public interface ApiService {
    @GET("/posts/{id}")
    Call<TestPost> getPost(@Path("id") Integer id);

    @POST("/posts")
    Call<TestPost> postPost(@Body TestPost post);

    @GET("/posts")
    Call<TestPost[]> webPost();
}
