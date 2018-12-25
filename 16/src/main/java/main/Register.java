package main;

public class Register {
    private int value = 0;

    public Register() {}

    public Register(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
