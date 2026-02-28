# MO-IT101 — MotorPH Payroll System

![Java](https://img.shields.io/badge/Language-Java-orange) ![Status](https://img.shields.io/badge/Status-In%20Progress-yellow) ![Term](https://img.shields.io/badge/Term%202-SY%202025--2026-blue)

**Group 53 | Term 2 | SY 2025-2026**
Bachelor of Science in Information Technology
Mapua Malayan Digital College (MMDC)

---

## Project Description

The MotorPH Payroll System is a Java console-based application that automates payroll computation for MotorPH, a motorcycle parts manufacturing and retail company. The system reads employee and attendance records from CSV files, applies statutory deduction rules, and generates a formatted payslip-style output through the console.

### Core Features
- Display employee information from CSV (FR-01)
- Compute hours worked from attendance logs (FR-02)
- Calculate gross weekly salary based on hourly rate (FR-03)
- Apply SSS deduction via bracket lookup table (FR-04)
- Apply PhilHealth deduction at 2.5% employee share (FR-05)
- Apply Pag-IBIG deduction capped at PHP 100.00 (FR-06)
- Compute withholding tax using BIR bracket table (FR-07)
- Compute net pay (FR-08)
- Display formatted payslip summary in console (FR-09)

---

## Project Structure

```
MO-IT101-Group53/
├── README.md
├── .gitignore
├── src/
│   └── com/
│       └── motorph/
│           ├── Main.java                  # Entry point
│           ├── Employee.java              # Employee data model
│           ├── AttendanceProcessor.java   # Hours worked computation
│           ├── PayrollCalculator.java     # Gross pay computation
│           ├── DeductionCalculator.java   # SSS, PhilHealth, Pag-IBIG, W-Tax
│           ├── PayslipGenerator.java      # Formatted console output
│           └── CSVReader.java             # CSV file loading utility
├── data/
│   ├── employees.csv                      # Employee master data
│   └── attendance.csv                     # Daily time logs
└── docs/
    └── MO-IT101-Milestone1-Group53.pdf    # Milestone 1 document
```

---

## How to Run

1. Clone the repository:
```bash
git clone https://github.com/chrstphrpond/MO-IT101-Group53.git
```
2. Open the project in your Java IDE (IntelliJ IDEA, Eclipse, or VS Code)
3. Place `employees.csv` and `attendance.csv` in the `/data` folder
4. Run `Main.java`

**Requirements:** Java JDK 11 or higher

---

## Team Members

| Name | Role |
|------|------|
| Christopher Pond Maquidato | Solo Developer |

---

## Milestones

| Milestone | Description | Status |
|-----------|-------------|--------|
| Milestone 1 | Requirements & Planning Analysis | Completed |
| Milestone 2 | Initial Code Submission | In Progress |

---

## References

- [MotorPH Case Study](https://sites.google.com/mmdc.mcl.edu.ph/motorph/home)
- MO-IT101 Computer Programming 1 — Mapua Malayan Digital College
