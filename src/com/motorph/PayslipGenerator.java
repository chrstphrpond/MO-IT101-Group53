package com.motorph;

/**
 * PayslipGenerator.java - Generates formatted payslip console output.
 * Implements FR-09: Display Formatted Payslip Summary.
 */
public class PayslipGenerator {

    private static final String LINE = "============================================";
    private static final String DIVIDER = "--------------------------------------------";

    /**
     * Prints a formatted payslip to the console for a given employee.
     *
     * @param employee        Employee record
     * @param payPeriod       Pay period label (e.g., "2026-03-01 to 2026-03-07")
     * @param hoursWorked     Total hours worked
     * @param grossPay        Computed gross pay
     * @param sss             SSS deduction
     * @param philHealth      PhilHealth deduction
     * @param pagIbig         Pag-IBIG deduction
     * @param withholdingTax  Withholding tax deduction
     * @param lateDeductions  Late/undertime deductions
     * @param netPay          Final net pay
     */
    public void printPayslip(Employee employee, String payPeriod,
                             double hoursWorked, double grossPay,
                             double sss, double philHealth, double pagIbig,
                             double withholdingTax, double lateDeductions, double netPay) {

        double totalDeductions = sss + philHealth + pagIbig + withholdingTax + lateDeductions;

        System.out.println(LINE);
        System.out.println("       MOTORPH PAYROLL SYSTEM");
        System.out.println("         EMPLOYEE PAYSLIP");
        System.out.println(LINE);
        System.out.printf("Pay Period  : %s%n", payPeriod);
        System.out.println(DIVIDER);
        System.out.println("EMPLOYEE DETAILS");
        System.out.printf("  Employee No  : %s%n", employee.getEmployeeNumber());
        System.out.printf("  Name         : %s%n", employee.getFullName());
        System.out.printf("  Position     : %s%n", employee.getPosition());
        System.out.printf("  Status       : %s%n", employee.getStatus());
        System.out.println(DIVIDER);
        System.out.println("ATTENDANCE");
        System.out.printf("  Hours Worked : %.2f hrs%n", hoursWorked);
        System.out.println(DIVIDER);
        System.out.println("EARNINGS");
        System.out.printf("  Gross Pay              PHP %,10.2f%n", grossPay);
        System.out.println(DIVIDER);
        System.out.println("DEDUCTIONS");
        System.out.printf("  SSS Contribution       PHP %,10.2f%n", sss);
        System.out.printf("  PhilHealth             PHP %,10.2f%n", philHealth);
        System.out.printf("  Pag-IBIG               PHP %,10.2f%n", pagIbig);
        System.out.printf("  Withholding Tax        PHP %,10.2f%n", withholdingTax);
        System.out.printf("  Late/Undertime         PHP %,10.2f%n", lateDeductions);
        System.out.printf("  Total Deductions       PHP %,10.2f%n", totalDeductions);
        System.out.println(LINE);
        System.out.printf("  NET PAY                PHP %,10.2f%n", netPay);
        System.out.println(LINE);
        System.out.println();
    }
}
