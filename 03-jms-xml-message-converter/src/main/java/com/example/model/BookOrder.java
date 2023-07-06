package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookOrder {
    private String bookOrderId;
    private Book book;
    private Customer customer;

    @JsonCreator
    public BookOrder(@JsonProperty("bookOrderId") String bookOrderId, @JsonProperty("book") Book book, @JsonProperty("customer") Customer customer) {
        this.bookOrderId = bookOrderId;
        this.book = book;
        this.customer = customer;
    }

}