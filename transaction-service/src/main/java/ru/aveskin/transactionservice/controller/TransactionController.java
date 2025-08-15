package ru.aveskin.transactionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aveskin.transactionservice.dto.TransactionRequest;
import ru.aveskin.transactionservice.dto.TransactionResponse;
import ru.aveskin.transactionservice.service.TransactionService;

import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;


    @PostMapping
    public ResponseEntity<TransactionResponse> sendTransaction(@RequestBody TransactionRequest transaction) {
        try {
            transactionService.sendTransaction(transaction);
            return ResponseEntity.ok(new TransactionResponse("SUCCESS", "Transaction sent to Kafka"));
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body(new TransactionResponse("ERROR", "Timeout while sending to Kafka"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TransactionResponse("ERROR", "Failed to send transaction: " + e.getMessage()));
        }
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}