package com.example.config;

import com.example.model.Book;
import com.example.model.BookOrder;
import com.example.model.Customer;
import com.thoughtworks.xstream.MarshallingStrategy;
import com.thoughtworks.xstream.security.ExplicitTypePermission;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
public class JmsConfig {

    @Bean
    public MessageConverter xmlMarshallingMessageConverter() {
        MarshallingMessageConverter converter = new MarshallingMessageConverter(xmlMarshaller());
        converter.setTargetType(MessageType.TEXT);
        return converter;
    }

    // https://stackoverflow.com/questions/30812293/com-thoughtworks-xstream-security-forbiddenclassexception
    // https://stackoverflow.com/questions/72719398/caused-by-com-thoughtworks-xstream-security-forbiddenclassexception-spring-b
    @Bean
    public XStreamMarshaller xmlMarshaller() {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.getXStream().allowTypes(new Class[]{Book.class, Customer.class, BookOrder.class});
        marshaller.setSupportedClasses(Book.class, Customer.class, BookOrder.class);
        return marshaller;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
    }


    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(xmlMarshallingMessageConverter());
        return factory;
    }
}