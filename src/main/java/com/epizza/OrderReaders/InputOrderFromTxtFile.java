package com.epizza.OrderReaders;

import com.epizza.domain.Pizza;
import com.epizza.exceptions.OrderTakingException;
import com.epizza.types.*;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Immutable
public class InputOrderFromTxtFile implements IInputOrder {

    private String orderFilePath;

    public InputOrderVO takeOrder() {

        List<Pizza> orderPizzaList = new ArrayList<Pizza>();
        String customerEmail = "";

        InputOrderVO inputOrderVO = null;
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(orderFilePath));

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith("EMAIL:")) {
                    customerEmail = sCurrentLine.substring(sCurrentLine.indexOf(":") + 1);
                } else  {
                    String[] options = sCurrentLine.split("\\|");
                    String[] cheeseFields = options[4].split(",");
                    boolean doubleCheese = cheeseFields.length > 1;

                    //Build the pizza
                    Pizza aPizza = new Pizza.PizzaBuilder()
                            .selectSize(PizzaSizeType.fromString(options[0]))
                            .selectBase(PizzaBaseType.fromString(options[1]))
                            .selectSauce(PizzaSauceType.fromString(options[2]))
                            .selectToppings(getToppings(options[3]))
                            .selectCheese(PizzaCheeseType.fromString(cheeseFields[0]))
                            .setDoubleCheese(doubleCheese).build();

                    orderPizzaList.add(aPizza);
                }
            }

            //Now create the input order object
            inputOrderVO = new InputOrderVO(orderPizzaList, customerEmail);
        } catch (IOException e) {
            throw new OrderTakingException();

        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                throw new OrderTakingException();
            }
        }

        return inputOrderVO;

    }

    private List<PizzaToppingType> getToppings(String commaSeparatedListOfToppings) {
        List<PizzaToppingType> pizzaToppingTypesList = new ArrayList<PizzaToppingType>();

        String[] toppings = commaSeparatedListOfToppings.split(",");
        for (int i=0; i < toppings.length; i++) {
            pizzaToppingTypesList.add(PizzaToppingType.fromString(toppings[i]));
        }

        return pizzaToppingTypesList;
    }

    public InputOrderFromTxtFile(String orderFilePath) {
        this.orderFilePath = orderFilePath;
    }
}
