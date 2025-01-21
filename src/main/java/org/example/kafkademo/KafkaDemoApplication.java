package org.example.kafkademo;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class KafkaDemoApplication {

    private final Logger log = LoggerFactory.getLogger(KafkaDemoApplication.class);
    private final TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApplication.class, args).close();
    }

    @Bean
    public CommonErrorHandler commonErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        return new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(1000L, 2));
    }

    @Bean
    public RecordMessageConverter converter() {
        JsonMessageConverter converter = new JsonMessageConverter();
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        mapper.addTrustedPackages("org.example.kafkademo");
        Map<String,Class<?>> mappings = new HashMap<>();
        mappings.put("foo", Foo2.class);
        mappings.put("bar", Bar2.class);
        mapper.setIdClassMapping(mappings);
        converter.setTypeMapper(mapper);
         return converter;
    }


    @Bean
    public NewTopic foos(){
        return new NewTopic("foos",1,(short) 1);
    }
    @Bean
    public NewTopic bars(){
        return new NewTopic("bars",1,(short) 1);
    }
    @Bean
    @Profile("default")
    public ApplicationRunner runner() {
        return args -> {
            System.out.println("Hi Enter to terminate...");
            System.in.read();
        };
    }
}
