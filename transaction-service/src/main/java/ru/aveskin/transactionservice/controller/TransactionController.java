package ru.aveskin.transactionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.aveskin.transactionservice.model.Transaction;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private static final String TOPIC = "transactions";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public String sendTransaction(@RequestBody Transaction transaction) {
        // Можно сериализовать в JSON вручную или через библиотеку
        // Для простоты — преобразуем в строку (например, JSON)
        String transactionStr = transaction.toString(); // тут лучше использовать JSON!

        kafkaTemplate.send(TOPIC, transaction.getId(), transactionStr);

        return "Transaction sent to Kafka";
    }

    @GetMapping("/test")
    public String test() {
        return "done";
    }
}