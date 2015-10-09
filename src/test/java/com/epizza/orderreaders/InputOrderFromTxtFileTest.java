package com.epizza.orderreaders;


import com.epizza.OrderReaders.InputOrderFromTxtFile;
import com.epizza.OrderReaders.InputOrderVO;
import com.epizza.exceptions.OrderTakingException;
import com.epizza.types.PizzaBaseType;
import com.epizza.types.PizzaCheeseType;
import com.epizza.types.PizzaSauceType;
import com.epizza.types.PizzaSizeType;
import org.junit.Assert;
import org.junit.Test;


public class InputOrderFromTxtFileTest {

    @Test
    public void readOrderFromFileSuccess() {

        String orderFilePath = "/resources/CustomerOrder_1.txt";

        InputOrderFromTxtFile inputOrderFromTxtFile = new InputOrderFromTxtFile(orderFilePath);
        InputOrderVO inputOrderVO = inputOrderFromTxtFile.takeOrder();

        Assert.assertEquals("email@example.com", inputOrderVO.getCustomerEmail());
        Assert.assertEquals(2, inputOrderVO.getOrderedPizzaList().size());

        Assert.assertEquals(PizzaSizeType.SMALL, inputOrderVO.getOrderedPizzaList().get(0).getSize());
        Assert.assertEquals(PizzaBaseType.NORMAL, inputOrderVO.getOrderedPizzaList().get(0).getBase());
        Assert.assertEquals(PizzaSauceType.MARGARITA, inputOrderVO.getOrderedPizzaList().get(0).getSauce());
        Assert.assertEquals("[CAPSICUM, ONION, OLIVE]", inputOrderVO.getOrderedPizzaList().get(0).getToppings().toString());
        Assert.assertEquals(PizzaCheeseType.MOZZARELLA, inputOrderVO.getOrderedPizzaList().get(0).getCheese());
        Assert.assertEquals(false, inputOrderVO.getOrderedPizzaList().get(0).isDoubleCheese());

        Assert.assertEquals(PizzaSizeType.LARGE, inputOrderVO.getOrderedPizzaList().get(1).getSize());
        Assert.assertEquals(PizzaBaseType.PAN, inputOrderVO.getOrderedPizzaList().get(1).getBase());
        Assert.assertEquals(PizzaSauceType.MARGARITA, inputOrderVO.getOrderedPizzaList().get(1).getSauce());
        Assert.assertEquals("[ONION, PINEAPPLE]", inputOrderVO.getOrderedPizzaList().get(1).getToppings().toString());
        Assert.assertEquals(PizzaCheeseType.CREAMCHEESE, inputOrderVO.getOrderedPizzaList().get(1).getCheese());
        Assert.assertEquals(true, inputOrderVO.getOrderedPizzaList().get(1).isDoubleCheese());

    }

    @Test (expected = OrderTakingException.class)
    public void tryReadingFromNonExistantFile() {
        String invalidFilePath = "invalidPath";

        InputOrderFromTxtFile inputOrderFromTxtFile = new InputOrderFromTxtFile(invalidFilePath);
        inputOrderFromTxtFile.takeOrder();
    }

}
