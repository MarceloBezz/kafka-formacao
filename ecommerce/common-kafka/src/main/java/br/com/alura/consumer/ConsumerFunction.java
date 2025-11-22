package br.com.alura.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.alura.Message;

public interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, Message<T>> record) throws Exception;
}
