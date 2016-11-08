package com.aaroncampbell.budget.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by aaroncampbell on 11/2/16.
 */

public class Expense {
    @SerializedName("id")
    private Integer id;
    @SerializedName("amount")
    private Double amount;
    @SerializedName("categoryId")
    private Integer categoryId;
    @SerializedName("date")
    private Date date;
    @SerializedName("note")
    private String note;
    @SerializedName("categoryName")
    private String categoryName;

    public Expense() {
    }

    public Expense(Integer categoryId, Double amount, Date date, String note) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
