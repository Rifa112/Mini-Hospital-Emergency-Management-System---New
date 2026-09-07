package com.hospital.structures;

import com.hospital.exception.EmptyStructureException;

/**
 * Minimal LIFO Stack abstract data type contract.
 *
 * @param <T> the type of element stored in the stack
 */
public interface StackADT<T> {
    void push(T item);
    T pop() throws EmptyStructureException;
    T peek() throws EmptyStructureException;
    boolean isEmpty();
    int size();
}
