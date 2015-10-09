package com.epizza.OrderReaders;

/**
 * Interface class that all Input Order sources should inherit from.
 */
public interface IInputOrder {
    InputOrderVO takeOrder();
}
