package com.hospital.model;

import java.time.LocalDateTime;

/**
 * Represents a completed treatment event, pushed onto the
 * {@link com.hospital.structures.TreatmentStack} once a patient's
 * emergency treatment has concluded.
 */
public final class TreatmentRecord {
    private final int patientId;
    private final String patientName;
    private final String treatmentGiven;
    private final LocalDateTime completedAt;

    public TreatmentRecord(int patientId, String patientName, String treatmentGiven, LocalDateTime completedAt) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.treatmentGiven = treatmentGiven;
        this.completedAt = completedAt;
    }

    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getTreatmentGiven() { return treatmentGiven; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    @Override
    public String toString() {
        return String.format("Patient #%d (%s) | Treatment: %s | Completed: %s",
                patientId, patientName, treatmentGiven, completedAt);
    }
}
