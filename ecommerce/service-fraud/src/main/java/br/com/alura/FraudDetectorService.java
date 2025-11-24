package br.com.alura;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.alura.consumer.ConsumerService;
import br.com.alura.consumer.ServiceRunner;
import br.com.alura.dispatcher.KafkaDispatcher;

public class FraudDetectorService implements ConsumerService<Order> {
    private LocalDatabase database;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new ServiceRunner<>(FraudDetectorService::new).start(1);
    }

    public FraudDetectorService() throws SQLException {
        this.database = new LocalDatabase("frauds_database");
        this.database.createIfNotExists("""
                create table if not exists Orders (
                uuid varchar(200) primary key,
                is_fraud boolean
                )""");
    }

    private final KafkaDispatcher<Order> ordeDispatcher = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException, SQLException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.partition());
        System.out.println(record.offset());

        var message = record.value();
        var order = message.getPayload();
        if (wasProcessed(order)) {
            System.out.println("Order" + order.getOrderId() + " was already processed");
            return;
        }

        if (isFraud(order)) {
            database.update("insert into Orders (uuid, is_fraud) values (?,true)", order.getOrderId());
            System.out.println("Order is a fraud!!!!!!!\n" + order);
            ordeDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getEmail(), order,
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()));
        } else {
            database.update("insert into Orders (uuid, is_fraud) values (?,false)", order.getOrderId());
            System.out.println("Approved: " + order);
            ordeDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), order,
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()));
        }

        System.out.println("Order processed!");
    }

    private boolean wasProcessed(Order order) throws SQLException {
        var results = database.query("select uuid from Orders where uuid = ? limit 1", order.getOrderId());
        return results.next();
    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return FraudDetectorService.class.getSimpleName();
    }
}
