package com.aaroncampbell.peoplemon.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by aaroncampbell on 11/6/16.
 */

public class User {
    @SerializedName("UserId")
    private String userId;
    @SerializedName("CaughtUserId")
    private String caughtUserId;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("AvatarBase64")
    private String base64;
    @SerializedName("Longitude")
    private double longitude;
    @SerializedName("Latitude")
    private double latitude;
    @SerializedName("Created")
    private Date createDate;
    @SerializedName("radiusInMeters")
    private Integer radiusInMeters;

    public User() {
    }

    public User(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public User(String userId, String userName, String base64, double longitude, double latitude, Date createDate) {
        this.userId = userId;
        this.userName = userName;
        this.base64 = base64;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createDate = createDate;
    }

    public User(String caughtUserId, Integer radiusInMeters) {
        this.caughtUserId = caughtUserId;
        this.radiusInMeters = radiusInMeters;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCaughtUserId() {
        return caughtUserId;
    }
    public void setCaughtUserId(String caughtUserId) {
        this.caughtUserId = caughtUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getRadiusInMeters() {
        return radiusInMeters;
    }
    public void setRadiusInMeters(Integer radiusInMeters) {
        this.radiusInMeters = radiusInMeters;
    }
}
