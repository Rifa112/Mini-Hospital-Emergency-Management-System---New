package com.hospital.exception;

/**
 * Thrown when a destructive read (dequeue/pop) is attempted on an
 * empty {@code EmergencyQueue} or {@code TreatmentStack}.
 */
public class EmptyStructureException extends Exception {
    public EmptyStructureException(String structureName) {
        super(structureName + " is empty. Operation could not be completed.");
    }
}
