package com.epizza.ReceiptWriters;


import com.epizza.domain.ReceiptVO;
import com.epizza.exceptions.FileIOException;
import com.epizza.types.ReceiptWriterType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class TextFileReceiptWriter implements IReceiptWriter {

    private String receiptFileName;

    public TextFileReceiptWriter(String receiptFileName) {
        this.receiptFileName = receiptFileName;
    }

    public void writeReceipt(ReceiptVO receiptVO) {
        Writer writer = null;

        try {
            writer = new FileWriter(receiptFileName);

            for (Map.Entry receiptEntry: receiptVO.getReceiptItems().entrySet()) {
                writer.write(receiptEntry.getKey() + "-----: " + receiptEntry.getValue());
                writer.write(System.getProperty("line.separator"));
            }

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

    public ReceiptWriterType getWriterType() {
        return ReceiptWriterType.FILE;
    }
}
