package com.hospital.structures;

import com.hospital.exception.EmptyStructureException;

/**
 * Minimal FIFO Queue abstract data type contract.
 * Declaring this as an interface (rather than hard-coding EmergencyQueue
 * everywhere) means the underlying implementation could later be swapped
 * (e.g. array-based vs linked-list-based) without touching client code.
 *
 * @param <T> the type of element stored in the queue
 */
public interface QueueADT<T> {
    void enqueue(T item);
    T dequeue() throws EmptyStructureException;
    boolean isEmpty();
    int size();
}
