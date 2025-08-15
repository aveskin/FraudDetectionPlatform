package ru.aveskin.transactionservice.service;

import ru.aveskin.transactionservice.dto.TransactionRequest;

public interface TransactionService {
    void sendTransaction(TransactionRequest transaction) throws Exception;
}
