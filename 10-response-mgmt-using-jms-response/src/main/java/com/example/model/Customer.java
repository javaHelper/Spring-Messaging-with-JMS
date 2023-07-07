package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Customer {
    private String customerId;
    private String fullName;

    @JsonCreator
    public Customer(@JsonProperty("customerId") String customerId, @JsonProperty("fullName") String fullName) {
        this.customerId = customerId;
        this.fullName = fullName;
    }
}