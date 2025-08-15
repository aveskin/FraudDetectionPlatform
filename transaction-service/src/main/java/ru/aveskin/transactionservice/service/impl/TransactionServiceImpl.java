package ru.aveskin.transactionservice.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aveskin.transactionservice.dto.TransactionRequest;
import ru.aveskin.transactionservice.service.TransactionService;

import java.util.concurrent.TimeUnit;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final String TOPIC = "transactions";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public TransactionServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendTransaction(TransactionRequest transaction) throws Exception {
        String transactionJson = objectMapper.writeValueAsString(transaction);
        kafkaTemplate.send(TOPIC, transaction.id(), transactionJson)
                .get(5, TimeUnit.SECONDS);
    }
}
