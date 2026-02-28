package com.motorph;

import java.util.List;
import java.util.Scanner;

/**
 * MO-IT101 - MotorPH Payroll System
 * Group 53 | Term 2 | SY 2025-2026
 *
 * Main.java - Entry point of the MotorPH Payroll System.
 * Displays the main menu and routes user input to appropriate modules.
 */
public class Main {

    // Shared instances of system modules
    private static CSVReader           csvReader           = new CSVReader();
    private static AttendanceProcessor attendanceProcessor = new AttendanceProcessor();
    private static PayrollCalculator   payrollCalculator   = new PayrollCalculator();
    private static PayslipGenerator    payslipGenerator    = new PayslipGenerator();
    private static Scanner             scanner             = new Scanner(System.in);

    public static void main(String[] args) {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Enter choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    viewEmployeeInformation();
                    break;
                case "2":
                    processPayroll();
                    break;
                case "3":
                    generatePayslip();
                    break;
                case "0":
                    System.out.println("\nExiting MotorPH Payroll System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please enter 0-3.");
            }
        }
        scanner.close();
    }

    // -----------------------------------------------------------------------
    // MENU DISPLAY
    // -----------------------------------------------------------------------

    /** Prints the system banner on startup. */
    private static void printBanner() {
        System.out.println("=============================================");
        System.out.println("       MotorPH Payroll System v1.0          ");
        System.out.println("       Group 53 | MO-IT101 | Term 2         ");
        System.out.println("=============================================");
        System.out.println();
    }

    /** Prints the main navigation menu. */
    private static void printMainMenu() {
        System.out.println("---------------------------------------------");
        System.out.println(" MAIN MENU");
        System.out.println("---------------------------------------------");
        System.out.println(" [1] View Employee Information");
        System.out.println(" [2] Process Payroll");
        System.out.println(" [3] Generate Payslip");
        System.out.println(" [0] Exit");
        System.out.println("---------------------------------------------");
    }

    // -----------------------------------------------------------------------
    // MODULE 1: VIEW EMPLOYEE INFORMATION (FR-01)
    // -----------------------------------------------------------------------

    /**
     * Allows user to search for an employee by ID and view their information.
     * Implements FR-01: View Employee Information.
     */
    private static void viewEmployeeInformation() {
        System.out.println("\n=== VIEW EMPLOYEE INFORMATION ===");
        System.out.print("Enter Employee Number (or ENTER to list all): ");
        String empNum = scanner.nextLine().trim();

        List<String[]> employees = csvReader.loadEmployees("data/employees.csv");
        boolean found = false;

        for (Employee emp : csvReader.parseEmployees(employees)) {
            if (empNum.isEmpty() || emp.getEmployeeNumber().equals(empNum)) {
                printEmployeeDetails(emp);
                found = true;
                if (!empNum.isEmpty()) break;
            }
        }
        if (!found) {
            System.out.println("[!] Employee #" + empNum + " not found.");
        }
    }

    /** Prints formatted employee details to console. */
    private static void printEmployeeDetails(Employee emp) {
        System.out.println("\n--- Employee Details ---");
        System.out.printf("  Employee #  : %s%n",  emp.getEmployeeNumber());
        System.out.printf("  Name        : %s, %s%n", emp.getLastName(), emp.getFirstName());
        System.out.printf("  Birthday    : %s%n",  emp.getBirthday());
        System.out.printf("  Position    : %s%n",  emp.getPosition());
        System.out.printf("  Status      : %s%n",  emp.getStatus());
        System.out.printf("  Basic Salary: PHP %,.2f%n", emp.getBasicMonthlySalary());
        System.out.printf("  Hourly Rate : PHP %,.2f%n", emp.getHourlyRate());
        System.out.printf("  SSS #       : %s%n",  emp.getSssNumber());
        System.out.printf("  PhilHealth #: %s%n",  emp.getPhilHealthNumber());
        System.out.printf("  Pag-IBIG #  : %s%n",  emp.getPagIbigNumber());
        System.out.printf("  TIN #       : %s%n",  emp.getTin());
        System.out.println("------------------------");
    }

    // -----------------------------------------------------------------------
    // MODULE 2: PROCESS PAYROLL (FR-02, FR-03, FR-04 to FR-08)
    // -----------------------------------------------------------------------

    /**
     * Calculates gross pay, deductions, and net pay for a selected employee
     * over a specified pay period. Implements FR-02 through FR-08.
     */
    private static void processPayroll() {
        System.out.println("\n=== PROCESS PAYROLL ===");
        System.out.print("Enter Employee Number: ");
        String empNum = scanner.nextLine().trim();

        // Look up employee
        Employee emp = findEmployee(empNum);
        if (emp == null) {
            System.out.println("[!] Employee #" + empNum + " not found.");
            return;
        }

        System.out.print("Enter Pay Period Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine().trim();
        System.out.print("Enter Pay Period End Date   (YYYY-MM-DD): ");
        String endDate = scanner.nextLine().trim();

        // FR-02: Calculate hours worked and late minutes
        double hoursWorked   = attendanceProcessor.computeHoursWorked(empNum, startDate, endDate);
        double lateMinutes   = attendanceProcessor.computeLateMinutes(empNum, startDate, endDate);
        double lateDeduction = (emp.getHourlyRate() / 60.0) * lateMinutes;

        // FR-03: Gross Pay = Hourly Rate x Hours Worked
        double grossPay = payrollCalculator.computeGrossPay(emp, hoursWorked);

        // FR-04 to FR-06: Statutory deductions
        DeductionCalculator dc = new DeductionCalculator();
        double sss        = dc.computeSSS(emp.getBasicMonthlySalary());
        double philHealth = dc.computePhilHealth(emp.getBasicMonthlySalary());
        double pagIbig    = dc.computePagIbig(emp.getBasicMonthlySalary());

        // FR-07: Withholding tax on taxable income
        double taxableIncome    = grossPay - sss - philHealth - pagIbig - lateDeduction;
        double withholdingTax   = dc.computeWithholdingTax(taxableIncome);

        // FR-08: Net Pay
        double netPay = payrollCalculator.computeNetPay(emp, grossPay, lateDeduction);

        // Display payroll summary
        System.out.println("\n--- Payroll Summary ---");
        System.out.printf("  Employee    : %s %s (#%s)%n",
            emp.getFirstName(), emp.getLastName(), emp.getEmployeeNumber());
        System.out.printf("  Pay Period  : %s to %s%n", startDate, endDate);
        System.out.printf("  Hours Worked: %.1f hrs%n",   hoursWorked);
        System.out.printf("  Late Minutes: %.0f mins%n",  lateMinutes);
        System.out.println("  ..........................................");
        System.out.printf("  Gross Pay        : PHP %,12.2f%n", grossPay);
        System.out.printf("  SSS Deduction    : PHP %,12.2f%n", sss);
        System.out.printf("  PhilHealth       : PHP %,12.2f%n", philHealth);
        System.out.printf("  Pag-IBIG         : PHP %,12.2f%n", pagIbig);
        System.out.printf("  Late Deduction   : PHP %,12.2f%n", lateDeduction);
        System.out.printf("  Withholding Tax  : PHP %,12.2f%n", withholdingTax);
        System.out.println("  ..........................................");
        System.out.printf("  NET PAY          : PHP %,12.2f%n", netPay);
        System.out.println("-----------------------");
    }

    // -----------------------------------------------------------------------
    // MODULE 3: GENERATE PAYSLIP (FR-09)
    // -----------------------------------------------------------------------

    /**
     * Generates and prints a formatted payslip for an employee.
     * Implements FR-09: Generate Payslip.
     */
    private static void generatePayslip() {
        System.out.println("\n=== GENERATE PAYSLIP ===");
        System.out.print("Enter Employee Number: ");
        String empNum = scanner.nextLine().trim();

        Employee emp = findEmployee(empNum);
        if (emp == null) {
            System.out.println("[!] Employee #" + empNum + " not found.");
            return;
        }

        System.out.print("Enter Pay Period Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine().trim();
        System.out.print("Enter Pay Period End Date   (YYYY-MM-DD): ");
        String endDate = scanner.nextLine().trim();

        double hoursWorked   = attendanceProcessor.computeHoursWorked(empNum, startDate, endDate);
        double lateMinutes   = attendanceProcessor.computeLateMinutes(empNum, startDate, endDate);
        double lateDeduction = (emp.getHourlyRate() / 60.0) * lateMinutes;
        double grossPay      = payrollCalculator.computeGrossPay(emp, hoursWorked);
        double netPay        = payrollCalculator.computeNetPay(emp, grossPay, lateDeduction);

        // FR-09: Print formatted payslip
        payslipGenerator.printPayslip(emp, startDate, endDate,
            hoursWorked, grossPay, lateDeduction, netPay);
    }

    // -----------------------------------------------------------------------
    // UTILITY
    // -----------------------------------------------------------------------

    /**
     * Finds and returns an Employee object by employee number.
     *
     * @param employeeNumber The employee ID to search for
     * @return Employee object if found, null otherwise
     */
    private static Employee findEmployee(String employeeNumber) {
        List<String[]> rows = csvReader.loadEmployees("data/employees.csv");
        for (Employee emp : csvReader.parseEmployees(rows)) {
            if (emp.getEmployeeNumber().equals(employeeNumber)) {
                return emp;
            }
        }
        return null;
    }
}
