package com.epizza.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ReceiptVO {

    private Map<String, BigDecimal> receiptMap;

    public ReceiptVO() {
        receiptMap = new LinkedHashMap<String, BigDecimal>();
    }

    public void addReceiptItem(String itemTitle, BigDecimal amount) {
        receiptMap.put(itemTitle, amount);
    }

    public Map<String, BigDecimal> getReceiptItems() {
        return receiptMap;
    }

}
