package com.hospital.exception;

/** Thrown when a Patient ID lookup fails against the {@code PatientBST}. */
public class PatientNotFoundException extends Exception {
    public PatientNotFoundException(int patientId) {
        super("Patient with ID " + patientId + " was not found in the records.");
    }
}
