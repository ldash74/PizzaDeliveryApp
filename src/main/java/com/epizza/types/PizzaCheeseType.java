package com.epizza.types;


public enum PizzaCheeseType {

    MOZZARELLA("Mozzarella"),
    CREAMCHEESE("Cream");

    private String text;

    PizzaCheeseType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static PizzaCheeseType fromString(String text) {
        if (text != null) {
            for (PizzaCheeseType b : PizzaCheeseType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
