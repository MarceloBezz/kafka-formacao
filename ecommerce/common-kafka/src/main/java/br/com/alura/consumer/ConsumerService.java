package br.com.alura.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.alura.Message;

public interface ConsumerService<T> {
    void parse(ConsumerRecord<String, Message<T>> record) throws Exception;
    String getTopic();
    String getConsumerGroup();
}
