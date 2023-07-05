package com.example.demo;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @JmsListener(destination = "order-queue", containerFactory = "warehouseFactory")
    public void receiveMessage(String order){
        System.out.println("Order received is: "+ order);
    }
}