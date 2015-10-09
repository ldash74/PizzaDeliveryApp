package com.epizza.ReceiptWriters;


import com.epizza.domain.ReceiptVO;
import com.epizza.types.ReceiptWriterType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class ConsoleReceiptWriter implements IReceiptWriter {

    private static Log logger = LogFactory.getLog(ConsoleReceiptWriter.class);

    public void writeReceipt(ReceiptVO receiptVO) {
        for (Map.Entry receiptEntry: receiptVO.getReceiptItems().entrySet()) {
            logger.info(receiptEntry.getKey() + "-----: " + receiptEntry.getValue());
        }

    }

    public ReceiptWriterType getWriterType() {
        return ReceiptWriterType.CONSOLE;
    }
}
