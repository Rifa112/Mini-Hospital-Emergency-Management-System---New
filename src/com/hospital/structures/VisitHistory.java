package com.hospital.structures;

import com.hospital.model.Visit;
import java.util.Optional;

/**
 * VisitHistory.java
 * A Singly Linked List holding one patient's past hospital visits in
 * chronological (insertion) order. Encapsulated behind add/remove/search/
 * display so callers never touch node internals directly.
 */
public class VisitHistory {

    private static final class Node {
        final Visit visit;
        Node next;
        Node(Visit visit) { this.visit = visit; }
    }

    private Node head;
    private int size;

    /** Appends a new visit to the end of the history — O(n). */
    public void addVisit(Visit visit) {
        Node newNode = new Node(visit);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /** Removes the visit with the given ID. Returns true if a node was removed. */
    public boolean removeVisit(int visitId) {
        if (head == null) return false;

        if (head.visit.getVisitId() == visitId) {
            head = head.next;
            size--;
            return true;
        }
        Node current = head;
        while (current.next != null) {
            if (current.next.visit.getVisitId() == visitId) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Searches for a visit by ID, returning it wrapped in an Optional. */
    public Optional<Visit> searchVisit(int visitId) {
        Node current = head;
        while (current != null) {
            if (current.visit.getVisitId() == visitId) {
                return Optional.of(current.visit);
            }
            current = current.next;
        }
        return Optional.empty();
    }

    /** Non-destructive view of all visits, oldest first. */
    public java.util.List<Visit> snapshot() {
        java.util.List<Visit> list = new java.util.ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.visit);
            current = current.next;
        }
        return list;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }
}
