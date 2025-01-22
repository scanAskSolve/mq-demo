package org.example.kafkademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @PostMapping(path = "/send/foos/{what}")
    public void sendFoo(@PathVariable String what) {
        this.kafkaTemplate.executeInTransaction(kafkaTemplate -> {
            StringUtils.commaDelimitedListToSet(what).stream()
                    .map(s -> new Foo1(s))
                    .forEach(foo1 -> kafkaTemplate.send("topic2", foo1));
            return null;
        });
    }
}
