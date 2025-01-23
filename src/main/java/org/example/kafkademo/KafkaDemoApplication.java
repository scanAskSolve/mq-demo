package org.example.kafkademo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;

@SpringBootApplication
public class KafkaDemoApplication {

    private final Logger log = LoggerFactory.getLogger(KafkaDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApplication.class, args);
    }

    @RetryableTopic(attempts = "5",backoff = @Backoff(delay = 2000,maxDelay = 10_000,multiplier = 2))
    @KafkaListener(id = "fooGroup", topics = "topic4")
    public void listen(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.OFFSET) long offset) {
        this.log.info("Received in:{}, topic:{}, offset:{}", in, topic, offset);
        if(in.startsWith("fail")){
            throw new RuntimeException();
        }
    }


    @DltHandler
    public void listenDlt(String in,@Header(KafkaHeaders.RECEIVED_TOPIC)String topic,@Header(KafkaHeaders.OFFSET)long offset){
        this.log.info("Received dlt in:{},topic:{},offset:{}",in,topic,offset);
    }
}
