package org.example.kafkademo;

public class Foo2 {
    private String foo;

    public Foo2(){}
    public Foo2(String foo) {
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
        return "Foo2{foo='%s}".formatted(foo);
    }
}

