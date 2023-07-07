package com.example.receiver;

import java.util.Map;

import com.example.model.ProcessedBookOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsMessageHeaderAccessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.example.model.BookOrder;
import com.example.service.WarehouseProcessingService;

@Service
public class WarehouseReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseReceiver.class);

    @Autowired
    private WarehouseProcessingService warehouseProcessingService;

    @JmsListener(destination = "book.order.queue")
    @SendTo("book.order.processed.queue")
    public ProcessedBookOrder receive(@Payload BookOrder bookOrder,
                                               @Headers Map<String, Object> headers,
                                               MessageHeaders messageHeaders,
                                               JmsMessageHeaderAccessor jmsMessageHeaderAccessor) {
        LOGGER.info("Message received!");
        LOGGER.info("Message is == " + bookOrder);


        LOGGER.info("headers : {}", headers);

        LOGGER.info("--------------------------");
        LOGGER.info("messageHeaders : " + messageHeaders.get("bookOrderId"));
        LOGGER.info("storeId : " + messageHeaders.get("storeId"));
        LOGGER.info("orderState : " + messageHeaders.get("orderState"));

        String storeId = (String) messageHeaders.get("storeId");
        String  orderState = (String) messageHeaders.get("orderState");

        LOGGER.info("--------------------------");
        LOGGER.info("\n# Spring JMS retrieving all header properties JmsMessageHeaderAccessor");
        LOGGER.info("- jms_destination=" + jmsMessageHeaderAccessor.getDestination());
        LOGGER.info("- jms_priority=" + jmsMessageHeaderAccessor.getPriority());
        LOGGER.info("- jms_timestamp=" + jmsMessageHeaderAccessor.getTimestamp());
        LOGGER.info("- jms_type=" + jmsMessageHeaderAccessor.getType());
        LOGGER.info("- jms_redelivered=" + jmsMessageHeaderAccessor.getRedelivered());
        LOGGER.info("- jms_replyTo=" + jmsMessageHeaderAccessor.getReplyTo());
        LOGGER.info("- jms_correlationId=" + jmsMessageHeaderAccessor.getCorrelationId());
        LOGGER.info("- jms_contentType=" + jmsMessageHeaderAccessor.getContentType());
        LOGGER.info("- jms_expiration=" + jmsMessageHeaderAccessor.getExpiration());
        LOGGER.info("- jms_messageId=" + jmsMessageHeaderAccessor.getMessageId());
        LOGGER.info("- jms_deliveryMode=" + jmsMessageHeaderAccessor.getDeliveryMode() + "\n");


//        if (bookOrder.getBook().getTitle().startsWith("L")) {
//            throw new IllegalArgumentException("bookOrderId=" + bookOrder.getBookOrderId() + " is of a book not allowed!");
//        }
        return warehouseProcessingService.processOrder(bookOrder, orderState, storeId);
    }
}