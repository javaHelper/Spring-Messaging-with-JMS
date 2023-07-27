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


// https://stackoverflow.com/questions/42276707/how-to-use-return-value-from-executorservice
public class BulkJmsMessageSender {

	private final JAXBContext context;
	private JmsTemplate jmsTemplate;
	private ExecutorService executorService;

	public BulkJmsMessageSender() throws JAXBException {
		context = JAXBContext.newInstance(Employee.class);

	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		this.executorService = Executors.newFixedThreadPool(10);
	}

	public List<Boolean> sendMessagesWithCorrelationId(List<String> messageTexts) {

		List<Boolean> results = new ArrayList<>();

		List<Future<Boolean>> futures = new ArrayList<>();
		for (String messageText : messageTexts) {
			Future<Boolean> future = executorService.submit(() -> publishMessages(messageText));
			futures.add(future);
		}

		for (Future<Boolean> future : futures) {
			try {
				Boolean result = future.get();
				results.add(result);
			} catch (InterruptedException | ExecutionException ex) {
				System.out.println(ex);
			}
		}

		return results;
	}

	private Boolean publishMessages(String messageText) {

		return jmsTemplate.execute("employee", new ProducerCallback<Boolean>() {

			@Override
			public Boolean doInJms(Session session, MessageProducer producer) throws JMSException {

				System.out.println(Thread.currentThread().getName()+" executing ...");
				try {
					TextMessage message = session.createTextMessage(messageText);
					producer.send(message);
					return true;
				} catch (Exception e) {
					System.out.println("Exception occured : " + e);
					return false;
				}
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
