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
import org.springframework.util.backoff.FixedBackOff;

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
        return new JsonMessageConverter();
    }

    @KafkaListener(id = "fooGroup", topics = "topic1")
    public void listen(Foo2 foo) {
        log.info("Received: {}", foo);
        if (foo.getFoo().startsWith("fail")) {
            throw new RuntimeException("failed");
        }
        this.taskExecutor.execute(() -> System.out.println("Hit enter to terminate"));
    }

    @KafkaListener(id="dltGroup",topics = "topic1-dlt")
    public void dltListen(byte[] in){
        log.info("Received from DLT : {}", new String(in));
        this.taskExecutor.execute(() -> System.out.println("Hit enter to terminate"));
    }

    @Bean
    public NewTopic topic(){
        return new NewTopic("topic1",1,(short) 1);
    }
    @Bean
    public NewTopic dlt(){
        return new NewTopic("topic1-dlt",1,(short) 1);
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
