package br.com.alura;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.alura.consumer.ConsumerService;
import br.com.alura.consumer.ServiceRunner;
import br.com.alura.dispatcher.KafkaDispatcher;

public class EmailNewOrderService implements ConsumerService<Order>{
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new ServiceRunner<>(EmailNewOrderService::new).start(1);
    }

    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, preparing email");
        var message = record.value();
        System.out.println(message);
    
        var order = message.getPayload();
        var emailCode = "Thank you for your order! We are processing your order!";
        emailDispatcher.send("ECOMMERCE_SEND_EMAIL",
                order.getEmail(),
                emailCode,
                record.value().getId().continueWith(EmailNewOrderService.class.getSimpleName()));
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return EmailNewOrderService.class.getSimpleName();
    }

}
