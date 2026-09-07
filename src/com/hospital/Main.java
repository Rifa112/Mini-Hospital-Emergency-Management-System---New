package com.hospital;

import com.hospital.exception.DuplicatePatientException;
import com.hospital.exception.EmptyStructureException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.model.Patient;
import com.hospital.model.Priority;
import com.hospital.model.TreatmentRecord;
import com.hospital.model.Visit;
import com.hospital.structures.EmergencyQueue;
import com.hospital.structures.PatientBST;
import com.hospital.structures.TreatmentStack;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main.java
 * Entry point and console controller for the Mini Hospital
 * Emergency Management System with box-style banners and tables.
 */
public class Main {

    private static final int MENU_WIDTH = 63;
    private static final String MENU_BORDER = "+" + "-".repeat(MENU_WIDTH - 2) + "+";
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final PatientBST patientBST = new PatientBST();
    private final EmergencyQueue<Patient> emergencyQueue = new EmergencyQueue<>();
    private final TreatmentStack<TreatmentRecord> treatmentStack = new TreatmentStack<>();
    private final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        loadSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            System.out.println();

            try {
                switch (choice) {
                    case 1 -> registerPatient();
                    case 2 -> searchPatient();
                    case 3 -> deletePatient();
                    case 4 -> displayAllPatients();
                    case 5 -> addToQueue();
                    case 6 -> treatNextPatient();
                    case 7 -> displayQueue();
                    case 8 -> displayTreatmentHistory();
                    case 9 -> undoLastTreatment();
                    case 10 -> addVisit();
                    case 11 -> viewVisitHistory();
                    case 12 -> removeVisit();
                    case 13 -> searchVisit();
                    case 0 -> {
                        running = false;
                        printBanner("SHUTTING DOWN. GOODBYE!");
                    }
                    default -> System.out.println("[!] Invalid option, please choose from the menu.");
                }
            } catch (DuplicatePatientException | PatientNotFoundException | EmptyStructureException ex) {
                System.out.println("[!] ERROR: " + ex.getMessage());
            }
            System.out.println();
        }
        sc.close();
    }

    private void printMenu() {
        System.out.println(MENU_BORDER);
        printMenuCentered("MINI HOSPITAL EMERGENCY MANAGEMENT SYSTEM");
        System.out.println(MENU_BORDER);
        printMenuLine("MENU");
        System.out.println(MENU_BORDER);
        printMenuLine("PATIENT RECORDS (BST)");
        printMenuLine("  1. Register New Patient");
        printMenuLine("  2. Search Patient by ID");
        printMenuLine("  3. Delete Patient");
        printMenuLine("  4. Display All Patients (sorted by ID)");
        System.out.println(MENU_BORDER);
        printMenuLine("EMERGENCY QUEUE");
        printMenuLine("  5. Add to Queue");
        printMenuLine("  6. Treat Next Patient (Dequeue)");
        printMenuLine("  7. View Waiting Queue");
        System.out.println(MENU_BORDER);
        printMenuLine("TREATMENT HISTORY (STACK)");
        printMenuLine("  8. View Treatment History");
        printMenuLine("  9. Undo Last Treatment (Pop)");
        System.out.println(MENU_BORDER);
        printMenuLine("PATIENT VISIT HISTORY (LINKED LIST)");
        printMenuLine(" 10. Add Visit Record");
        printMenuLine(" 11. View Visit History");
        printMenuLine(" 12. Remove Visit Record");
        printMenuLine(" 13. Search Visit Record");
        System.out.println(MENU_BORDER);
        printMenuLine("  0. Exit");
        System.out.println(MENU_BORDER);
    }

    private void printMenuLine(String text) {
        int pad = (MENU_WIDTH - 2) - 2 - text.length();
        if (pad < 0) pad = 0;
        System.out.println("| " + text + " ".repeat(pad) + " |");
    }

    private void printMenuCentered(String text) {
        int totalPad = (MENU_WIDTH - 2) - text.length();
        if (totalPad < 0) totalPad = 0;
        int leftPad = totalPad / 2;
        int rightPad = totalPad - leftPad;
        System.out.println("|" + " ".repeat(leftPad) + text + " ".repeat(rightPad) + "|");
    }

    private static void printBanner(String title) {
        String border = "+" + "-".repeat(MENU_WIDTH - 2) + "+";
        int totalPad = (MENU_WIDTH - 2) - title.length();
        if (totalPad < 0) totalPad = 0;
        int leftPad = totalPad / 2;
        int rightPad = totalPad - leftPad;
        System.out.println(border);
        System.out.println("|" + " ".repeat(leftPad) + title + " ".repeat(rightPad) + "|");
        System.out.println(border);
    }

    // ---------------- BST ----------------
    private void registerPatient() throws DuplicatePatientException {
        printBanner("REGISTER NEW PATIENT");
        int id = readInt("Enter Patient ID: ");
        String name = readLine("Enter Name: ");
        int age = readInt("Enter Age: ");
        String contact = readLine("Enter Contact Number: ");
        String condition = readLine("Enter Medical Condition: ");
        Priority priority = readPriority();

        Patient p = new Patient(id, name, age, contact, condition, priority);
        patientBST.insert(p);
        System.out.println("\n[OK] Patient registered successfully:");
        printPatientTable(List.of(p));
    }

    private void searchPatient() throws PatientNotFoundException {
        printBanner("SEARCH PATIENT");
        int id = readInt("Enter Patient ID to search: ");
        Patient p = patientBST.search(id);
        System.out.println();
        printPatientTable(List.of(p));
    }

    private void deletePatient() throws PatientNotFoundException {
        printBanner("DELETE PATIENT");
        int id = readInt("Enter Patient ID to delete: ");
        Patient p = patientBST.search(id);
        patientBST.delete(id);
        System.out.println("\n[OK] Patient deleted from records:");
        printPatientTable(List.of(p));
    }

    private void displayAllPatients() {
        printBanner("ALL PATIENTS (SORTED BY ID)");
        List<Patient> patients = patientBST.inOrder();
        System.out.println();
        if (patients.isEmpty()) {
            System.out.println("No patient records available.");
        } else {
            printPatientTable(patients);
            System.out.println("Total Registered Patients: " + patients.size());
        }
    }

    // ---------------- QUEUE ----------------
    private void addToQueue() throws PatientNotFoundException {
        printBanner("ADD TO EMERGENCY QUEUE");
        int id = readInt("Enter Patient ID to add to emergency queue: ");
        Patient patient = patientBST.search(id);
        emergencyQueue.enqueue(patient);
        System.out.println("\n[OK] Patient added to emergency queue:");
        printPatientTable(List.of(patient));
    }

    private void treatNextPatient() throws EmptyStructureException {
        printBanner("TREAT NEXT PATIENT (DEQUEUE)");
        Patient next = emergencyQueue.dequeue();
        System.out.println("Processing patient:");
        printPatientTable(List.of(next));
        String treatmentGiven = readLine("Enter Treatment Given: ");
        TreatmentRecord record = new TreatmentRecord(next.getPatientId(), next.getName(), treatmentGiven, LocalDateTime.now());
        treatmentStack.push(record);
        System.out.println("\n[OK] Treatment recorded successfully into Treatment History.");
    }

    private void displayQueue() {
        printBanner("EMERGENCY WAITING QUEUE");
        List<Patient> waiting = emergencyQueue.snapshot();
        if (waiting.isEmpty()) {
            System.out.println("No patients currently waiting in emergency queue.");
        } else {
            printQueueTable(waiting);
            System.out.println("Total Patients in Queue: " + waiting.size());
        }
    }

    // ---------------- STACK ----------------
    private void displayTreatmentHistory() {
        printBanner("TREATMENT HISTORY (STACK)");
        List<TreatmentRecord> history = treatmentStack.snapshot();
        if (history.isEmpty()) {
            System.out.println("No treatment records available.");
        } else {
            printTreatmentTable(history);
            System.out.println("Total Treatment Records: " + history.size());
        }
    }

    private void undoLastTreatment() throws EmptyStructureException {
        printBanner("UNDO LAST TREATMENT (POP)");
        TreatmentRecord removed = treatmentStack.pop();
        System.out.println("[OK] Removed most recent treatment record:");
        printTreatmentTable(List.of(removed));
    }

    // ---------------- LINKED LIST ----------------
    private void addVisit() throws PatientNotFoundException {
        printBanner("ADD VISIT RECORD");
        int id = readInt("Enter Patient ID: ");
        Patient patient = patientBST.search(id);

        int visitId = readInt("Enter Visit ID: ");
        LocalDate date = LocalDate.parse(readLine("Enter Visit Date (YYYY-MM-DD): "));
        String doctor = readLine("Enter Doctor Name: ");
        String diagnosis = readLine("Enter Diagnosis: ");
        String treatment = readLine("Enter Treatment: ");

        Visit v = new Visit(visitId, date, doctor, diagnosis, treatment);
        patient.getVisitHistory().addVisit(v);
        System.out.println("\n[OK] Visit record added for Patient #" + id + " (" + patient.getName() + "):");
        printVisitTable(List.of(v));
    }

    private void viewVisitHistory() throws PatientNotFoundException {
        printBanner("PATIENT VISIT HISTORY");
        int id = readInt("Enter Patient ID: ");
        Patient patient = patientBST.search(id);
        List<Visit> visits = patient.getVisitHistory().snapshot();
        System.out.println("Patient: " + patient.getName() + " (ID: #" + patient.getPatientId() + ")");
        if (visits.isEmpty()) {
            System.out.println("No previous visits recorded for this patient.");
        } else {
            printVisitTable(visits);
            System.out.println("Total Visits: " + visits.size());
        }
    }

    private void removeVisit() throws PatientNotFoundException {
        printBanner("REMOVE VISIT RECORD");
        int id = readInt("Enter Patient ID: ");
        Patient patient = patientBST.search(id);
        int visitId = readInt("Enter Visit ID to remove: ");
        boolean removed = patient.getVisitHistory().removeVisit(visitId);
        if (removed) {
            System.out.println("[OK] Visit #" + visitId + " removed successfully from patient's history.");
        } else {
            System.out.println("[!] Visit #" + visitId + " not found for Patient #" + id + ".");
        }
    }

    private void searchVisit() throws PatientNotFoundException {
        printBanner("SEARCH VISIT RECORD");
        int id = readInt("Enter Patient ID: ");
        Patient patient = patientBST.search(id);
        int visitId = readInt("Enter Visit ID to search: ");
        Optional<Visit> visit = patient.getVisitHistory().searchVisit(visitId);
        if (visit.isPresent()) {
            System.out.println("Visit record found:");
            printVisitTable(List.of(visit.get()));
        } else {
            System.out.println("[!] Visit #" + visitId + " not found for Patient #" + id + ".");
        }
    }

    // ---------------- TABLE RENDERERS ----------------
    private void printPatientTable(List<Patient> patients) {
        String border = "+-----+------------------+-----+-------------+-----------------+------------+";
        System.out.println(border);
        System.out.println("| ID  | Name             | Age | Contact     | Condition       | Priority   |");
        System.out.println(border);
        for (Patient p : patients) {
            System.out.printf("| %-3d | %-16s | %-3d | %-11s | %-15s | %-10s |\n",
                    p.getPatientId(),
                    truncate(p.getName(), 16),
                    p.getAge(),
                    truncate(p.getContactNumber(), 11),
                    truncate(p.getMedicalCondition(), 15),
                    p.getPriority());
        }
        System.out.println(border);
    }

    private void printQueueTable(List<Patient> queue) {
        String border = "+-------+-----+------------------+-----------------+------------+";
        System.out.println(border);
        System.out.println("| Pos # | ID  | Name             | Condition       | Priority   |");
        System.out.println(border);
        int pos = 1;
        for (Patient p : queue) {
            System.out.printf("| %-5d | %-3d | %-16s | %-15s | %-10s |\n",
                    pos++,
                    p.getPatientId(),
                    truncate(p.getName(), 16),
                    truncate(p.getMedicalCondition(), 15),
                    p.getPriority());
        }
        System.out.println(border);
    }

    private void printTreatmentTable(List<TreatmentRecord> records) {
        String border = "+-----+------------------+------------------------------+--------------------+";
        System.out.println(border);
        System.out.println("| ID  | Patient Name     | Treatment Given              | Completed At       |");
        System.out.println(border);
        for (TreatmentRecord r : records) {
            String time = (r.getCompletedAt() != null) ? r.getCompletedAt().format(TIME_FMT) : "N/A";
            System.out.printf("| %-3d | %-16s | %-28s | %-18s |\n",
                    r.getPatientId(),
                    truncate(r.getPatientName(), 16),
                    truncate(r.getTreatmentGiven(), 28),
                    time);
        }
        System.out.println(border);
    }

    private void printVisitTable(List<Visit> visits) {
        String border = "+----------+------------+--------------------+--------------------+--------------------+";
        System.out.println(border);
        System.out.println("| Visit ID | Date       | Doctor Name        | Diagnosis          | Treatment          |");
        System.out.println(border);
        for (Visit v : visits) {
            System.out.printf("| %-8d | %-10s | %-18s | %-18s | %-18s |\n",
                    v.getVisitId(),
                    v.getVisitDate(),
                    truncate(v.getDoctorName(), 18),
                    truncate(v.getDiagnosis(), 18),
                    truncate(v.getTreatment(), 18));
        }
        System.out.println(border);
    }

    private String truncate(String val, int max) {
        if (val == null) return "";
        return (val.length() > max) ? val.substring(0, max - 1) + "." : val;
    }

    // ---------------- SEED SAMPLE DATA ----------------
    private void loadSampleData() {
        try {
            Patient p1 = new Patient(1, "Kamal Perera", 35, "0771234567", "Chest Pain", Priority.CRITICAL);
            Patient p2 = new Patient(2, "Asmija Banu", 24, "0763449151", "stomach pain", Priority.STANDARD);
            Patient p3 = new Patient(3, "Nimal Silva", 42, "0719876543", "Fracture", Priority.URGENT);

            p2.getVisitHistory().addVisit(new Visit(101, LocalDate.of(2026, 8, 15), "Dr. Fernando", "Gastritis", "Antacids"));
            p2.getVisitHistory().addVisit(new Visit(102, LocalDate.of(2026, 9, 2), "Dr. Perera", "Ulcer check", "Omeprazole"));

            patientBST.insert(p1);
            patientBST.insert(p2);
            patientBST.insert(p3);

            emergencyQueue.enqueue(p1);
            emergencyQueue.enqueue(p2);

            treatmentStack.push(new TreatmentRecord(1, "Kamal Perera", "ECG & Aspirin", LocalDateTime.now().minusMinutes(30)));
        } catch (DuplicatePatientException ignored) {
        }
    }

    // ---------------- INPUT HELPERS ----------------
    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid whole number: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private Priority readPriority() {
        System.out.println("Priority: 1=CRITICAL  2=URGENT  3=STANDARD");
        int choice = readInt("Choose priority: ");
        return switch (choice) {
            case 1 -> Priority.CRITICAL;
            case 2 -> Priority.URGENT;
            default -> Priority.STANDARD;
        };
    }
}
