package se.hernebring.tollfeecalculator;

public class Vehicle {

    private final Type type;
    private double tones = 3.5;

    public Vehicle(Type type) {
        this.type = type;
    }

    public void setTones(double tones) {
        this.tones = tones;
    }

    boolean isToll() {
        return type == Type.CAR | type == Type.TRUCK | (type == Type.BUS && tones < 14);
    }

    boolean isTollFree() {
        return !isToll();
    }
}
