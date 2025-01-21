package org.example.kafkademo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Foo1 {
    private String foo;

    public Foo1(){

    }
    public Foo1(String foo) {
        this.foo = foo;
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    @Override
    public String toString() {
        return "Foo1{foo='%s}".formatted(foo);
    }
}
