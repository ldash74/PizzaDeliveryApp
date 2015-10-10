package com.epizza.utils;

import com.epizza.OrderReaders.InputOrderVO;
import com.epizza.domain.Pizza;
import com.epizza.domain.Price;
import com.epizza.domain.ReceiptVO;
import com.epizza.exceptions.FileIOException;
import com.epizza.exceptions.PriceCalculationException;
import com.epizza.types.PizzaBaseType;
import com.epizza.types.PizzaSizeType;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class OrderPriceCalculatorHelper {

    private static final BigDecimal NORMAL_CRUST_SMALL_PRICE = new BigDecimal("5.0");
    private static final BigDecimal PAN_CRUST_SMALL_PRICE = new BigDecimal("6.0");
    private static final BigDecimal THIN_CRUST_SMALL_PRICE = new BigDecimal("6.0");
    private static final BigDecimal CHEESY_BITES_SMALL_PRICE = new BigDecimal("7.0");

    private static final BigDecimal MEDIUM_SIZE_PRICE_INCREASE_FACTOR = new BigDecimal("1.25");
    private static final BigDecimal LARGE_SIZE_PRICE_INCREASE_FACTOR = new BigDecimal("1.5");

    private static final BigDecimal EXTRA_TOPPING_PRICE_SMALL = new BigDecimal("1.0");
    private static final BigDecimal EXTRA_TOPPING_PRICE_MEDIUM = new BigDecimal("1.5");
    private static final BigDecimal EXTRA_TOPPING_PRICE_LARGE = new BigDecimal("2.0");

    private static final BigDecimal DOUBLE_CHEESE_PRICE_SMALL = new BigDecimal("0.0");
    private static final BigDecimal DOUBLE_CHEESE_PRICE_MEDIUM = new BigDecimal("1.0");
    private static final BigDecimal DOUBLE_CHEESE_PRICE_LARGE = new BigDecimal("1.5");

    private static final BigDecimal NEW_CUSTOMER_DISCOUNT_FACTOR = new BigDecimal("0.1");
    private static final BigDecimal VAT_FACTOR = new BigDecimal("0.2");

    public static final String NEW_CUSTOMER_DISCOUNT_LABLE = "New customer discount (10%)";
    public static final String VAT_LABLE= "VAT (20%)";
    public static final String ORDER_TOTAL_LABLE = "Order Total";
    public static final String TOTAL_LABLE= "Total";

    private static Map<String, BigDecimal> baseSizePriceMap = new HashMap<String, BigDecimal>();
    private static Map<PizzaSizeType, BigDecimal> toppingPriceMap = new HashMap<PizzaSizeType, BigDecimal>();
    private static Map<PizzaSizeType, BigDecimal> doubleCheesePriceMap = new HashMap<PizzaSizeType, BigDecimal>();

    private final String customerEmailFilePath;


    public OrderPriceCalculatorHelper (final String customerEmailFilePath) {
        this.customerEmailFilePath = customerEmailFilePath;
    }

    static {
        //Base Size Price Map
        baseSizePriceMap.put(PizzaSizeType.SMALL.getText() + PizzaBaseType.NORMAL.getText(), NORMAL_CRUST_SMALL_PRICE);
        baseSizePriceMap.put(PizzaSizeType.SMALL.getText() + PizzaBaseType.PAN.getText(), PAN_CRUST_SMALL_PRICE);
        baseSizePriceMap.put(PizzaSizeType.SMALL.getText() + PizzaBaseType.THIN.getText(), THIN_CRUST_SMALL_PRICE);
        baseSizePriceMap.put(PizzaSizeType.SMALL.getText() + PizzaBaseType.CHEESY.getText(), CHEESY_BITES_SMALL_PRICE);

        baseSizePriceMap.put(PizzaSizeType.MEDIUM.getText() + PizzaBaseType.NORMAL.getText(), NORMAL_CRUST_SMALL_PRICE.multiply(MEDIUM_SIZE_PRICE_INCREASE_FACTOR));
        baseSizePriceMap.put(PizzaSizeType.MEDIUM.getText() + PizzaBaseType.PAN.getText(), PAN_CRUST_SMALL_PRICE.multiply(MEDIUM_SIZE_PRICE_INCREASE_FACTOR));
        baseSizePriceMap.put(PizzaSizeType.MEDIUM.getText() + PizzaBaseType.THIN.getText(), THIN_CRUST_SMALL_PRICE.multiply(MEDIUM_SIZE_PRICE_INCREASE_FACTOR));
        baseSizePriceMap.put(PizzaSizeType.MEDIUM.getText() + PizzaBaseType.CHEESY.getText(), CHEESY_BITES_SMALL_PRICE.multiply(MEDIUM_SIZE_PRICE_INCREASE_FACTOR));

        baseSizePriceMap.put(PizzaSizeType.LARGE.getText() + PizzaBaseType.NORMAL.getText(), NORMAL_CRUST_SMALL_PRICE.multiply(LARGE_SIZE_PRICE_INCREASE_FACTOR));
        baseSizePriceMap.put(PizzaSizeType.LARGE.getText() + PizzaBaseType.PAN.getText(), PAN_CRUST_SMALL_PRICE.multiply(LARGE_SIZE_PRICE_INCREASE_FACTOR));
        baseSizePriceMap.put(PizzaSizeType.LARGE.getText() + PizzaBaseType.THIN.getText(), THIN_CRUST_SMALL_PRICE.multiply(LARGE_SIZE_PRICE_INCREASE_FACTOR));
        baseSizePriceMap.put(PizzaSizeType.LARGE.getText() + PizzaBaseType.CHEESY.getText(), CHEESY_BITES_SMALL_PRICE.multiply(LARGE_SIZE_PRICE_INCREASE_FACTOR));

        //Topping Price Map
        toppingPriceMap.put(PizzaSizeType.SMALL, EXTRA_TOPPING_PRICE_SMALL);
        toppingPriceMap.put(PizzaSizeType.MEDIUM, EXTRA_TOPPING_PRICE_MEDIUM);
        toppingPriceMap.put(PizzaSizeType.LARGE, EXTRA_TOPPING_PRICE_LARGE);

        //Double cheese price map
        doubleCheesePriceMap.put(PizzaSizeType.SMALL, DOUBLE_CHEESE_PRICE_SMALL);
        doubleCheesePriceMap.put(PizzaSizeType.MEDIUM, DOUBLE_CHEESE_PRICE_MEDIUM);
        doubleCheesePriceMap.put(PizzaSizeType.LARGE, DOUBLE_CHEESE_PRICE_LARGE);
    }

    public ReceiptVO calculatePriceAndGenerateReceipt(final InputOrderVO inputOrderVO) {

        ReceiptVO receiptVO = new ReceiptVO();
        PizzaSizeType pizzaSize;
        PizzaBaseType pizzaBase;

        //BigDecimal totalPrice = BigDecimal.ZERO;
        Price totalPrice = new Price();

        //Calculating prices and generate receipt.
        for (Pizza thisPizza: inputOrderVO.getOrderedPizzaList()) {

            //BigDecimal pizzaPrice = BigDecimal.ZERO;
            Price pizzaPrice = new Price();

            pizzaSize = thisPizza.getSize();
            pizzaBase = thisPizza.getBase();

            //Price for size/base combination
            pizzaPrice.add(baseSizePriceMap.get(pizzaSize.getText() + pizzaBase.getText()));

            //Price for toppings
            pizzaPrice.add(getPriceForToppings(thisPizza).getPrice());

            //Price for double cheese selection
            if (thisPizza.isDoubleCheese()) {
                pizzaPrice.add(doubleCheesePriceMap.get(pizzaSize));
            }

            //Populate Receipt object
            receiptVO.addReceiptItem(thisPizza.toString(), pizzaPrice.getPrice());

            //Update total price
            totalPrice.add(pizzaPrice.getPrice());
        }

        //If email id is provided, add discount if the customer is first time.
        if (StringUtils.isNotEmpty(inputOrderVO.getCustomerEmail()) && ifFirstTimeCustomer(inputOrderVO.getCustomerEmail())) {
            BigDecimal newCustomerDiscountAmount = totalPrice.getPrice().multiply(NEW_CUSTOMER_DISCOUNT_FACTOR, new MathContext(3, RoundingMode.HALF_UP));
            newCustomerDiscountAmount = newCustomerDiscountAmount.negate();

            //Add item in receipt
            receiptVO.addReceiptItem(NEW_CUSTOMER_DISCOUNT_LABLE, newCustomerDiscountAmount);

            //Update final price
            totalPrice.add(newCustomerDiscountAmount);
        }

        //Put the order total in receipt
        receiptVO.addReceiptItem(ORDER_TOTAL_LABLE, totalPrice.getPrice());

        //Calculate VAT and add in receipt
        BigDecimal vatAmount = totalPrice.getPrice().multiply(VAT_FACTOR, new MathContext(3, RoundingMode.HALF_UP));
        receiptVO.addReceiptItem(VAT_LABLE, vatAmount);

        //Calculate final price and add in receipt
        totalPrice.add(vatAmount);
        receiptVO.addReceiptItem(TOTAL_LABLE, totalPrice.getPrice());

        return receiptVO;
    }

    private Price getPriceForToppings(Pizza aPizza) {
        int numToppings = aPizza.getToppings().size();
        PizzaSizeType pizzaSize = aPizza.getSize();
        Price toppingPrice = new Price();

        if (numToppings > 2) {
            for (int count = 3; count <= numToppings; count++) {
                toppingPrice.add(toppingPriceMap.get(pizzaSize));
            }
        }

        return toppingPrice;

    }

    private boolean ifFirstTimeCustomer(final String customerEmail) {

        List<String> existingCustomerEmailList = new ArrayList<String>();
        boolean isFirstTimeCustomer = false;


        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(customerEmailFilePath));

            while ((sCurrentLine = br.readLine()) != null) {
                String[] customerEmails = sCurrentLine.split(",");
                existingCustomerEmailList.addAll(new ArrayList<String>(Arrays.asList(customerEmails)));
            }

            isFirstTimeCustomer = !existingCustomerEmailList.contains(customerEmail);

        } catch (IOException e) {
            throw new PriceCalculationException();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                throw new PriceCalculationException();
            }
        }

        if (isFirstTimeCustomer) {
            existingCustomerEmailList.add(customerEmail);
            updateCustomerListFile(existingCustomerEmailList);
        }

        return isFirstTimeCustomer;
    }

    private void updateCustomerListFile(List<String>  customerEmailList) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(customerEmailFilePath));
            StringBuilder aStringBuilder = new StringBuilder();

            int loopCount = 1;
            for (String eMail: customerEmailList) {
                aStringBuilder.append(eMail);

                if (loopCount != customerEmailList.size()) {
                    aStringBuilder.append(",");
                    loopCount++;
                }
            }

            writer.write(aStringBuilder.toString());

        } catch (IOException e) {
            throw new FileIOException();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    throw new FileIOException();
                }
            }
        }
    }

}
