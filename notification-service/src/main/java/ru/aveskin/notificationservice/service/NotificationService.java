package ru.aveskin.notificationservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(topics = "${topics.in}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleFraudAlert(String alertJson) {
        // эмуляция отправки
        System.out.println("📩 [Fake Notification] Fraud alert received: " + alertJson);
    }
}
