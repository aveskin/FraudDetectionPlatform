package ru.aveskin.frauddetectionservice.dto;

public record TransactionRequest(
        String id,
        String accountFrom,
        String accountTo,
        Double amount,
        String currency
) {
}
