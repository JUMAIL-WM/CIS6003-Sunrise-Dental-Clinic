# Dental Reservation System

**Module:** Advanced Programming (CIS6003)  
**Assessment:** WRIT1 - Online Reservation System  
**University:** Cardiff Metropolitan University / ICBT Campus  
**Student Name:** MOHAMMED JUMAIL  
**Student ID:** CL/BSCSD/35/24  

---

## 📌 Project Overview
Sunrise Dental Clinic is a busy private dental center in Colombo. This Java-based desktop application is developed to digitize and manage patient appointments, treatment billing, and staff authentication, solving issues related to paper-based manual records, double bookings, and billing errors.

---

## ✨ Features Implemented

1. **User Authentication (Login)**
   - Secure access using staff credentials to restrict unauthorized access.
2. **Register New Appointment**
   - Captures Appointment Number, Patient Name, Address, Contact, Dentist Name, Treatment Type, Date, and Time.
3. **Display & Search Appointment Details**
   - Instant search capability using the unique Appointment Number.
4. **Calculate and Print Bill**
   - Automatically computes treatment costs based on selected treatment types and consultation fees, generating a printable digital receipt.
5. **Help Section**
   - Step-by-step instructions for new clinic staff on navigating the system.
6. **Exit System**
   - Allows safe closing of the application.

---

## 🛠️ Tech Stack & Architecture

- **Programming Language:** Java (JDK 8+)
- **User Interface:** Java Swing (GUI)
- **Database:** MySQL (XAMPP Server)
- **Database Connector:** JDBC (`mysql-connector-java.jar`)
- **IDE:** Apache NetBeans
- **Design Pattern Used:** Singleton Pattern (for Thread-Safe Database Connection) & 3-Tier Architecture
- **Version Control:** Git & GitHub Workflows

---

## 🗄️ Database Setup Instructions

1. Start **Apache** and **MySQL** in **XAMPP Control Panel**.
2. Open `http://localhost/phpmyadmin` in your web browser.
3. Create a new database named `sunrise_dental_db`.
4. Execute the following SQL queries to set up required tables:

---

## 🚀 How to Run the Project

1. Clone or download this repository to your local machine:
```bash
git clone [https://github.com/JUMAIL-WM/CIS6003-Sunrise-Dental-Clinic.git](https://github.com/JUMAIL-WM/CIS6003-Sunrise-Dental-Clinic.git)

```


2. Open **Apache NetBeans IDE**.
3. Go to `File` ➔ `Open Project` and select the downloaded project folder.
4. Ensure `mysql-connector-java.jar` is added to the project **Libraries**.
5. Locate `Main.java` or `LoginFrame.java` in `src/sunrisedentalsystem/`.
6. Right-click the file and select **Run File** (or press `Shift + F6`).

**Default Login Credentials:**

* **Username:** `staff`
* **Password:** `admin123`

---

## 🧪 Testing & Automation

Unit testing was performed using **JUnit 5** framework to validate the business logic layer, specifically the total treatment cost calculation in `BillingCalculatorTest.java`.

---

## 📜 License & Acknowledgments

Developed for academic assessment purposes under Cardiff Metropolitan University module **CIS6003 - Advanced Programming**.

```

```
