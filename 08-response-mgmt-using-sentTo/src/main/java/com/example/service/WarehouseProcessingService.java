package com.example.service;

import com.example.model.BookOrder;
import com.example.model.ProcessedBookOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Configuration
public class WarehouseProcessingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseProcessingService.class);

    @Transactional
    public ProcessedBookOrder processOrder(BookOrder bookOrder, String orderState, String storeId) {

        if ("NEW".equalsIgnoreCase(orderState)) {
            return add(bookOrder, storeId);
        } else if ("UPDATE".equalsIgnoreCase(orderState)) {
            return update(bookOrder, storeId);
        } else if ("DELETE".equalsIgnoreCase(orderState)) {
            return delete(bookOrder, storeId);
        } else {
            throw new IllegalArgumentException("WarehouseProcessingService.processOrder(...) - orderState does not match expected criteria!");
        }
    }

    private ProcessedBookOrder add(BookOrder bookOrder, String storeId) {
        LOGGER.info("ADDING A NEW ORDER TO DB");
        return new ProcessedBookOrder(bookOrder, new Date(), null);
    }

    private ProcessedBookOrder update(BookOrder bookOrder, String storeId) {
        LOGGER.info("UPDATING A ORDER TO DB");
        return new ProcessedBookOrder(bookOrder, new Date(), null);
    }

    private ProcessedBookOrder delete(BookOrder bookOrder, String storeId) {
        LOGGER.info("DELETING ORDER FROM DB");
        return new ProcessedBookOrder(bookOrder, new Date(), null);
    }
}