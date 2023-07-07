package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Book {
    private String bookId;
    private String title;

    @JsonCreator
    public Book(@JsonProperty("bookId") String bookId, @JsonProperty("title") String title) {
        this.bookId = bookId;
        this.title = title;
    }
}