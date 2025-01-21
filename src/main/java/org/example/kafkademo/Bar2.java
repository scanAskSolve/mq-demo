package org.example.kafkademo;

public class Bar2 {
    public String bar;

    public Bar2 (){}
    public Bar2(String bar) {
        this.bar = bar;
    }

    public String getBar() {
        return bar;
    }

    @Override
    public String toString() {
        return "Bar2{bar='%s}".formatted(bar);
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
}
