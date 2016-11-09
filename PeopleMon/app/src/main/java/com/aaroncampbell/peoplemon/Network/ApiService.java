package com.aaroncampbell.peoplemon.Network;

import com.aaroncampbell.peoplemon.Models.Account;
import com.aaroncampbell.peoplemon.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("/token")
    Call<Account> login(
            @Field("grant_type") String grant_type,
            @Field("username") String username,
            @Field("password") String password);

    @POST("/api/Account/Register")
    Call<Void> register(@Body Account account);

    @GET("/v1/User/Nearby")
    Call<User[]> nearby(@Body User user);

    @POST("/v1/User/CheckIn")
    Call<User> checkIn(@Body User user);

    @GET("/api/Account/UserInfo")
    Call<Account> viewProfile(@Body Account account);

    @POST("/api/Account/UserInfo")
    Call<Account> editProfile(@Body Account account);
}
