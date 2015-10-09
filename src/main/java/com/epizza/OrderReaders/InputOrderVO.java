package com.epizza.OrderReaders;

import com.epizza.domain.Pizza;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.List;

@Immutable
public class InputOrderVO {
    private final List<Pizza> orderedPizzaList;
    private final String customerEmail;

    public InputOrderVO(final List<Pizza> orderedPizzaList, final String customerEmail) {
        this.orderedPizzaList = orderedPizzaList;
        this.customerEmail = customerEmail;
    }


    public List<Pizza> getOrderedPizzaList() {
        return orderedPizzaList;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
