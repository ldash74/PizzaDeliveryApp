package com.epizza.types;


public enum PizzaBaseType {
    NORMAL("NORMAL"),
    PAN("PAN"),
    THIN("THIN"),
    CHEESY("CHEESY");

    private String text;

    PizzaBaseType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static PizzaBaseType fromString(String text) {
        if (text != null) {
            for (PizzaBaseType b : PizzaBaseType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
