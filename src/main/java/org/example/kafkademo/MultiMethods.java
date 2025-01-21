package org.example.kafkademo;


import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "multiGroup", topics = {"foos", "bars"})
public class MultiMethods {
    private final TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

    @KafkaHandler
    public void foo(Foo2 foo2) {
        System.out.println("Received Foo2 " + foo2);
        terminateMessage();
    }

    @KafkaHandler
    public void bar(Bar2 bar2) {
        System.out.println("Received Bar2 " + bar2);
        terminateMessage();
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Received unknown " + object);
        terminateMessage();
    }

    private void terminateMessage() {
        this.taskExecutor.execute(() -> System.out.println("Hit Enter to terminate"));
    }
}
