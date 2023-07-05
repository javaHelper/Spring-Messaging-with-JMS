package com.example;

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