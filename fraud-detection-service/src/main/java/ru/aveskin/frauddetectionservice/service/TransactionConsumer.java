package ru.aveskin.frauddetectionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aveskin.frauddetectionservice.dto.TransactionRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {
    private final FraudDetectionService fraudService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topics.out}")
    private String fraudAlertTopic;

    @KafkaListener(topics = "${topics.in}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTransaction(String message) {
        try {
            // Парсим JSON
            ObjectMapper mapper = new ObjectMapper();
            TransactionRequest tx = mapper.readValue(message, TransactionRequest.class);

            boolean fraud = fraudService.isFraudulent(tx);

            if (fraud) {
                log.warn("FRAUD DETECTED! User: {}", tx.id());
                String fraudJson = mapper.writeValueAsString(tx);
                kafkaTemplate.send(fraudAlertTopic, tx.id(), fraudJson);
            } else {
                log.info("Transaction is OK: {}", tx);
            }

        } catch (Exception e) {
            log.error("Ошибка обработки транзакции: {}", e.getMessage());
        }
    }
}
