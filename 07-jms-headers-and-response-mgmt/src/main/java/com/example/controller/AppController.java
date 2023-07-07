package com.example.controller;

import com.example.model.Book;
import com.example.model.BookOrder;
import com.example.model.Customer;
import com.example.service.BookOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private BookOrderService bookOrderService;

    List<Book> books = Arrays.asList(
            new Book("1", "Lord of the Flies"),
            new Book("2", "Being and Nothingness"),
            new Book("3", "At Sea and Lost"));


    List<Customer> customers = Arrays.asList(
            new Customer("1", "Michael Rodgers"),
            new Customer("2", "Jeff Peek"),
            new Customer("3", "Steve McClarney")
    );

    @RequestMapping(path = "/process/store/{storeId}/order/{orderId}/{customerId}/{bookId}/{orderState}/", method = RequestMethod.GET)
    public @ResponseBody
    String processOrder(@PathVariable("storeId") String storeId,
                        @PathVariable("orderId") String orderId,
                        @PathVariable("customerId") String customerId,
                        @PathVariable("bookId") String bookId,
                        @PathVariable("orderState") String orderState) {

        try {
            LOGGER.info("StoredId is = {}", storeId);
            bookOrderService.send(build(customerId, bookId, orderId), storeId, orderState);
        } catch (Exception exception) {
            return "Error occurred!" + exception.getLocalizedMessage();
        }
        return "Order sent to warehouse for bookId = " + bookId + " from customerId = " + customerId + " successfully processed!";
    }

    private BookOrder build(String customerId, String bookId, String orderId) {
        Book myBook = null;
        Customer myCustomer = null;

        for (Book bk : books) {
            if (bk.getBookId().equalsIgnoreCase(bookId)) {
                myBook = bk;
            }
        }
        if (null == myBook) {
            throw new IllegalArgumentException("Book selected does not exist in inventory!");
        }

        for (Customer ct : customers) {
            if (ct.getCustomerId().equalsIgnoreCase(customerId)) {
                myCustomer = ct;
            }
        }

        if (null == myCustomer) {
            throw new IllegalArgumentException("Customer selected does not appear to be valid!");
        }

        return new BookOrder(orderId, myBook, myCustomer);
    }
}