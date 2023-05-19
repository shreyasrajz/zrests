package com.example.zrests.kafka;

import com.example.zrests.model.Restaurant;
import com.example.zrests.requests.Request2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private KafkaTemplate<String, Restaurant> kafkaTemplate;

    public Producer(KafkaTemplate<String, Restaurant> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendmessage(Request2 data) {
        LOGGER.info(String.format("Sending this data to kafka -------> %s",data.toString()));
        Message<Request2> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC,"zrests")
                .build();
        kafkaTemplate.send(message);
    }
}
