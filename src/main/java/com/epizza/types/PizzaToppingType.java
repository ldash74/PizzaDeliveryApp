package com.epizza.types;


public enum PizzaToppingType {

    CAPSICUM("CAPSICUM"),
    ONION("ONION"),
    TOMATO("TOMATO"),
    PINEAPPLE("PINEAPPLE"),
    OLIVE("OLIVE"),
    JALAPENO("JALAPENO");

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
