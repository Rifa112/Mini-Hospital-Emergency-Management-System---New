package com.hospital.model;

import java.time.LocalDate;

/**
 * Represents a single historical hospital visit belonging to a patient.
 * Stored as a node payload inside {@link com.hospital.structures.VisitHistory}
 * (a singly linked list).
 */
public final class Visit {
    private final int visitId;
    private final LocalDate visitDate;
    private final String doctorName;
    private final String diagnosis;
    private final String treatment;

    public Visit(int visitId, LocalDate visitDate, String doctorName, String diagnosis, String treatment) {
        this.visitId = visitId;
        this.visitDate = visitDate;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    public int getVisitId() { return visitId; }
    public LocalDate getVisitDate() { return visitDate; }
    public String getDoctorName() { return doctorName; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }

    @Override
    public String toString() {
        return String.format("Visit #%d | %s | Dr. %s | Diagnosis: %s | Treatment: %s",
                visitId, visitDate, doctorName, diagnosis, treatment);
    }
}
