package com.vnc.otp.config;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String servers;

    @Value("${kafka.ssl.truststore-location}")
    private String trustStoreLocation;

    @Value("${kafka.ssl.truststore-password}")
    private String trustStorePassword;

    @Value("${kafka.ssl.keystore-location}")
    private String keyStoreLocation;

    @Value("${kafka.ssl.keystore-password}")
    private String keyStorePassword;

    @Value("${kafka.ssl.key-password}")
    private String keyPassword;

    @Value("${kafka.producer.group-id}")
     private String groupId;

     @Bean
     public ProducerFactory<String, String> producerFactory() {
         Map<String, Object> configProps = new HashMap<>();
          configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
          configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
         configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
         configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
         return new DefaultKafkaProducerFactory<>(configProps);
     }
     //wraps a Producer instance and provides convenience methods for sending messages to Kafka topics.
     @Bean
     public KafkaTemplate<String, String> kafkaTemplate() {
         return new KafkaTemplate<>(producerFactory());
     }
}
