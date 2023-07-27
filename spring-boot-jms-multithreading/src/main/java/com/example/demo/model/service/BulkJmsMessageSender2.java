package com.example.demo.model.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;

import com.example.demo.model.Employee;

public class BulkJmsMessageSender2 {

	private final JAXBContext context;
	private JmsTemplate jmsTemplate;
	private ExecutorService executorService;

	public BulkJmsMessageSender2() throws JAXBException {
		context = JAXBContext.newInstance(Employee.class);

	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		this.executorService = Executors.newFixedThreadPool(10);
	}

	public List<Boolean> sendMessagesWithCorrelationId(List<String> messageTexts) {

		List<Boolean> result = new ArrayList<>();
		Future<List<Boolean>> future = executorService.submit(() -> publishMessages(messageTexts));
		try {
			result = future.get();
			System.out.println("Result : "+ result.size());
		} catch (InterruptedException | ExecutionException e) {
			System.out.println(e);
		}
		return result;
	}

	private List<Boolean> publishMessages(List<String> messageTexts) {

		return jmsTemplate.execute("employee", new ProducerCallback<List<Boolean>>() {

			@Override
			public List<Boolean> doInJms(Session session, MessageProducer producer) throws JMSException {
				List<Boolean> results = new ArrayList<>();

				for (String messageText : messageTexts) {
					System.out.println(Thread.currentThread().getName() + " sending messages ");
					try {
						TextMessage message = session.createTextMessage(messageText);
						producer.send(message);
						results.add(true);
					} catch (Exception e) {
						System.out.println("Exception occured : " + e);
						results.add(false);
					}
				}
				return results;
			}

		});
	}

	public List<String> toXMLList(List<Employee> employees) {
		return employees.stream().map(e -> toXmlString(e)).collect(Collectors.toList());
	}

	public String toXmlString(Employee employee) {
		try {
			StringWriter writer = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(employee, writer);
			return writer.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
