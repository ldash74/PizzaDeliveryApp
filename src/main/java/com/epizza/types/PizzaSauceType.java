package com.epizza.types;


public enum PizzaSauceType {
    MARGARITA("Margarita"),
    MEXICAN("Mexican Salsa");

    private String text;

    PizzaSauceType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static PizzaSauceType fromString(String text) {
        if (text != null) {
            for (PizzaSauceType b : PizzaSauceType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
