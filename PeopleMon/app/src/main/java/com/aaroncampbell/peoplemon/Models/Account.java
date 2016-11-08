package com.aaroncampbell.peoplemon.Models;

import com.aaroncampbell.peoplemon.Components.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class Account {
    @SerializedName("Email")
    private String email;
    @SerializedName("FullName")
    private String fullName;
    @SerializedName("AvatarBase64")
    private String base64Avatar;
    @SerializedName("ApiKey")
    private String apiKey;
    @SerializedName("Password")
    private String password;
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("grantType")
    private String grantType;
    @SerializedName("expiration")
    private Date expiration;

    public Account() {
    }

    public Account(String email, String fullName, String base64Avatar, String apiKey, String password) {
        this.email = email;
        this.fullName = fullName;
        this.base64Avatar = base64Avatar;
        this.apiKey = apiKey;
        this.password = password;
    }

    public Account(String grantType, String email, String password) {
        this.grantType = Constants.GRANT_TYPE;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBase64Avatar() {
        return base64Avatar;
    }

    public void setBase64Avatar(String base64Avatar) {
        this.base64Avatar = base64Avatar;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return access_token;
    }

    public void setToken(String accessToken) {
        this.access_token = access_token;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
