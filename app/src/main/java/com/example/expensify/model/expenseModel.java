package com.example.expensify.model;

import java.io.Serializable;

public class expenseModel implements Serializable {
    private String category, note, id, date;
    private int amount;
    private String type;

    public expenseModel() {
    }

    public expenseModel(String category, String note, String id, String date, int amount, String type) {
        this.category = category;
        this.note = note;
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}