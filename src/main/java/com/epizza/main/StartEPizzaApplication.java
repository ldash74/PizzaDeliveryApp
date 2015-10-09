package com.epizza.main;


import com.epizza.OrderReaders.IInputOrder;
import com.epizza.OrderReaders.InputOrderFromTxtFile;
import com.epizza.ReceiptWriters.ConsoleReceiptWriter;
import com.epizza.ReceiptWriters.IReceiptWriter;
import com.epizza.ReceiptWriters.TextFileReceiptWriter;
import com.epizza.service.IPizzaDeliveryService;
import com.epizza.service.impl.PizzaDeliveryServiceImpl;
import com.epizza.utils.OrderPriceCalculatorHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class StartEPizzaApplication {

    private static String customerEmailFilePath;
    private static String receiptFileName;
    private static String orderFilePath;

    private IPizzaDeliveryService pizzaDeliveryService;

    private static Log logger = LogFactory.getLog(StartEPizzaApplication.class);

    public static void main(String args[]) {


        if (args.length != 3 ||
                StringUtils.isEmpty(args[0]) ||
                StringUtils.isEmpty(args[1]) ||
                StringUtils.isEmpty(args[2])) {

            logger.error("Usage: StartEPizzaApplication.main -D<Customer Email file path> -D <Order File Path> -D <Receipt file path>");
            System.exit(0);

        }

        customerEmailFilePath = args[0];
        orderFilePath = args[1];
        receiptFileName = args[2];

        StartEPizzaApplication thisApp = new StartEPizzaApplication();
        thisApp.takeOrderAndPrintReceipt();
    }

    public StartEPizzaApplication() {
        //Perform initialisations

        /**
         * Usually we will have these dependencies injected through a framework
         * such as spring. We are doing it manually here as we have been suggested to
         * avoid using any library in this exercise.
         */

        //Initialise OrderPriceCalculatorHelper class
        OrderPriceCalculatorHelper orderPriceCalculatorHelper = new OrderPriceCalculatorHelper(customerEmailFilePath);

        //Create list of writers.
        List<IReceiptWriter> writers = new ArrayList<IReceiptWriter>();
        TextFileReceiptWriter textFileReceiptWriter = new TextFileReceiptWriter(receiptFileName);

        writers.add(new ConsoleReceiptWriter());
        writers.add(textFileReceiptWriter);

        //Initialise Service
        pizzaDeliveryService = new PizzaDeliveryServiceImpl(orderPriceCalculatorHelper, writers);
    }

    private void takeOrderAndPrintReceipt() {
        try {
            IInputOrder inputOrder = new InputOrderFromTxtFile(orderFilePath);
            pizzaDeliveryService.takeOrderAndPrintReceipt(inputOrder);
        } catch (Exception e) {
            logger.error("Error while processing orders ", e);
        }

    }
}
