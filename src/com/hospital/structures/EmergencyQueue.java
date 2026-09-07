package com.hospital.structures;

import com.hospital.exception.EmptyStructureException;

/**
 * EmergencyQueue.java
 * A generic, singly-linked-list-backed Queue (FIFO) implementation of
 * {@link QueueADT}. Used to manage patients waiting for emergency
 * treatment. O(1) enqueue and dequeue via head/tail pointers.
 *
 * @param <T> type of element held in the queue (used with {@code Patient})
 */
public class EmergencyQueue<T> implements QueueADT<T> {

    private static final class Node<T> {
        final T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    @Override
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    @Override
    public T dequeue() throws EmptyStructureException {
        if (isEmpty()) {
            throw new EmptyStructureException("EmergencyQueue");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    /** Non-destructive view of every waiting patient, front to rear. */
    public java.util.List<T> snapshot() {
        java.util.List<T> list = new java.util.ArrayList<>();
        Node<T> current = front;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int size() {
        return size;
    }
}
