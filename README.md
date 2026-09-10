# Mini Hospital Emergency Management System

**Course:** CIT300 - Data Structures and Algorithms  
**Assignment:** Individual Mid Assignment  
**Student Name:** MN. Rifa  
**Student ID:** 23DA2-0502  
**Submission Date:**7 September 2026  

---

## 1. Project Overview

The Mini Hospital Emergency Management System is a console-based Java application developed for CIT300 (Data Structures and Algorithms). It simulates core hospital operations:
- Registering and managing patient records
- Queueing emergency patients in arrival order (FIFO)
- Tracking completed treatments with an undo facility (LIFO)
- Maintaining detailed past consultation histories per patient

All data structures in this project (`PatientBST`, `EmergencyQueue`, `TreatmentStack`, and `VisitHistory`) were implemented from scratch without using built-in Java collection classes (`java.util.LinkedList`, `java.util.Stack`, `java.util.TreeMap`, etc.) to demonstrate understanding of node manipulation, pointer handling, and memory management.

---

## 2. Package and Directory Structure

```
hospital-advanced/
│
├── README.md
├── bin/
└── src/
    └── com/
        └── hospital/
            ├── Main.java
            │
            ├── model/
            │   ├── Patient.java
            │   ├── Priority.java
            │   ├── TreatmentRecord.java
            │   └── Visit.java
            │
            ├── structures/
            │   ├── QueueADT.java
            │   ├── StackADT.java
            │   ├── EmergencyQueue.java
            │   ├── TreatmentStack.java
            │   ├── PatientBST.java
            │   └── VisitHistory.java
            │
            └── exception/
                ├── DuplicatePatientException.java
                ├── EmptyStructureException.java
                └── PatientNotFoundException.java
```

---

## 3. Data Structures Implementation

### 3.1 Patient Records: Binary Search Tree (`PatientBST.java`)
The patient directory is stored in an unbalanced Binary Search Tree keyed on `patientId`.

- **Node Structure:** Each node stores a `Patient` object and references to `left` and `right` child nodes.
- **Insert (`insert`):** Inserts a new patient by comparing IDs. If the ID matches an existing record, a checked `DuplicatePatientException` is thrown to ensure uniqueness.
- **Search (`search`):** Recursively navigates left or right based on the ID. Throws `PatientNotFoundException` if the record does not exist.
- **Delete (`delete`):** Implements Hibbard deletion covering all three standard cases:
  1. Leaf node: The node is unlinked by setting the parent pointer to `null`.
  2. Single child: The node is replaced directly with its single child.
  3. Two children: Finds the in-order successor (minimum value in the right subtree), replaces the target node's data with the successor's data, and recursively deletes the successor.
- **In-Order Traversal (`inOrder`):** Traverses left subtree, root, then right subtree. This produces a sorted list of all patients in ascending order of Patient ID in O(n) time.

### 3.2 Emergency Queue: Singly Linked FIFO Queue (`EmergencyQueue.java`)
Manages patients awaiting emergency treatment under strict First-In, First-Out (FIFO) ordering.

- **Interface:** Implements `QueueADT<T>` generic interface.
- **Internal Storage:** Backed by custom `Node<T>` objects with both `front` and `rear` pointers.
- **Enqueue (`enqueue`):** Appends an incoming patient to `rear` in O(1) time.
- **Dequeue (`dequeue`):** Removes the next patient from `front` in O(1) time. If the queue is empty, an `EmptyStructureException` is thrown.
- **Snapshot (`snapshot`):** Produces a non-destructive sequential list from front to rear so the waiting list can be displayed in the console without dequeuing patients.

### 3.3 Treatment History: Array-Backed Dynamic Stack (`TreatmentStack.java`)
Stores completed emergency treatment records under Last-In, First-Out (LIFO) access.

- **Interface:** Implements `StackADT<T>` generic interface.
- **Internal Storage:** Uses an `Object[]` array with an integer pointer `top`.
- **Dynamic Resizing:** The stack starts with capacity 16. When the array is full, capacity is doubled automatically via `Arrays.copyOf()`, giving amortized O(1) push operations.
- **Push (`push`):** Adds the latest completed treatment to `top`.
- **Pop (`pop`):** Removes the most recent treatment record. Explicitly clears the reference (`data[top--] = null`) to allow the Java Garbage Collector to reclaim memory. Throws `EmptyStructureException` if empty.
- **Snapshot (`snapshot`):** Reads records from `top` down to index 0, displaying the most recently treated patients first.

### 3.4 Patient Visit History: Singly Linked List (`VisitHistory.java`)
Maintains a chronological record of historical outpatient consultations for each patient.

- **Encapsulation:** Each `Patient` object owns an independent instance of `VisitHistory`.
- **Add Visit (`addVisit`):** Appends a new `Visit` node at the end of the list in O(n) time (preserving chronological order).
- **Remove Visit (`removeVisit`):** Traverses the list, unlinks the node matching the provided `visitId`, and returns true if found.
- **Search Visit (`searchVisit`):** Iterates through nodes looking for a specific visit ID, returning the result wrapped in an `Optional<Visit>`.
- **Snapshot (`snapshot`):** Returns all visits as a list for formatted display.

---

## 4. Domain Models

- **`Patient`:** Represents a registered patient with `patientId`, `name`, `age`, `contactNumber`, `medicalCondition`, `priority`, and an internal `VisitHistory`. Implements `Comparable<Patient>` so it compares naturally on `patientId`.
- **`Visit`:** Immutable record storing `visitId`, `visitDate` (`LocalDate`), `doctorName`, `diagnosis`, and `treatment`.
- **`TreatmentRecord`:** Represents emergency care rendered to a dequeued patient, storing `patientId`, `patientName`, `treatmentGiven`, and completion timestamp (`LocalDateTime`).
- **`Priority`:** Enum (`CRITICAL`, `URGENT`, `STANDARD`). Stored as clinical triage metadata on the patient record.

---

## 5. Custom Exceptions and Error Handling

The application uses checked exceptions rather than returning `null` or boolean status flags:
- **`DuplicatePatientException`:** Thrown when attempting to register a patient with an ID that already exists in the BST.
- **`PatientNotFoundException`:** Thrown when a patient ID is not found during search, deletion, queueing, or visit recording.
- **`EmptyStructureException`:** Thrown when attempting to dequeue an empty queue or pop an empty stack.

All exceptions are caught in a central `try-catch` block inside `Main.java`'s main loop:
```java
try {
    switch (choice) { ... }
} catch (DuplicatePatientException | PatientNotFoundException | EmptyStructureException ex) {
    System.out.println("[!] ERROR: " + ex.getMessage());
}
```
This central design prevents invalid user input from crashing the program and prints clear messages.

---

## 6. Console Menu Guide (Options 0–13)

The interactive menu provides 14 options grouped by functional area:

```text
+-------------------------------------------------+
|    MINI HOSPITAL EMERGENCY MANAGEMENT SYSTEM    |
+-------------------------------------------------+
| MENU                                            |
+-------------------------------------------------+
| Patient Records (BST)                           |
|   1. Register Patient                           |
|   2. Search Patient                             |
|   3. Delete Patient                             |
|   4. List All Patients (in-order)               |
+-------------------------------------------------+
| Emergency Queue                                 |
|   5. Add to Queue                               |
|   6. Treat Next (dequeue)                       |
|   7. View Waiting Queue                         |
+-------------------------------------------------+
| Treatment History (Stack)                       |
|   8. View Treatment History                     |
|   9. Undo Last Treatment (pop)                  |
+-------------------------------------------------+
| Visit History (Linked List)                     |
|  10. Add Visit                                  |
|  11. View Visit History                         |
|  12. Remove Visit                               |
|  13. Search Visit                               |
+-------------------------------------------------+
|   0. Exit                                       |
+-------------------------------------------------+
```

| Option | Function | What It Does |
| :---: | :--- | :--- |
| **1** | Register Patient | Prompts for ID, Name, Age, Contact, Condition, Priority. Adds patient to BST. |
| **2** | Search Patient | Searches BST by ID and displays patient record. |
| **3** | Delete Patient | Removes a patient from the BST using Hibbard deletion. |
| **4** | List All Patients | Performs in-order traversal of BST, printing all patients sorted by ID. |
| **5** | Add to Queue | Looks up a registered patient in BST and enqueues them into the Emergency Queue. |
| **6** | Treat Next | Dequeues the front patient, records the treatment given, and pushes it onto the Treatment Stack. |
| **7** | View Waiting Queue | Displays all currently waiting patients in arrival order (front to rear). |
| **8** | View Treatment History | Displays all completed treatments with most recent first (top of stack). |
| **9** | Undo Last Treatment | Pops the top treatment record from the stack. |
| **10** | Add Visit | Adds a consultation visit (Date, Doctor, Diagnosis, Treatment) to a patient's visit history list. |
| **11** | View Visit History | Displays all past visits recorded for a specific patient. |
| **12** | Remove Visit | Removes a single visit from a patient's history by Visit ID. |
| **13** | Search Visit | Finds and displays a specific visit record by Visit ID. |
| **0** | Exit | Closes input scanner and cleanly terminates the program. |

---

## 7. Compilation and Execution Guide

### Prerequisites
- **Java 17 or Java 21 LTS** is required.
- *Important note:* This codebase uses modern Java syntax (switch rules `case 1 -> ...`, `String.repeat()`). Compiling with legacy Java 8 will result in syntax errors, and running class files compiled under Java 21 with a Java 8 JRE will produce an `UnsupportedClassVersionError` (class version 65.0 vs 52.0).

---

### Step 1: Open Terminal in Project Directory
Make sure your terminal is inside the `hospital-advanced` directory:
```powershell
cd "d:\Downloads\files (3) (1)\hospital-advanced"
```

---

### Step 2: Compile and Run

#### Method A: Windows PowerShell (Using Installed JDK 21 Directly)
```powershell
# 1. Compile all Java files into the bin directory
& "C:\Program Files\Java\jdk-21\bin\javac.exe" -d bin (Get-ChildItem -Path src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)

# 2. Run the program
& "C:\Program Files\Java\jdk-21\bin\java.exe" -cp bin com.hospital.Main
```

#### Method B: Standard Command Prompt (CMD)
```cmd
if not exist bin mkdir bin
javac -d bin -sourcepath src src\com\hospital\Main.java src\com\hospital\model\*.java src\com\hospital\structures\*.java src\com\hospital\exception\*.java
java -cp bin com.hospital.Main
```

#### Method C: Linux / macOS
```bash
mkdir -p bin
javac -d bin -sourcepath src $(find src -name "*.java")
java -cp bin com.hospital.Main
```

---

## 8. Time and Space Complexity Summary

| Data Structure | Operation | Method | Average Time | Worst Time | Space Complexity |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **PatientBST** | Search | `search(id)` | O(log n) | O(n) | O(1) auxiliary |
| | Insert | `insert(patient)` | O(log n) | O(n) | O(1) auxiliary |
| | Delete | `delete(id)` | O(log n) | O(n) | O(1) auxiliary |
| | In-Order List | `inOrder()` | O(n) | O(n) | O(h) recursion stack |
| **EmergencyQueue** | Enqueue | `enqueue(patient)` | O(1) | O(1) | O(1) auxiliary |
| | Dequeue | `dequeue()` | O(1) | O(1) | O(1) auxiliary |
| | Snapshot | `snapshot()` | O(n) | O(n) | O(n) auxiliary list |
| **TreatmentStack** | Push | `push(record)` | O(1) | O(n) *(resize only)* | O(1) auxiliary |
| | Pop | `pop()` | O(1) | O(1) | O(1) auxiliary |
| | Snapshot | `snapshot()` | O(n) | O(n) | O(n) auxiliary list |
| **VisitHistory** | Add Visit | `addVisit(visit)` | O(n) | O(n) | O(1) auxiliary |
| | Remove Visit | `removeVisit(id)` | O(n) | O(n) | O(1) auxiliary |
| | Search Visit | `searchVisit(id)` | O(n) | O(n) | O(1) auxiliary |

---

## 9. Design Decisions and Trade-offs

1. **Unbalanced BST vs Self-Balancing Trees:**
   The assignment specification asks specifically for a Binary Search Tree. A plain BST gives O(log n) performance for random insertions. However, if patient IDs are inserted in strictly ascending order (e.g., 101, 102, 103), the tree degrades into a linear chain with O(n) time. For production systems, an AVL tree or Red-Black Tree (such as `java.util.TreeMap`) would be used to guarantee balanced height.

2. **Generic Queue and Stack Interfaces:**
   `QueueADT<T>` and `StackADT<T>` define abstract contracts. The underlying implementations (`EmergencyQueue` and `TreatmentStack`) operate generically rather than being hardcoded to `Patient` or `TreatmentRecord`. This keeps the data structure layer modular and reusable.

3. **Dynamic Array with Doubling for the Stack:**
   Instead of using a fixed-size array that can overflow, `TreatmentStack` automatically doubles its capacity when full. This guarantees an amortized time complexity of O(1) for push operations while adhering to the requirement of building the structure manually.

4. **Checked Exceptions vs Status Codes:**
   Using checked exceptions (`DuplicatePatientException`, `PatientNotFoundException`, `EmptyStructureException`) forces explicit error handling at call sites and avoids ambiguous return values like `null` or `-1`.

5. **FIFO Queue with Priority Metadata:**
   The emergency queue strictly follows FIFO arrival order as required by the assignment. The `Priority` enum (`CRITICAL`, `URGENT`, `STANDARD`) is retained on the `Patient` model as clinical metadata. In an advanced triage system, this could be upgraded to a Priority Queue / Min-Heap.

---

## 10. Suggested Development Commit History

1. `feat: initial project structure, packages, and model classes`
2. `feat(model): implement Patient, Visit, TreatmentRecord models and Priority enum`
3. `feat(structures): implement custom Singly Linked List for VisitHistory`
4. `feat(structures): implement PatientBST with insert and in-order traversal`
5. `feat(structures): implement BST search, Hibbard deletion, and custom exceptions`
6. `feat(structures): implement QueueADT interface and generic EmergencyQueue`
7. `feat(structures): implement StackADT interface and dynamic TreatmentStack`
8. `feat(ui): implement interactive Main console menu and exception handling`
9. `test: verify edge cases (empty structures, duplicate IDs, invalid inputs)`
10. `docs: complete README documentation and complexity analysis`
