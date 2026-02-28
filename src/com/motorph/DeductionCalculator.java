package com.motorph;

/**
 * DeductionCalculator.java - Computes all statutory deductions.
 * Implements FR-04 (SSS), FR-05 (PhilHealth), FR-06 (Pag-IBIG), FR-07 (Withholding Tax).
 */
public class DeductionCalculator {

    /**
     * FR-04: Computes SSS deduction using bracket-based contribution table.
     * @param grossMonthlySalary Employee's gross monthly salary
     * @return Employee SSS contribution amount
     */
    public double computeSSS(double grossMonthlySalary) {
        // TODO: Implement SSS bracket lookup table
        // Reference: Official SSS Contribution Table
        return 0.0;
    }

    /**
     * FR-05: Computes PhilHealth deduction.
     * Rate: 5% of basic monthly salary, employee share = 2.5%
     * @param basicMonthlySalary Employee's basic monthly salary
     * @return Employee PhilHealth contribution
     */
    public double computePhilHealth(double basicMonthlySalary) {
        // Employee share = 2.5% of basic monthly salary
        // TODO: Apply PhilHealth ceiling cap per current regulations
        double employeeShare = basicMonthlySalary * 0.025;
        return employeeShare;
    }

    /**
     * FR-06: Computes Pag-IBIG deduction.
     * Rate: 2% of monthly salary, capped at PHP 100.00
     * @param grossMonthlySalary Employee's gross monthly salary
     * @return Employee Pag-IBIG contribution
     */
    public double computePagIbig(double grossMonthlySalary) {
        double contribution = grossMonthlySalary * 0.02;
        // Cap at PHP 100.00
        return Math.min(contribution, 100.0);
    }

    /**
     * FR-07: Computes withholding tax using BIR monthly bracket table.
     * @param taxableIncome Gross pay minus non-taxable deductions
     * @return Withholding tax amount
     */
    public double computeWithholdingTax(double taxableIncome) {
        // TODO: Implement BIR monthly tax bracket lookup
        // Taxable income = Gross Pay - SSS - PhilHealth - Pag-IBIG - Late deductions
        return 0.0;
    }
}
