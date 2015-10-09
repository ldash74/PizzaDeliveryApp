package com.epizza.ReceiptWriters;

import com.epizza.domain.ReceiptVO;
import com.epizza.types.ReceiptWriterType;

public interface IReceiptWriter {

    void writeReceipt(ReceiptVO receiptVO);
    ReceiptWriterType getWriterType();
}
