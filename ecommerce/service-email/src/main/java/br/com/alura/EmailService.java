package br.com.alura;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.alura.consumer.ConsumerService;
import br.com.alura.consumer.ServiceRunner;

public class EmailService implements ConsumerService<String> {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new ServiceRunner<>(EmailService::new).start(5);
    }

    public String getConsumerGroup() {
        return EmailService.class.getSimpleName();
    }

    public String getTopic() {
        return "ECOMMERCE_SEND_EMAIL";
    }

    public void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("------------------------------------------");
        System.out.println("SEND EMAIL");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        System.out.println("Email sent!");
    }
}
