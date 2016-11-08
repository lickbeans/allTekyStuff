package com.aaroncampbell.budget.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Locale;

/**
 * Created by aaroncampbell on 11/1/16.
 */

public class Category {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("amount")
    private Double amount;
    @SerializedName("startDate")
    private Date startDate;

    public Category() {

    }

    public Category(String name, Double amount) {
        this.name = name;
        this.amount = amount;
        this.startDate = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
