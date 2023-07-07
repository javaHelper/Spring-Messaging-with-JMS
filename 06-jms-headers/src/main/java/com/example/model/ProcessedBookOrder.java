package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProcessedBookOrder {
    private BookOrder bookOrder;
    private Date processingDateTime;
    private Date expectedShippingDateTime;

    @JsonCreator
    public ProcessedBookOrder(
            @JsonProperty("bookOrder") BookOrder bookOrder,
            @JsonProperty("processingDateTime") Date processingDateTime,
            @JsonProperty("expectedShippingDateTime") Date expectedShippingDateTime) {
        this.bookOrder = bookOrder;
        this.processingDateTime = processingDateTime;
        this.expectedShippingDateTime = expectedShippingDateTime;
    }
}