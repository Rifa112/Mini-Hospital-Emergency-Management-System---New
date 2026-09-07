package com.hospital.exception;

/** Thrown when attempting to insert a Patient ID that already exists in the BST. */
public class DuplicatePatientException extends Exception {
    public DuplicatePatientException(int patientId) {
        super("Patient with ID " + patientId + " already exists. Duplicate insert rejected.");
    }
}
