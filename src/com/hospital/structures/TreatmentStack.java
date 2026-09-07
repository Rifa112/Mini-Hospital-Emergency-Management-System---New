package com.hospital.structures;

import com.hospital.exception.EmptyStructureException;
import java.util.Arrays;

/**
 * TreatmentStack.java
 * A generic, array-backed Stack (LIFO) implementation of {@link StackADT}
 * with automatic capacity doubling (amortised O(1) push), used to store
 * completed treatment records.
 *
 * @param <T> type of element held in the stack (used with {@code TreatmentRecord})
 */
public class TreatmentStack<T> implements StackADT<T> {

    private Object[] data;
    private int top; // index of the top element, -1 when empty

    public TreatmentStack() {
        this(16);
    }

    public TreatmentStack(int initialCapacity) {
        data = new Object[Math.max(initialCapacity, 1)];
        top = -1;
    }

    @Override
    public void push(T item) {
        if (top + 1 == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
        data[++top] = item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pop() throws EmptyStructureException {
        if (isEmpty()) {
            throw new EmptyStructureException("TreatmentStack");
        }
        T item = (T) data[top];
        data[top--] = null; // avoid memory leak
        return item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() throws EmptyStructureException {
        if (isEmpty()) {
            throw new EmptyStructureException("TreatmentStack");
        }
        return (T) data[top];
    }

    /** Non-destructive view, most recent (top) first. */
    @SuppressWarnings("unchecked")
    public java.util.List<T> snapshot() {
        java.util.List<T> list = new java.util.ArrayList<>();
        for (int i = top; i >= 0; i--) {
            list.add((T) data[i]);
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    @Override
    public int size() {
        return top + 1;
    }
}
