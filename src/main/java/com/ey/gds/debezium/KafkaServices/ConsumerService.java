package com.ey.gds.debezium.KafkaServices;

//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class ConsumerService {

//    @KafkaListener(topics = "debeziumTopic", groupId = "group_id")
    public void consume(String message){
        System.out.println(String.format("Message received -> %s", message));
    }
}
