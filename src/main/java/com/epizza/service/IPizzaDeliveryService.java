package com.epizza.service;


import com.epizza.OrderReaders.IInputOrder;

public interface IPizzaDeliveryService {
    void takeOrderAndPrintReceipt(IInputOrder inputOrder);
}
