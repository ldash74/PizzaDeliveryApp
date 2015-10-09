package com.epizza.types;


public enum PizzaSizeType {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large");

    private String text;

    PizzaSizeType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static PizzaSizeType fromString(String text) {
        if (text != null) {
            for (PizzaSizeType b : PizzaSizeType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
