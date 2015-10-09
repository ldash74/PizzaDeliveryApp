package com.epizza.ReceiptWriters;

import com.epizza.domain.ReceiptVO;
import com.epizza.types.ReceiptWriterType;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ConsoleReceiptWriterTest {

    @Test
    public void writeReceiptSuccessTest() {

        ReceiptVO receiptVO = new ReceiptVO();

        receiptVO.addReceiptItem("Item1", new BigDecimal("10.5"));
        receiptVO.addReceiptItem("Item2", new BigDecimal("20.5"));

        ConsoleReceiptWriter consoleReceiptWriter = new ConsoleReceiptWriter();

        consoleReceiptWriter.writeReceipt(receiptVO);

        Assert.assertEquals(ReceiptWriterType.CONSOLE, consoleReceiptWriter.getWriterType());

    }
}
