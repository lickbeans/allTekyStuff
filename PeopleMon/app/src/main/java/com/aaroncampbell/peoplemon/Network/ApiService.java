package com.aaroncampbell.peoplemon.Network;

import com.aaroncampbell.peoplemon.Models.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
}
