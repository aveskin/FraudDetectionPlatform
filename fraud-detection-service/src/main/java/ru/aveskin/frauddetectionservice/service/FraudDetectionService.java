package ru.aveskin.frauddetectionservice.service;

import ru.aveskin.frauddetectionservice.dto.TransactionRequest;

public interface FraudDetectionService {
    boolean isFraudulent(TransactionRequest tx);
}
