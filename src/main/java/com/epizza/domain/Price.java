package com.epizza.domain;

import java.math.BigDecimal;

public class Price {
    private BigDecimal price = BigDecimal.ZERO;

    public void add(BigDecimal valueToAdd) {
        price = price.add(valueToAdd);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
