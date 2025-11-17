package br.com.alura;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {
    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        var service = new KafkaService<Order>(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudService::parse,
                Order.class,
                Map.of());
        service.run();
    }

    private final KafkaDispatcher<Order> ordeDispatcher = new KafkaDispatcher<>();

    void parse(ConsumerRecord<String, Order> record) throws InterruptedException, ExecutionException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        // System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        var order = record.value();
        if (isFraud(order)) {
            System.out.println("Order is a fraud!!!!!!!\n" + order);
            ordeDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getUserId(), order);
        } else {
            System.out.println("Approved: " + order);
            ordeDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getUserId(), order);
        }

        System.out.println("Order processed!");
    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}
