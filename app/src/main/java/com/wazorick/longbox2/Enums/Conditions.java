package com.wazorick.longbox2.Enums;

public enum Conditions {
    NEAR_MINT ("Near Mint"),
    VERY_FINE ("Very Fine"),
    FINE ("Fine"),
    VERY_GOOD ("Very Good"),
    GOOD ("Good"),
    FAIR ("Fair"),
    POOR ("Poor");

    private final String name;

    private Conditions(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
