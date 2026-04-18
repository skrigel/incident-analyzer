package com.example.incidentplatform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Custom Kafka serializer using Jackson ObjectMapper.
 * Replacement for deprecated Spring Kafka JsonSerializer in 4.0+.
 */
public class JacksonKafkaSerializer implements Serializer<Object> {

    private final ObjectMapper objectMapper;

    public JacksonKafkaSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        if (data == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }
}