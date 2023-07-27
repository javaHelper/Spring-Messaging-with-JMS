package com.example.demo;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.example.demo.model.Employee;
import com.example.demo.model.service.BulkJmsMessageSender;
import com.github.javafaker.Faker;

@SpringBootApplication
@ImportResource("appContext.xml")
public class SpringBootJmsMultithreadingApplication {

	private static final Faker FAKER = Faker.instance(Locale.ENGLISH);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootJmsMultithreadingApplication.class,
				args);

		BulkJmsMessageSender messageSender = context.getBean(BulkJmsMessageSender.class);

		List<Employee> employees = IntStream.range(0, 100)
				.boxed()
				.map(index -> {
					String firstName = FAKER.name().firstName();
					String lastName = FAKER.name().lastName();

					return Employee.builder()
							.id(index)
							.firstName(firstName)
							.lastName(lastName)
							.age(ThreadLocalRandom.current().nextInt(1, 70))
							.emailId(firstName + "." + lastName + "@spring.com")
							.salary(ThreadLocalRandom.current().nextDouble(1000, 10000)).build();
				}).collect(Collectors.toList());

		List<String> xmlList = messageSender.toXMLList(employees);

		List<Boolean> response = messageSender.sendMessagesWithCorrelationId(xmlList);
		System.out.println("Count : " + response.size());
	}
}
