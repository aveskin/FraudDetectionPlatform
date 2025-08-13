package ru.aveskin.transactionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String id;
    private String accountFrom;
    private String accountTo;
    private Double amount;
    private String currency;
}