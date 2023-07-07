package com.example.service;

import com.example.model.BookOrder;
import com.example.model.ProcessedBookOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Configuration
public class WarehouseProcessingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseProcessingService.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    public void processOrder(BookOrder bookOrder){
        ProcessedBookOrder order = new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()

        );
        LOGGER.info("sending message "+ order);
        jmsTemplate.convertAndSend("book.order.processed.queue", order);
    }
}