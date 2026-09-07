package com.hospital.model;

import com.hospital.structures.VisitHistory;

/**
 * Represents a hospital patient. Implements {@link Comparable} on
 * patientId so it can be used naturally as a BST key.
 * Each Patient owns exactly one {@link VisitHistory} (singly linked list)
 * capturing their past visits.
 */
public class Patient implements Comparable<Patient> {

    private final int patientId;
    private String name;
    private int age;
    private String contactNumber;
    private String medicalCondition;
    private Priority priority;
    private final VisitHistory visitHistory;

    public Patient(int patientId, String name, int age, String contactNumber,
                   String medicalCondition, Priority priority) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
        this.medicalCondition = medicalCondition;
        this.priority = priority;
        this.visitHistory = new VisitHistory();
    }

    public int getPatientId() { return patientId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContactNumber() { return contactNumber; }
    public String getMedicalCondition() { return medicalCondition; }
    public Priority getPriority() { return priority; }
    public VisitHistory getVisitHistory() { return visitHistory; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }
    public void setPriority(Priority priority) { this.priority = priority; }

    @Override
    public int compareTo(Patient other) {
        return Integer.compare(this.patientId, other.patientId);
    }

    @Override
    public String toString() {
        return String.format("#%-5d %-15s Age:%-3d Tel:%-12s Condition:%-15s Priority:%s",
                patientId, name, age, contactNumber, medicalCondition, priority);
    }
}
