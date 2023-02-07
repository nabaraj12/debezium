package com.ey.gds.debezium.KafkaServices;

//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
public final class ProducerService {

//    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC = "debeziumTopic" + "";

//    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

//    public void sendMessage(String message) {
//        this.kafkaTemplate.send(TOPIC, message);
//    }
}
