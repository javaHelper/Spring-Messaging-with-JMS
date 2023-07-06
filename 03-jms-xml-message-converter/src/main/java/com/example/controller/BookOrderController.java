package com.example.controller;

import com.example.Application;
import com.example.model.BookOrder;
import com.example.service.BookOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookOrderController {
    @Autowired
    private BookOrderService service;

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE})
    public void saveBookOrder(@RequestBody BookOrder bookOrder) {
        service.send(bookOrder);
    }
}