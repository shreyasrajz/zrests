package com.example.zrests;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    @KafkaListener(topics = "zrests",groupId = "king")
    public void consume(Request1 request) {
        System.out.println(request.toString());
    }
}
