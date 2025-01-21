package org.example.kafkademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

    @PostMapping(path="/send/foo/{what}")
    public void sendFoo(@PathVariable String what) {
        this.kafkaTemplate.send("topic1", new Foo1(what));
    }
}
