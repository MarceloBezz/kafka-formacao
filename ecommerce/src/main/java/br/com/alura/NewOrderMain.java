package br.com.alura;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var dispatcher = new KafkaDispatcher();        
        for (int i = 0; i < 11; i++) {
            var key = UUID.randomUUID().toString();
            var value = key + "132123,23566,9458305";
            dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);
        } 
    }
}
