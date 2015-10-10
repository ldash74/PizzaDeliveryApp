package com.epizza.utils;

import com.epizza.OrderReaders.InputOrderFromTxtFile;
import com.epizza.OrderReaders.InputOrderVO;
import com.epizza.domain.ReceiptVO;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

public class OrderPriceCalculatorHelperTest {

    @Test
    public void caculateOrderPriceFirstTimeCustomerSuccessTest() {
        String customerEmailFilePath = "/Users/debjanidas/resources/CustomerEmails.csv";
        String orderFilePath = "/Users/debjanidas/resources/CustomerOrder_1.txt";

        InputOrderFromTxtFile inputOrderFromTxtFile = new InputOrderFromTxtFile(orderFilePath);
        InputOrderVO inputOrderVO = inputOrderFromTxtFile.takeOrder();

        OrderPriceCalculatorHelper orderPriceCalculatorHelper = new OrderPriceCalculatorHelper(customerEmailFilePath);

        ReceiptVO receiptVO = orderPriceCalculatorHelper.calculatePriceAndGenerateReceipt(inputOrderVO);

        Map<String, BigDecimal> receiptItems = receiptVO.getReceiptItems();

        Assert.assertEquals(6, receiptItems.size());
        Assert.assertEquals(new BigDecimal("6.0"), receiptItems.get(inputOrderVO.getOrderedPizzaList().get(0).toString()));
        Assert.assertEquals(new BigDecimal("10.50"), receiptItems.get(inputOrderVO.getOrderedPizzaList().get(1).toString()));
        Assert.assertEquals(new BigDecimal("-1.65"), receiptItems.get(OrderPriceCalculatorHelper.NEW_CUSTOMER_DISCOUNT_LABLE));
        Assert.assertEquals(new BigDecimal("14.85"), receiptItems.get(OrderPriceCalculatorHelper.ORDER_TOTAL_LABLE));
        Assert.assertEquals(new BigDecimal("2.97"), receiptItems.get(OrderPriceCalculatorHelper.VAT_LABLE));
        Assert.assertEquals(new BigDecimal("17.82"), receiptItems.get(OrderPriceCalculatorHelper.TOTAL_LABLE));

    }

    @Test
    public void caculateOrderPriceNoEmailProvidedSuccessTest() {
        String customerEmailFilePath = "/Users/debjanidas/resources/CustomerEmails.csv";
        String orderFilePath = "/Users/debjanidas/resources/CustomerOrder_2.txt";

        InputOrderFromTxtFile inputOrderFromTxtFile = new InputOrderFromTxtFile(orderFilePath);
        InputOrderVO inputOrderVO = inputOrderFromTxtFile.takeOrder();

        OrderPriceCalculatorHelper orderPriceCalculatorHelper = new OrderPriceCalculatorHelper(customerEmailFilePath);

        ReceiptVO receiptVO = orderPriceCalculatorHelper.calculatePriceAndGenerateReceipt(inputOrderVO);

        Map<String, BigDecimal> receiptItems = receiptVO.getReceiptItems();


        Assert.assertEquals(5, receiptItems.size());
        Assert.assertEquals(new BigDecimal("6.0"), receiptItems.get(inputOrderVO.getOrderedPizzaList().get(0).toString()));
        Assert.assertEquals(new BigDecimal("10.50"), receiptItems.get(inputOrderVO.getOrderedPizzaList().get(1).toString()));
        Assert.assertEquals(new BigDecimal("16.50"), receiptItems.get(OrderPriceCalculatorHelper.ORDER_TOTAL_LABLE));
        Assert.assertEquals(new BigDecimal("3.30"), receiptItems.get(OrderPriceCalculatorHelper.VAT_LABLE));
        Assert.assertEquals(new BigDecimal("19.80"), receiptItems.get(OrderPriceCalculatorHelper.TOTAL_LABLE));

    }

    @Test
    public void caculateOrderPriceExistingCustomerSuccessTest() {
        String customerEmailFilePath = "/Users/debjanidas/resources/CustomerEmails.csv";
        String orderFilePath = "/Users/debjanidas/resources/CustomerOrder_3.txt";

        InputOrderFromTxtFile inputOrderFromTxtFile = new InputOrderFromTxtFile(orderFilePath);
        InputOrderVO inputOrderVO = inputOrderFromTxtFile.takeOrder();

        OrderPriceCalculatorHelper orderPriceCalculatorHelper = new OrderPriceCalculatorHelper(customerEmailFilePath);

        ReceiptVO receiptVO = orderPriceCalculatorHelper.calculatePriceAndGenerateReceipt(inputOrderVO);

        Map<String, BigDecimal> receiptItems = receiptVO.getReceiptItems();

        Assert.assertEquals(5, receiptItems.size());
        Assert.assertEquals(new BigDecimal("6.0"), receiptItems.get(inputOrderVO.getOrderedPizzaList().get(0).toString()));
        Assert.assertEquals(new BigDecimal("10.50"), receiptItems.get(inputOrderVO.getOrderedPizzaList().get(1).toString()));
        Assert.assertEquals(new BigDecimal("16.50"), receiptItems.get(OrderPriceCalculatorHelper.ORDER_TOTAL_LABLE));
        Assert.assertEquals(new BigDecimal("3.30"), receiptItems.get(OrderPriceCalculatorHelper.VAT_LABLE));
        Assert.assertEquals(new BigDecimal("19.80"), receiptItems.get(OrderPriceCalculatorHelper.TOTAL_LABLE));

    }
}
