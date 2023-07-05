#

MainApp.java

```java
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@EnableJms
@SpringBootApplication
public class SendConsumeMessageActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SendConsumeMessageActivemqApplication.class, args);

        ConfigurableApplicationContext context = SpringApplication.run(SendConsumeMessageActivemqApplication.class, args);
        Sender sender = context.getBean(Sender.class);
        sender.sendMessage("order-queue", "Hello from Sender");
    }

    @Bean
    public JmsListenerContainerFactory warehouseFactory(ConnectionFactory connectionFactory,
                                                        DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
        configurer.configure(listenerContainerFactory, connectionFactory);
        return listenerContainerFactory;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin", "admin", "tcp://localhost:61616");
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("1-1");
        return factory;
    }
}
```

----------

Sender.java

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String destination, String message){
        jmsTemplate.convertAndSend(destination, message);
    }
}
```

Receiver

```java
@Component
public class Receiver {

    @JmsListener(destination = "order-queue")
    public void receiveMessage(String order){
        System.out.println("Order Received = " + order);
    }
}
```

<img width="1452" alt="Screenshot 2023-07-05 at 11 58 25 PM" src="https://github.com/javaHelper/Spring-Messaging-with-JMS/assets/54174687/224211be-32f5-4d6f-b125-d9dc09b608fb">

<img width="1205" alt="Screenshot 2023-07-06 at 12 01 21 AM" src="https://github.com/javaHelper/Spring-Messaging-with-JMS/assets/54174687/5f24684b-2492-4f14-b6fc-f0ecfd5b63ff">
