package com.epizza.domain;

import com.epizza.types.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class PizzaBuilderTest {

    @Test
    public void buildPizzaTest() {

        Pizza aPizza = new Pizza.PizzaBuilder()
                .selectSize(PizzaSizeType.SMALL)
                .selectBase(PizzaBaseType.PAN)
                .selectSauce(PizzaSauceType.MARGARITA)
                .selectToppings(new ArrayList<PizzaToppingType>(Arrays.asList(PizzaToppingType.CAPSICUM)))
                .selectCheese(PizzaCheeseType.CREAMCHEESE)
                .setDoubleCheese(true).build();

        Assert.assertEquals(PizzaSizeType.SMALL, aPizza.getSize());
        Assert.assertEquals(PizzaBaseType.PAN, aPizza.getBase());
        Assert.assertEquals(PizzaSauceType.MARGARITA, aPizza.getSauce());
        Assert.assertEquals(PizzaToppingType.CAPSICUM, aPizza.getToppings().get(0));
        Assert.assertEquals(PizzaCheeseType.CREAMCHEESE, aPizza.getCheese());
        Assert.assertEquals(true, aPizza.isDoubleCheese());

    }
}