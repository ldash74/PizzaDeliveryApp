package com.epizza.service.impl;

import com.epizza.OrderReaders.IInputOrder;
import com.epizza.OrderReaders.InputOrderVO;
import com.epizza.ReceiptWriters.IReceiptWriter;
import com.epizza.domain.*;
import com.epizza.service.IPizzaDeliveryService;
import com.epizza.utils.OrderPriceCalculatorHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class PizzaDeliveryServiceImpl implements IPizzaDeliveryService {

    private List<IReceiptWriter> writeReceiptList;

    private static Log logger = LogFactory.getLog(PizzaDeliveryServiceImpl.class);

    private OrderPriceCalculatorHelper orderPriceCalculatorHelper;

    public PizzaDeliveryServiceImpl(final OrderPriceCalculatorHelper orderPriceCalculatorHelper, final List<IReceiptWriter> writeReceiptList) {
        this.orderPriceCalculatorHelper = orderPriceCalculatorHelper;
        this.writeReceiptList = writeReceiptList;
    }

    public void takeOrderAndPrintReceipt(IInputOrder inputOrder) {

        //Take Order
        logger.info("Taking Order");
        InputOrderVO inputOrderVO = inputOrder.takeOrder();

        //Calculate order prices and generate receipt
        logger.info("Calculating Price");
        ReceiptVO receiptVO = orderPriceCalculatorHelper.calculatePriceAndGenerateReceipt(inputOrderVO);

        //Write receipt for each receipt generation channel
        for (IReceiptWriter aWwriteReceipt: writeReceiptList) {
            logger.info("Generating Receipt for " + aWwriteReceipt.getWriterType().getText());
            aWwriteReceipt.writeReceipt(receiptVO);
        }

    }
}
