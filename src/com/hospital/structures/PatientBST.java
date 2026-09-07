package com.hospital.structures;

import com.hospital.exception.DuplicatePatientException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.model.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * PatientBST.java
 * An unbalanced Binary Search Tree keyed on Patient ID (via
 * {@link Patient#compareTo}). Chosen over a balanced tree (AVL/Red-Black)
 * because the assignment brief specifically calls for a plain BST — see
 * the README "Design Decisions" section for a discussion of the
 * worst-case O(n) trade-off this implies for sorted-ID insertion order.
 *
 * Supported operations: insert, search, delete (all 3 classic cases),
 * and in-order traversal for ascending-ID display.
 */
public class PatientBST {

    private static final class Node {
        Patient patient;
        Node left, right;
        Node(Patient patient) { this.patient = patient; }
    }

    private Node root;
    private int size;

    // ---------------- INSERT ----------------
    public void insert(Patient patient) throws DuplicatePatientException {
        root = insertRec(root, patient);
    }

    private Node insertRec(Node node, Patient patient) throws DuplicatePatientException {
        if (node == null) {
            size++;
            return new Node(patient);
        }
        int cmp = patient.compareTo(node.patient);
        if (cmp < 0) {
            node.left = insertRec(node.left, patient);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, patient);
        } else {
            throw new DuplicatePatientException(patient.getPatientId());
        }
        return node;
    }

    // ---------------- SEARCH ----------------
    public Patient search(int patientId) throws PatientNotFoundException {
        Node result = searchRec(root, patientId);
        if (result == null) {
            throw new PatientNotFoundException(patientId);
        }
        return result.patient;
    }

    /** Non-throwing variant, useful for existence checks. */
    public boolean contains(int patientId) {
        return searchRec(root, patientId) != null;
    }

    private Node searchRec(Node node, int patientId) {
        if (node == null) return null;
        if (patientId == node.patient.getPatientId()) return node;
        return (patientId < node.patient.getPatientId())
                ? searchRec(node.left, patientId)
                : searchRec(node.right, patientId);
    }

    // ---------------- DELETE ----------------
    public void delete(int patientId) throws PatientNotFoundException {
        if (!contains(patientId)) {
            throw new PatientNotFoundException(patientId);
        }
        root = deleteRec(root, patientId);
        size--;
    }

    private Node deleteRec(Node node, int patientId) {
        if (node == null) return null;

        if (patientId < node.patient.getPatientId()) {
            node.left = deleteRec(node.left, patientId);
        } else if (patientId > node.patient.getPatientId()) {
            node.right = deleteRec(node.right, patientId);
        } else {
            // Case 1: leaf node
            if (node.left == null && node.right == null) return null;
            // Case 2: single child
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // Case 3: two children -> replace with in-order successor
            Node successor = findMin(node.right);
            node.patient = successor.patient;
            node.right = deleteRec(node.right, successor.patient.getPatientId());
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ---------------- TRAVERSAL ----------------
    /** Returns all patients in ascending Patient ID order — O(n). */
    public List<Patient> inOrder() {
        List<Patient> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(Node node, List<Patient> result) {
        if (node == null) return;
        inOrderRec(node.left, result);
        result.add(node.patient);
        inOrderRec(node.right, result);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
