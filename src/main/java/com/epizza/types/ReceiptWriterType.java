package com.epizza.types;


public enum ReceiptWriterType {
    CONSOLE("Console"),
    FILE("File");

    private String text;

    ReceiptWriterType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static ReceiptWriterType fromString(String text) {
        if (text != null) {
            for (ReceiptWriterType b : ReceiptWriterType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
