# Response Management Using JMS Response

First hot the NEW endpoint, then UPDATE and then DELETE

- http://localhost:8080/process/store/1/order/1/1/1/NEW/
- http://localhost:8080/process/store/1/order/1/1/1/UPDATE/
- http://localhost:8080/process/store/1/order/1/1/1/DELETE/

Goto http://0.0.0.0:8161/ - username/password = admin/admin

You can see `book.order.canceled.queue` and `book.order.processed.queue`

<img width="1181" alt="Screenshot 2023-07-07 at 7 16 16 PM" src="https://github.com/javaHelper/Spring-Messaging-with-JMS/assets/54174687/5a5f5d05-efde-47ca-acec-9fbd47645bf8">

------
Clicked on the `book.order.processed.queue` and you should be able to see message like below

<img width="1197" alt="Screenshot 2023-07-08 at 1 11 43 AM" src="https://github.com/javaHelper/Spring-Messaging-with-JMS/assets/54174687/14de8ef2-0b04-4b21-b11f-1f9716dadfc0">

Then click on the first message and you should see all the details

<img width="1094" alt="Screenshot 2023-07-07 at 7 16 39 PM" src="https://github.com/javaHelper/Spring-Messaging-with-JMS/assets/54174687/61a42f02-6712-4685-8982-214ff1288354">

----------

Then clicked on the `book.order.canceled.queue` and you should be able to see something like below - 

<img width="1149" alt="Screenshot 2023-07-07 at 7 16 27 PM" src="https://github.com/javaHelper/Spring-Messaging-with-JMS/assets/54174687/cf2a0f11-6117-4fbc-ac9f-b3da89ea12ab">



- JmsConfig.java



````java
package com.example.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JmsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsConfig.class);

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(
                new ActiveMQConnectionFactory(user, password, brokerUrl)
        );
        factory.setClientId("StoreFront");
        factory.setSessionCacheSize(100);
        return factory;
    }


    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setTransactionManager(jmsTransactionManager());
        factory.setErrorHandler(t -> {
            LOGGER.info("Handling error in listening for messages, error: " + t.getMessage());
        });
        return factory;
    }

    @Bean
    public PlatformTransactionManager jmsTransactionManager(){
        return new JmsTransactionManager(connectionFactory());
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }
}
````

BookOrderService

````java
@Component
public class BookOrderService {

    private static final String BOOK_QUEUE = "book.order.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    public void send(BookOrder bookOrder, String storeId, String orderState){
        jmsTemplate.convertAndSend(BOOK_QUEUE, bookOrder, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("bookOrderId", bookOrder.getBookOrderId());
                message.setStringProperty("storeId", storeId);
                message.setStringProperty("orderState", orderState);
                return message;
            }
        });
    }
}
````

- WarehouseProcessingService.java

````java
@Configuration
public class WarehouseProcessingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseProcessingService.class);

    private static final String PROCESSED_QUEUE = "book.order.processed.queue";
    private static final String CANCELED_QUEUE = "book.order.canceled.queue";


    @Transactional
    public JmsResponse<Message<ProcessedBookOrder>> processOrder(BookOrder bookOrder, String orderState, String storeId) {

        Message<ProcessedBookOrder> message;

        if ("NEW".equalsIgnoreCase(orderState)) {
            message = add(bookOrder, storeId);
            return JmsResponse.forQueue(message, PROCESSED_QUEUE);
        } else if ("UPDATE".equalsIgnoreCase(orderState)) {
            message = update(bookOrder, storeId);
            return JmsResponse.forQueue(message, PROCESSED_QUEUE);
        } else if ("DELETE".equalsIgnoreCase(orderState)) {
            message = delete(bookOrder, storeId);
            return JmsResponse.forQueue(message, CANCELED_QUEUE);
        } else {
            throw new IllegalArgumentException("WarehouseProcessingService.processOrder(...) - orderState does not match expected criteria!");
        }

    }

    private Message<ProcessedBookOrder> add(BookOrder bookOrder, String storeId) {
        LOGGER.info("ADDING A NEW ORDER TO DB");
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        ), "ADDED", storeId);
    }

    private Message<ProcessedBookOrder> update(BookOrder bookOrder, String storeId) {
        LOGGER.info("UPDATING A ORDER TO DB");
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        ), "UPDATED", storeId);
    }

    private Message<ProcessedBookOrder> delete(BookOrder bookOrder, String storeId) {
        LOGGER.info("DELETING ORDER FROM DB");
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                null
        ), "DELETED", storeId);
    }

    private Message<ProcessedBookOrder> build(ProcessedBookOrder bookOrder, String orderState, String storeId) {
        return MessageBuilder
                .withPayload(bookOrder)
                .setHeader("orderState", orderState)
                .setHeader("storeId", storeId)
                .build();
    }
}
````
