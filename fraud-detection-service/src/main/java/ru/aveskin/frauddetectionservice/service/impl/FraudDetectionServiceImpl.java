package ru.aveskin.frauddetectionservice.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.aveskin.frauddetectionservice.dto.TransactionRequest;
import ru.aveskin.frauddetectionservice.service.FraudDetectionService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {
    // Хранилище времени транзакций по пользователям
    private final Map<String, List<Long>> userTransactions = new ConcurrentHashMap<>();

    //черный список
    private final Set<String> blackList = new HashSet<>();

    // Параметры проверки частоты

    @Value("${fraud.max-tx-per-minute:3}")
    private static int MAX_TX_PER_MINUTE; // больше 3 транзакций за минуту — подозрительно

    @Value("${fraud.window-ms:60000}")
    private static long WINDOW_MS;  // 1 минута

    @Value("${fraud.max-amount:10000}")
    private static long MAX_AMOUNT;   // максимальная сумма перевода

    public FraudDetectionServiceImpl() {
        blackList.add("1");
        blackList.add("10");
        blackList.add("101");
    }

    @Override
    public boolean isFraudulent(TransactionRequest tx) {
        return isFrequentRepetition(tx)
                || isAmountExceeded(tx)
                || isIdFromBlackList(tx);
    }

    private boolean isFrequentRepetition(TransactionRequest tx) {
        long now = System.currentTimeMillis();
        userTransactions.putIfAbsent(tx.id(), new CopyOnWriteArrayList<>());
        List<Long> timestamps = userTransactions.get(tx.id());

        // Добавляем новую транзакцию
        timestamps.add(now);

        // Удаляем старые транзакции за пределами окна
        timestamps.removeIf(ts -> ts < now - WINDOW_MS);

        // Проверяем количество за последнюю минуту
        return timestamps.size() > MAX_TX_PER_MINUTE;
    }

    private boolean isAmountExceeded(TransactionRequest tx) {
        return (tx.amount() > MAX_AMOUNT);
    }

    private boolean isIdFromBlackList(TransactionRequest tx) {
        //TODO сходить в БД, проверить id на blacklist
        //пока для простоты, несколько id: 1, 10, 101
        return (blackList.contains(tx.id()));
    }


}
