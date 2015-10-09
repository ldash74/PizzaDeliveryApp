package com.epizza.domain;

import com.epizza.types.*;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.ArrayList;
import java.util.List;

/**
 * Pizza class. Encompasses the builder class
 */
@Immutable
public class Pizza {
    private PizzaBaseType base;
    private PizzaCheeseType cheese;
    private PizzaSauceType sauce;
    private PizzaSizeType size;
    private boolean doubleCheese;
    private List<PizzaToppingType> toppings = new ArrayList<PizzaToppingType>();

    //private constructor to enforce object creation through builder
    private Pizza(PizzaBuilder builder) {
        this.base = builder.base;
        this.cheese = builder.cheese;
        this.sauce = builder.sauce;
        this.size = builder.size;
        this.doubleCheese = builder.doubleCheese;
        this.toppings = builder.toppings;
    }

    public PizzaBaseType getBase() {
        return base;
    }

    public PizzaCheeseType getCheese() {
        return cheese;
    }

    public PizzaSauceType getSauce() {
        return sauce;
    }

    public PizzaSizeType getSize() {
        return size;
    }

    public List<PizzaToppingType> getToppings() {
        return toppings;
    }

    private String getFormattedToppingString() {
        StringBuilder aStringBuilder = new StringBuilder();

        int loopCount = 0;

        for (PizzaToppingType aTopping: toppings) {
            aStringBuilder.append(aTopping.getText());
            loopCount++;
            if (loopCount != toppings.size()) {
                aStringBuilder.append(",");
            }
        }

        return aStringBuilder.toString();
    }

    public boolean isDoubleCheese() {
        return doubleCheese;
    }


    public static class PizzaBuilder {
        private PizzaBaseType base;
        private PizzaCheeseType cheese;
        private PizzaSauceType sauce;
        private PizzaSizeType size;
        private boolean doubleCheese;
        private List<PizzaToppingType> toppings = new ArrayList<PizzaToppingType>();

        //builder methods for setting selections
        public PizzaBuilder selectBase(final PizzaBaseType base) {
            this.base = base;
            return this;
        }

        public PizzaBuilder selectCheese(final PizzaCheeseType cheese) {
            this.cheese = cheese;
            return this;
        }

        public PizzaBuilder selectSauce(final PizzaSauceType sauce) {
            this.sauce = sauce;
            return this;
        }

        public PizzaBuilder selectSize(final PizzaSizeType size) {
            this.size = size;
            return this;
        }

        public PizzaBuilder selectToppings(final List<PizzaToppingType> toppings) {
            this.toppings = toppings;
            return this;
        }

        public PizzaBuilder setDoubleCheese(final boolean doubleCheese) {
            this.doubleCheese = doubleCheese;
            return this;
        }

        //return fully build object
        public Pizza build() {
            return new Pizza(this);
        }
    }

    @Override
    public String toString() {
        return getSize().toString() + "|" + getBase().toString() + "|" +  getSauce().toString() + "|" + getFormattedToppingString() + "|" + getCheese().toString() + (doubleCheese ? ", DOUBLE" : "");

    }
}
