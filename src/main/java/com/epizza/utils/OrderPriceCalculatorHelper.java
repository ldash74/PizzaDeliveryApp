package com.epizza.utils;

import com.epizza.OrderReaders.InputOrderVO;
import com.epizza.domain.Pizza;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private final String customerEmailFilePath;

    public OrderPriceCalculatorHelper (final String customerEmailFilePath) {
        this.customerEmailFilePath = customerEmailFilePath;
    }

    public ReceiptVO calculatePriceAndGenerateReceipt(final InputOrderVO inputOrderVO) {

        ReceiptVO receiptVO = new ReceiptVO();
        PizzaSizeType pizzaSize;
        PizzaBaseType pizzaBase;

        BigDecimal totalPrice = BigDecimal.ZERO;

        //Start calculating prices and generate receipt.
        for (Pizza thisPizza: inputOrderVO.getOrderedPizzaList()) {

            BigDecimal pizzaPrice = BigDecimal.ZERO;
            pizzaSize = thisPizza.getSize();
            pizzaBase = thisPizza.getBase();

            //Price for size/base combination
            if (pizzaSize == PizzaSizeType.SMALL) {
                if (pizzaBase == PizzaBaseType.NORMAL) {
                    pizzaPrice = pizzaPrice.add(NORMAL_CRUST_SMALL_PRICE);
                } else if (pizzaBase == PizzaBaseType.PAN) {
                    pizzaPrice = pizzaPrice.add(PAN_CRUST_SMALL_PRICE);
                } else if (pizzaBase == PizzaBaseType.THIN) {
                    pizzaPrice = pizzaPrice.add(THIN_CRUST_SMALL_PRICE);
                } else if (pizzaBase == PizzaBaseType.CHEESY) {
                    pizzaPrice = pizzaPrice.add(CHEESY_BITES_SMALL_PRICE);
                }
            } else if (pizzaSize == PizzaSizeType.MEDIUM) {
                if (pizzaBase == PizzaBaseType.NORMAL) {
                    pizzaPrice = pizzaPrice.add(NORMAL_CRUST_SMALL_PRICE).multiply(MEDIUM_SIZE_PRICE_INCREASE_FACTOR);
                } else if (pizzaBase == PizzaBaseType.PAN) {
                    pizzaPrice = pizzaPrice.add(PAN_CRUST_SMALL_PRICE).multiply(MEDIUM_SIZE_PRICE_INCREASE_FACTOR);
                } else if (pizzaBase == PizzaBaseType.THIN) {
                    pizzaPrice = pizzaPrice.add(THIN_CRUST_SMALL_PRICE).multiply(MEDIUM_SIZE_PRICE_INCREASE_FACTOR);
                } else if (pizzaBase == PizzaBaseType.CHEESY) {
                    pizzaPrice = pizzaPrice.add(CHEESY_BITES_SMALL_PRICE).multiply(MEDIUM_SIZE_PRICE_INCREASE_FACTOR);
                }
            } else if (pizzaSize == PizzaSizeType.LARGE) {
                if (pizzaBase == PizzaBaseType.NORMAL) {
                    pizzaPrice = pizzaPrice.add(NORMAL_CRUST_SMALL_PRICE).multiply(LARGE_SIZE_PRICE_INCREASE_FACTOR);
                } else if (pizzaBase == PizzaBaseType.PAN) {
                    pizzaPrice = pizzaPrice.add(PAN_CRUST_SMALL_PRICE).multiply(LARGE_SIZE_PRICE_INCREASE_FACTOR);
                } else if (pizzaBase == PizzaBaseType.THIN) {
                    pizzaPrice = pizzaPrice.add(THIN_CRUST_SMALL_PRICE).multiply(LARGE_SIZE_PRICE_INCREASE_FACTOR);
                } else if (pizzaBase == PizzaBaseType.CHEESY) {
                    pizzaPrice = pizzaPrice.add(CHEESY_BITES_SMALL_PRICE).multiply(LARGE_SIZE_PRICE_INCREASE_FACTOR);
                }
            }

            //Price for toppings
            int numToppings = thisPizza.getToppings().size();

            if (numToppings > 2) {
                for (int count = 3; count <= numToppings; count++) {
                    if (pizzaSize == PizzaSizeType.SMALL) {
                        pizzaPrice = pizzaPrice.add(EXTRA_TOPPING_PRICE_SMALL);
                    }  else if (pizzaSize == PizzaSizeType.MEDIUM) {
                        pizzaPrice = pizzaPrice.add(EXTRA_TOPPING_PRICE_MEDIUM);
                    } else if (pizzaSize == PizzaSizeType.LARGE) {
                        pizzaPrice = pizzaPrice.add(EXTRA_TOPPING_PRICE_LARGE);
                    }
                }
            }

            //Price for double cheese selection
            if (thisPizza.isDoubleCheese()) {
                if (pizzaSize == PizzaSizeType.SMALL) {
                    pizzaPrice = pizzaPrice.add(DOUBLE_CHEESE_PRICE_SMALL);
                }  else if (pizzaSize == PizzaSizeType.MEDIUM) {
                    pizzaPrice = pizzaPrice.add(DOUBLE_CHEESE_PRICE_MEDIUM);
                } else if (pizzaSize == PizzaSizeType.LARGE) {
                    pizzaPrice = pizzaPrice.add(DOUBLE_CHEESE_PRICE_LARGE);
                }
            }

            //Populate Receipt object
            receiptVO.addReceiptItem(thisPizza.toString(), pizzaPrice);

            //Update total price
            totalPrice = totalPrice.add(pizzaPrice);
        }

        //If email id is provided, add discount if the customer if first time.
        if (StringUtils.isNotEmpty(inputOrderVO.getCustomerEmail()) && ifFirstTimeCustomer(inputOrderVO.getCustomerEmail())) {
            BigDecimal newCustomerDiscountAmount = totalPrice.multiply(NEW_CUSTOMER_DISCOUNT_FACTOR, new MathContext(3, RoundingMode.HALF_UP)).negate();
            //Add item in receipt
            receiptVO.addReceiptItem(NEW_CUSTOMER_DISCOUNT_LABLE, newCustomerDiscountAmount);

            //Update final price
            totalPrice = totalPrice.add(newCustomerDiscountAmount);
        }

        //Put the order total in receipt
        receiptVO.addReceiptItem(ORDER_TOTAL_LABLE, totalPrice);

        //Calculate VAT and add in receipt
        BigDecimal vatAmount = totalPrice.multiply(VAT_FACTOR,  new MathContext(3, RoundingMode.HALF_UP));
        receiptVO.addReceiptItem(VAT_LABLE, vatAmount);

        //Calculate final price and add in receipt
        totalPrice = totalPrice.add(vatAmount);
        receiptVO.addReceiptItem(TOTAL_LABLE, totalPrice);

        return receiptVO;
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
        Writer writer = null;

        try {
            writer = new FileWriter(customerEmailFilePath);
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
