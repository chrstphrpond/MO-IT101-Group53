package com.motorph;

/**
 * PayrollCalculator.java - Computes gross pay and net pay for an employee.
 * Implements FR-03 (Gross Pay), FR-08 (Net Pay).
 */
public class PayrollCalculator {

    private DeductionCalculator deductionCalculator;

    public PayrollCalculator() {
        this.deductionCalculator = new DeductionCalculator();
    }

    /**
     * FR-03: Computes gross weekly salary.
     * Formula: Hourly Rate x Total Regular Hours Worked
     *
     * @param employee      The employee record
     * @param hoursWorked   Total hours worked in the pay period
     * @return Gross pay amount
     */
    public double computeGrossPay(Employee employee, double hoursWorked) {
        return employee.getHourlyRate() * hoursWorked;
    }

    /**
     * FR-08: Computes net pay.
     * Formula: Net Pay = Gross Pay - SSS - PhilHealth - Pag-IBIG - W/Tax - Late Deductions
     *
     * @param employee        The employee record
     * @param grossPay        Computed gross pay
     * @param lateDeductions  Late/undertime deduction amount
     * @return Net pay amount
     */
    public double computeNetPay(Employee employee, double grossPay, double lateDeductions) {
        double sss         = deductionCalculator.computeSSS(employee.getBasicMonthlySalary());
        double philHealth  = deductionCalculator.computePhilHealth(employee.getBasicMonthlySalary());
        double pagIbig     = deductionCalculator.computePagIbig(employee.getBasicMonthlySalary());
        double taxableIncome = grossPay - sss - philHealth - pagIbig - lateDeductions;
        double withholdingTax = deductionCalculator.computeWithholdingTax(taxableIncome);

        return grossPay - sss - philHealth - pagIbig - withholdingTax - lateDeductions;
    }
}
