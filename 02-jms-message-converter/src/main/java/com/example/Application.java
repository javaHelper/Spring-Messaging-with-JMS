package com.example;

import com.example.model.Book;
import com.example.model.BookOrder;
import com.example.model.Customer;
import com.example.service.BookOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private BookOrderService service;

	@Override
	public void run(String... args) throws Exception {
		Book book = new Book("1","The Chava by Shivaji Sawant");
		Customer customer = new Customer("1","Harshita Dekate");
		BookOrder bookOrder = new BookOrder("1", book, customer);

		service.send(bookOrder);
	}
}