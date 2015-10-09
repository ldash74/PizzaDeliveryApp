package com.epizza.service;

import com.epizza.OrderReaders.IInputOrder;
import com.epizza.OrderReaders.InputOrderVO;
import com.epizza.ReceiptWriters.IReceiptWriter;
import com.epizza.domain.ReceiptVO;
import com.epizza.service.impl.PizzaDeliveryServiceImpl;
import com.epizza.types.ReceiptWriterType;
import com.epizza.utils.OrderPriceCalculatorHelper;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class PizzaDeliveryServiceTest {

    OrderPriceCalculatorHelper orderPriceCalculatorHelperMock;
    PizzaDeliveryServiceImpl pizzaDeliveryService;
    IInputOrder inputOrderMock;
    InputOrderVO inputOrderVOMock;
    ReceiptVO receiptVOMock;
    IReceiptWriter receiptWriterMock;

    @Test
    public void pizzaDeliveryServiceImplTest() {

        inputOrderMock = Mockito.mock(IInputOrder.class);
        orderPriceCalculatorHelperMock = Mockito.mock(OrderPriceCalculatorHelper.class);
        inputOrderVOMock = Mockito.mock(InputOrderVO.class);
        receiptVOMock = Mockito.mock(ReceiptVO.class);
        receiptWriterMock = Mockito.mock(IReceiptWriter.class);

        List<IReceiptWriter> receiptWriters = new ArrayList<IReceiptWriter>();
        receiptWriters.add(receiptWriterMock);

        when(inputOrderMock.takeOrder()).thenReturn(inputOrderVOMock);
        when(orderPriceCalculatorHelperMock.calculatePriceAndGenerateReceipt(inputOrderVOMock)).thenReturn(receiptVOMock);
        when(receiptWriterMock.getWriterType()).thenReturn(ReceiptWriterType.CONSOLE);

        pizzaDeliveryService = new PizzaDeliveryServiceImpl(orderPriceCalculatorHelperMock, receiptWriters);
        pizzaDeliveryService.takeOrderAndPrintReceipt(inputOrderMock);

        verify(inputOrderMock).takeOrder();
        verify(orderPriceCalculatorHelperMock).calculatePriceAndGenerateReceipt(inputOrderVOMock);
        verify(receiptWriterMock).writeReceipt(receiptVOMock);

    }
}
