package org.example.kafkademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
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
        this.kafkaTemplate.send("foos", new Foo1(what));
    }
    @PostMapping(path = "/send/bar/{what}")
    public void sendBar(@PathVariable String what) {
        this.kafkaTemplate.send("bars", new Bar1(what));
    }
    @PostMapping(path = "/send/unknown/{what}")
    public void sendUnknown(@PathVariable String what) {
        this.kafkaTemplate.send("bars", what);
    }
}
