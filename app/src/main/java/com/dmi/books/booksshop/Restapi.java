package com.dmi.books.booksshop;

import com.dmi.books.booksshop.Model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Restapi {
    // Range secure request
    @GET("/api/v1/secure/items")
    Call<List<Book>> getSecureListBooks(@Query("offset") int offset, @Query("count") int count);

    @GET("/api/v1/items")
    Call<List<Book>> getListBooks(@Query("offset") int offset, @Query("count") int count);
}


/*
[{"id":"7","title":"Seven is my lucky number","price":7.77,"link":"/api/v1/items/7"},{"id":"8","title":"A Dance with Dragons","price":19.01,"link":"/api/v1/items/8"},{"id":"10","title":"Ten ways to a better mind","price":10.0,"link":"/api/v1/items/10"},{"id":"42","title":"The Hitch-hikers Guide to the Galaxy","price":5.62,"link":"/api/v1/items/42"},{"id":"200","title":"Book title #200","price":83.0,"link":"/api/v1/items/200"},{"id":"201","title":"Book title #201","price":1.0,"link":"/api/v1/items/201"},{"id":"202","title":"Book title #202","price":27.0,"link":"/api/v1/items/202"},{"id":"203","title":"Book title #203","price":14.0,"link":"/api/v1/items/203"},{"id":"204","title":"Book title #204","price":44.0,"link":"/api/v1/items/204"},{"id":"205","title":"Book title #205","price":22.0,"link":"/api/v1/items/205"}]
 */