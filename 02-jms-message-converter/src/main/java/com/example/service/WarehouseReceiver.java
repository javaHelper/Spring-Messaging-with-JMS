package com.example.service;

import com.example.model.BookOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class WarehouseReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseReceiver.class);

    @Autowired
    private ObjectMapper objectMapper;


    @JmsListener(destination = "book.order.queue")
    public void receive(BookOrder bookOrder) throws JsonProcessingException {
        LOGGER.info("Message received!");
        System.out.println(objectMapper.writeValueAsString(bookOrder));
    }
}