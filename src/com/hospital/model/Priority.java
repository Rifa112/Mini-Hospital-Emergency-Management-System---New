package com.hospital.model;

/**
 * Represents the clinical urgency of a patient's condition.
 * Used purely as descriptive metadata on a Patient record; the queue
 * itself still behaves as strict FIFO as required by the assignment
 * spec, but this enum demonstrates how priority *could* be layered on
 * top of a plain queue in a real triage system (see README "Design
 * Decisions" section).
 */
public enum Priority {
    CRITICAL("Critical - immediate attention"),
    URGENT("Urgent - attend within 30 minutes"),
    STANDARD("Standard - routine queue order");

    private final String description;

    Priority(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
