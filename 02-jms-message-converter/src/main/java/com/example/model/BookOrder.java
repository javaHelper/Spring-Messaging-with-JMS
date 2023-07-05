package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
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