package br.com.alura;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.alura.consumer.KafkaService;
import br.com.alura.dispatcher.KafkaDispatcher;

public class EmailNewOrderService {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var emailService = new EmailNewOrderService();
        var service = new KafkaService<String>(EmailNewOrderService.class.getSimpleName(),
               "ECOMMERCE_NEW_ORDER",
                emailService::parse,
                Map.of());
        service.run();
    }

    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    void parse(ConsumerRecord<String, Message<String>> record) throws InterruptedException, ExecutionException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, preparing email");
        System.out.println(record.value());
    
        var emailCode = "Thank you for your order! We are processing your order!";
        emailDispatcher.send("ECOMMERCE_SEND_EMAIL",
                record.value().getPayload(),
                emailCode,
                record.value().getId().continueWith(EmailNewOrderService.class.getSimpleName()));
    }

}
