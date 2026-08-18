# Sunrise Dental Clinic - Reservation and Management System

**Module:** Advanced Programming (CIS6003)  
**Assessment:** WRIT1 - Online Reservation System  
**University:** Cardiff Metropolitan University / ICBT Campus  
**Student Name:** MOHAMMED JUMAIL  
**Student ID:** CL/BSCSD/35/24  
**Package:** Desktop Application (JAR: `dist/SunriseDentalSystem.jar`)
**System Architecture:** Three-Tier Desktop Application with MySQL Persistence

---

## Overview

**Sunrise Dental Clinic Reservation and Management System** is a modular Java desktop (Swing) application engineered to handle dental clinical operations and patient administration.

The system provides dedicated workflows for three main user roles:

* **Admin**
* **Staff / Receptionist**
* **Dentist**

The codebase is built on an enterprise architecture separating presentation views, business logic calculators, and Data Access Objects (DAOs), with persistent relational storage in MySQL.

---

## User Roles and Access Responsibilities

The system is structured around three primary user roles:

* **Admin:**
* Complete oversight of clinic master data, user accounts, and credentials.
* Configuration of treatment categories, base procedural fees, and clinical parameters.
* System-wide operational dashboard monitoring, global revenue audits, and KPI tracking.
* CSV operational log streaming and full database maintenance.



* **Staff / Receptionist:**
* Patient intake, demographic data registration, and directory management.
* Searching dentist availability and scheduling dynamic appointments.
* Calculating combined treatment bills ($\text{Treatment Cost} + \text{Consultation Fee}$).
* Generating formatted monospaced receipts and executing physical print jobs via the Java Print API.
* Exporting operational reports (Patients and Appointments) to CSV spreadsheets.



* **Dentist:**
* Viewing daily assigned consultation queues and scheduled patient appointments.
* Reviewing treatment procedures, clinical notes, and patient records.
* Issuing patient prescriptions with dosage instructions linked to appointment records.



---

## Stack

### Backend & Core

* **Language:** Java SE (JDK 11 / JDK 17 / JDK 21 compatible)
* **GUI Engine:** Java Swing (`javax.swing.*`, `java.awt.*`)
* **Look & Feel Theme:** FlatLaf Modern Dark/Slate UI (`com.formdev.flatlaf:flatlaf:3.x`)
* **Database Management:** MySQL 8.0+ / MariaDB
* **Connectivity & Persistence:** JDBC API with MySQL Connector/J (`mysql-connector-j-8.x.jar`)
* **Build System:** Apache Ant (Standard NetBeans Build Automation)
* **Printing Engine:** Java 2D Print API (`java.awt.print.PrinterJob`)

### Testing & Verification

* **Test Framework:** JUnit 5 / JUnit 4 runners
* **Assertion Engine:** Standard Java Assertions (`org.junit.Assert.*`)
* **Quality Assurance Matrix:** 35 Structured Test Cases (TC-001 to TC-035)

---

## Architecture

The project adheres to a decoupled **Three-Tier Architecture** utilizing standard software design patterns:

```
+-------------------------------------------------------------------------+
|                          Presentation Layer                             |
|       Swing / FlatLaf GUI: MainDashboard, BillingPage, Form Panels      |
+-------------------------------------------------------------------------+
                                    │
                                    ▼
+-------------------------------------------------------------------------+
|                         Business Logic Layer                            |
|       BillingCalculator, Input Sanitization, CSV Data Streamer          |
+-------------------------------------------------------------------------+
                                    │
                                    ▼
+-------------------------------------------------------------------------+
|                          Data Access Layer                              |
|           Singleton DBConnection, PreparedStatements, JDBC API          |
+-------------------------------------------------------------------------+
                                    │
                                    ▼
+-------------------------------------------------------------------------+
|                        Relational Database                              |
|                MySQL 8.0 Server (sunrise_dental_db)                     |
+-------------------------------------------------------------------------+

```

### Key Design Patterns Used

* **Model-View-Controller (MVC):** Decouples Swing view interfaces (`BillingPage`, `BookAppointmentPage`) from database transactions via business logic calculation engines (`BillingCalculator`).
* **Singleton Pattern:** Enforced in `DBConnection.java` to guarantee a single persistent JDBC connection instance without connection leaks.
* **CardLayout View Controller:** Orchestrated inside `MainDashboard.java` to switch between functional modules dynamically within a single viewport.
* **Factory / Builder Strategy:** Standardizes the instantiation of navigation buttons, status tags, form rows, and receipt layouts across UI components.

### Module Counts (Current Tree)

* **16 Java Source Classes** under `src/sunrisedentalsystem/`
* **7 Relational Database Tables** in `sunrise_dental_db`
* **35 Verified Functional Test Cases**

---

## Implemented Functional Areas

* **Authentication & Access Control:** Secure user login and staff registration forms (`LoginPage.java`, `SignupPage.java`).
* **KPI Operational Dashboards:** Real-time summary cards displaying active patient counts, scheduled appointments, registered dentists, and financial summaries (`DashboardPage.java`).
* **Patient Registration & Directory:** Validated patient intake forms and searchable patient directory tables (`AddPatientPage.java`, `PatientsPage.java`).
* **Dentist Directory Management:** Dentist roster interface displaying doctor specializations, contact information, and consultation channels (`DentistsPage.java`).
* **Dynamic Appointment Scheduling:** Auto-incrementing appointment numbering (`A0001`, `A0002`), doctor selection, date-time picking, and treatment selection (`BookAppointmentPage.java`, `AppointmentsPage.java`).
* **Dynamic Treatment Cost & Billing Engine:** Real-time billing calculations:

$$\text{Total Bill} = \text{Treatment Procedure Rate} + \text{Consultation Fee}$$


Persisting verified transactions into the `billing` table and rendering on `BillingPage.java`.
* **Receipt Terminal & Physical Print Dispatch:** Split-pane interface rendering monospaced ASCII receipts with native Java Print API integration for physical printing.
* **Clinical Prescriptions Management:** Managing prescriptions with medicine names, dosage frequencies, and clinical instructions linked to appointments (`PrescriptionsPage.java`).
* **User & Staff Account Management:** Administrative control interface for creating and managing staff credentials (`UsersPage.java`).
* **CSV Data Export Engine with File Chooser:** Native file selection dialog (`JFileChooser`) streaming database tables into RFC-4180 compliant `.csv` files (`ReportsPage.java`).
* **Safe Exit Routine:** Safeguards against accidental termination via `JOptionPane` confirmation dialogs.
* **Built-in System Documentation:** Integrated operational help manual (`HelpPage.java`).

---

## Navigation & Page Map

Configured via `CardLayout` in `src/sunrisedentalsystem/MainDashboard.java`:

| Route / Card Key | Component Class | Navigation Trigger / Menu Action |
| --- | --- | --- |
| `DASHBOARD` | `DashboardPage.java` | "⌂ Dashboard" menu click |
| `PATIENTS` | `PatientsPage.java` | "👥 Patients ▼" $\rightarrow$ "└ View Directory" |
| `ADD_PATIENT` | `AddPatientPage.java` | "👥 Patients ▼" $\rightarrow$ "└ Add Patient" |
| `DENTISTS` | `DentistsPage.java` | "🩺 Dentists" menu click |
| `APPOINTMENTS` | `AppointmentsPage.java` | "📅 Appointments ▼" $\rightarrow$ "└ View Appointments" |
| `BOOK_APP` | `BookAppointmentPage.java` | "📅 Appointments ▼" $\rightarrow$ "└ Book Appointment" |
| `BILLING` | `BillingPage.java` | "💳 Billing & Receipts" menu click |
| `PRESCRIPTIONS` | `PrescriptionsPage.java` | "✎ Prescriptions" menu click |
| `USERS` | `UsersPage.java` | "👤 User Management ▼" $\rightarrow$ "└ Manage Users" |
| `REPORTS` | `ReportsPage.java` | "📊 Reports & CSV" menu click |
| `HELP` | `HelpPage.java` | "? Help Manual" menu click |
| *Exit System* | Modal Dialog Handler | "▮ Exit System" $\rightarrow$ Safe exit confirmation prompt |

---

## Database

* **Database Name:** `sunrise_dental_db`
* **Port:** `3306`
* **Driver:** `com.mysql.cj.jdbc.Driver`
* **Connection URL:** `jdbc:mysql://localhost:3306/sunrise_dental_db`

### Database Schema Definition

```sql
CREATE DATABASE IF NOT EXISTS sunrise_dental_db;
USE sunrise_dental_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STAFF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Patients Table
CREATE TABLE IF NOT EXISTS patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    contact_no VARCHAR(20) NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Dentists Table
CREATE TABLE IF NOT EXISTS dentists (
    dentist_id INT AUTO_INCREMENT PRIMARY KEY,
    dentist_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    contact_no VARCHAR(20),
    email VARCHAR(100)
);

-- 4. Treatments Master Table
CREATE TABLE IF NOT EXISTS treatments (
    treatment_id INT AUTO_INCREMENT PRIMARY KEY,
    treatment_name VARCHAR(100) NOT NULL,
    cost DOUBLE NOT NULL
);

-- 5. Appointments Table
CREATE TABLE IF NOT EXISTS appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_no VARCHAR(20) NOT NULL UNIQUE,
    patient_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    contact_no VARCHAR(20),
    dentist_name VARCHAR(100),
    treatment_type VARCHAR(100),
    appointment_date VARCHAR(50),
    status VARCHAR(20) DEFAULT 'Pending'
);

-- 6. Billing Table
CREATE TABLE IF NOT EXISTS billing (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_no VARCHAR(20) NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL,
    status VARCHAR(20) DEFAULT 'Paid',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_no) REFERENCES appointments(appointment_no) ON DELETE CASCADE
);

-- 7. Prescriptions Table
CREATE TABLE IF NOT EXISTS prescriptions (
    prescription_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_no VARCHAR(20) NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    medicine VARCHAR(255) NOT NULL,
    dosage VARCHAR(100),
    instructions TEXT,
    prescribed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed Base Treatments Data
INSERT INTO treatments (treatment_name, cost) VALUES
('Cleaning & Polishing', 2500.00),
('Tooth Filling', 3500.00),
('Root Canal Therapy', 15000.00),
('Teeth Whitening', 12000.00),
('Tooth Extraction', 5000.00);

-- Seed Base Dentist Data
INSERT INTO dentists (dentist_name, specialization, contact_no, email) VALUES
('Smith', 'General Dentistry', '0771234567', 'smith@sunrisedental.com'),
('Jumail', 'Orthodontics', '0760527222', 'jumail@sunrisedental.com');

```

---

## Quick Start

### Prerequisites

* **JDK:** Java SE Development Kit (JDK 11, 17, or 21)
* **IDE:** Apache NetBeans IDE (v12.0+)
* **Database Server:** MySQL Server 8.0+ / XAMPP

### Step 1: Database Setup

1. Launch **Apache** and **MySQL** services via the XAMPP Control Panel.
2. Open phpMyAdmin (`http://localhost/phpmyadmin`).
3. Create a database named `sunrise_dental_db`.
4. Execute the SQL statements provided in the schema section above.

### Step 2: Open and Configure Project in NetBeans

1. Open **Apache NetBeans IDE**.
2. Click **File** $\rightarrow$ **Open Project...** and select the root directory of `SunriseDentalSystem`.
3. Ensure required `.jar` dependencies are mapped in the **Libraries** folder:
* `mysql-connector-j-8.x.jar`
* `flatlaf-3.x.jar`


### Step 3: Build & Run

* **Run in NetBeans:** Press **`Shift + F6`** (or right-click `Main.java` $\rightarrow$ **Run File**).
* **Build via Ant CLI:**
```bash
ant clean jar

```

* **Run Standalone JAR:**
```bash
java -jar dist/SunriseDentalSystem.jar

```

---

## Seed Accounts

| Role | Username | Password | Access Scope |
| --- | --- | --- | --- |
| **Admin** | `admin` | `Admin@1234` | Full System & Master Configuration |
| **Staff / Receptionist** | `staff` | `Staff@1234` | Patient Intake, Appointments, Billing & CSV Reports |
| **Demo Staff** | `reception` | `reception123` | Patient Registration & Appointment Scheduling |

---

## Configuration Notes

* **Database Connection Configuration:** Located in `src/sunrisedentalsystem/DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/sunrise_dental_db";
private static final String USER = "root";
private static final String PASSWORD = "";

```

* **Currency Formatting:** Standardized across all calculation routines and receipts to Sri Lankan Rupees (`LKR`, `LKR XX.XX`).

---

## Testing Coverage Areas

The system includes a 35-point testing matrix covering positive, negative, and edge-case execution paths:

* **Authentication & Access Control:** TC-001 to TC-005 (Valid login, invalid password, blank fields, duplicate accounts).


* **Patient Intake & Search Filters:** TC-006 to TC-008 (Record creation, non-numeric validation, real-time search).
* **Dentist Directory & Specialties:** TC-009 to TC-010 (Roster retrieval, specialty dropdown mapping).
* **Appointment Scheduling:** TC-011 to TC-015 (Sequential ID generation, mandatory field validation, treatment switches).

* **Dynamic Billing & Invoicing Engine:** TC-016 to TC-020 (Dynamic arithmetic, search matching, monospaced receipt formatting, Java Print API dispatch).

* **Prescriptions Management:** TC-021 to TC-022 (Clinical note capture, history records).
* **Reports & CSV Data Streaming:** TC-023 to TC-025 (`JFileChooser` path selection, RFC-4180 CSV compliance).

* **User Management & Role Permissions:** TC-026 to TC-028 (Staff creation, unique username constraints, active directory).

* **System Health, Security & Fault Tolerance:** TC-029 to TC-035 (Singleton connection validation, SQL injection mitigation via `PreparedStatement`, safe exit routines, offline database handling).


---

## Repository Layout

```text
SunriseDentalSystem/
├── build.xml                           # Ant Build Automation Script
├── manifest.mf                         # Application Archive Manifest
│
├── nbproject/                          # NetBeans Build Configuration
│   ├── build-impl.xml
│   ├── genfiles.properties
│   ├── project.properties
│   └── project.xml
│
├── src/
│   └── sunrisedentalsystem/
│       ├── Main.java                   # Application Entry Launcher
│       ├── DBConnection.java           # Singleton MySQL JDBC Driver Manager
│       ├── MainDashboard.java          # CardLayout Main Frame & Sidebar Navigation
│       ├── LoginPage.java              # User Authentication Screen
│       ├── SignupPage.java             # User Registration Interface
│       ├── DashboardPage.java          # Real-Time KPI Summary Statistics
│       ├── AddPatientPage.java         # Patient Intake Module
│       ├── PatientsPage.java           # Patient Directory Table with Search
│       ├── DentistsPage.java           # Doctor Roster & Specialty Cards
│       ├── BookAppointmentPage.java    # Dynamic Booking & Auto-Fee Generator
│       ├── AppointmentsPage.java       # Appointment Schedule Ledger
│       ├── BillingCalculator.java      # Dynamic Billing Business Logic Engine
│       ├── BillingPage.java            # Split-Pane Receipts & Billing Terminal
│       ├── PrescriptionsPage.java      # Medication Prescriptions Manager
│       ├── UsersPage.java              # Staff Account Management Module
│       ├── ReportsPage.java            # JFileChooser CSV Exporter Module
│       └── HelpPage.java               # Built-in User Documentation Manual
│
└── dist/
    └── SunriseDentalSystem.jar         # Compiled Executable Desktop Binary

```

---

## Author & Academic Attribution

* **Developer:** Mohammed Jumail Abdul Wahab
* **Student ID:** CL/BSCSD/33/119
* **Institution:** ICBT Campus / Cardiff Metropolitan University
* **Unit Code & Title:** CIS6003 - Advanced Programming
* **Academic Year:** 2026
