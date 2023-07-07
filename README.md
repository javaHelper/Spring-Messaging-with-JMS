# Spring-Messaging-with-JMS

Link: `http://localhost:8161/admin`, username/password => admin/admin

```
docker pull rmohr/activemq
docker run -p 61616:61616 -p 8161:8161 rmohr/activemq

docker -it rmohr/activemq bash
```

-------
- Connection configuration using SingleConnectionFactory

```java
@Bean
    public SingleConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");

        SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory(factory);
        singleConnectionFactory.setClientId("MyClientId");
        singleConnectionFactory.setReconnectOnException(true);
        return singleConnectionFactory;
    }
```

---

- Connection configuration using CachingConnectionFactory


```java
@Bean
public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(new ActiveMQConnectionFactory(user, password, brokerUrl));
        factory.setClientId("StoreFront");
        factory.setSessionCacheSize(100);
        return factory;
    }
```
