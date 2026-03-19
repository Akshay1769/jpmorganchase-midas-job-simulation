package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final String topic;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {

        String[] lines = transactionLine.split("\\r?\\n"); // handle multiple lines

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] transactionData = line.trim().split(",\\s*");

            kafkaTemplate.send(
                    topic,
                    new Transaction(
                            Long.parseLong(transactionData[0].trim()),
                            Long.parseLong(transactionData[1].trim()),
                            Float.parseFloat(transactionData[2].trim())
                    )
            );
        }
    }
}