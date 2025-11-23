package br.com.alura;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.alura.consumer.ConsumerService;
import br.com.alura.consumer.ServiceRunner;

public class ReadingReport implements ConsumerService<User>{

    public static void main(String[] args) {
        new ServiceRunner<>(ReadingReport::new).start(5);
    }

    public void parse(ConsumerRecord<String, Message<User>> record) throws URISyntaxException, IOException {
        Path SOURCE = Paths.get(ReadingReport.class.getClassLoader().getResource("report.txt").toURI());
        System.out.println("------------------------------------------");
        System.out.println("Processing report for" + record.value());

        var user = record.value().getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());

        System.out.println("File created: " + target.getAbsolutePath());
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_USER_GENERATE_READING_REPORT";
    }

    @Override
    public String getConsumerGroup() {
        return ReadingReport.class.getSimpleName();
    }
}
