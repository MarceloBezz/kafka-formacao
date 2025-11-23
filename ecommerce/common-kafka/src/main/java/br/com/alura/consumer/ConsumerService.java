package br.com.alura.consumer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.alura.Message;

public interface ConsumerService<T> {
    void parse(ConsumerRecord<String, Message<T>> record) throws URISyntaxException, IOException, InterruptedException, ExecutionException;
    String getTopic();
    String getConsumerGroup();
}
