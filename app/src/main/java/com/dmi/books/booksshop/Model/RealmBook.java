package com.dmi.books.booksshop.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class RealmBook extends RealmObject{

    private String id;
    private String title;
    private Double price;
    private String link;

    public RealmBook(){}

    public RealmBook(String id, String title, double price, String link) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.link = link;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
