
Архитектура
1. API Gateway — точка входа для клиентов.

2. Transaction Service — принимает транзакцию и публикует в Kafka.

3. Fraud Detection Service — многопоточно обрабатывает поток транзакций из Kafka, применяет правила (скоринг, лимиты, гео-проверки).

4. Notification Service — уведомляет о подозрительных транзакциях.

5. Reporting Service — хранит историю и отдаёт отчёты.

6. Kafka Broker — шина событий.

7. Config Server и Eureka — для конфигурации и сервис-дискавери.

8. H2 — как dev-база

Поток данных
1. Клиент отправляет транзакцию → API Gateway → Transaction Service.

2. Transaction Service сохраняет в БД → шлёт событие TransactionCreated в Kafka.

3. Fraud Detection Service параллельно обрабатывает транзакции:

  - проверка суммы (слишком большая — подозрение),

  - проверка частоты операций,

  - проверка по чёрному списку пользователей/карт.

4. Если найдено подозрение → FraudAlert в Kafka.

5. Notification Service шлёт уведомления (email/sms mock).

6. Reporting Service агрегирует данные для аналитики.


<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/e36a337f-9dab-4b26-9077-8ef2294dd434" />


