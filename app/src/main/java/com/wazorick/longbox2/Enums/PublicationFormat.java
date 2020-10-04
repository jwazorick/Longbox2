package com.example.longbox2.Enums;

public enum PublicationFormat {
    DIGITAL ("Digital"),
    SINGLE_ISSUE ("Single Issue"),
    HARDCOVER ("Hardcover"),
    SOFTCOVER ("Softcover");

    //private final String name;
    private final String display;

    private PublicationFormat(String s) {
        //name = s;
        display = s;
    }

    public boolean equalsName(String otherName) {
        return display.equals(otherName);
        //return name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.display;
        //return this.name;
    }
}
