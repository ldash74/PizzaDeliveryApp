package com.epizza.types;


public enum PizzaToppingType {

    CAPSICUM("Capsicum"),
    ONION("Onion"),
    TOMATO("Tomato"),
    PINEAPPLE("Pineapple"),
    OLIVE("Olive"),
    JALAPENO("Jalapeno");

    private String text;

    PizzaToppingType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static PizzaToppingType fromString(String text) {
        if (text != null) {
            for (PizzaToppingType b : PizzaToppingType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
