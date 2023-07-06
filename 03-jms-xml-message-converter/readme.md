#

```
curl --location 'http://localhost:8080/book' \
--header 'Content-Type: application/xml' \
--data '<?xml version="1.0" encoding="UTF-8" ?>
<root>
    <bookOrderId>2</bookOrderId>
    <book>
        <bookId>2</bookId>
        <title>The Chava by Shivaji Sawant</title>
    </book>
    <customer>
        <customerId>2</customerId>
        <fullName>Harshita Dekate</fullName>
    </customer>
</root>'
```

<img width="1447" alt="Screenshot 2023-07-06 at 11 24 17 AM" src="https://github.com/javaHelper/Spring-Messaging-with-JMS/assets/54174687/b949f85d-9811-45ff-bd2d-5a322b610795">
