package com.motorph;

/**
 * DeductionCalculator.java - Computes all statutory deductions.
 * Implements FR-04 (SSS), FR-05 (PhilHealth), FR-06 (Pag-IBIG), FR-07 (Withholding Tax).
 */
public class DeductionCalculator {

    /**
     * FR-04: Computes SSS deduction using the official 2024 SSS Contribution Table.
     * Employee share is based on monthly salary brackets.
     *
     * @param grossMonthlySalary Employee's gross monthly salary
     * @return Employee SSS contribution amount
     */
    public double computeSSS(double grossMonthlySalary) {
        // Official SSS Contribution Table 2024 (Employee Share)
        if (grossMonthlySalary < 3250)       return 135.00;
        else if (grossMonthlySalary < 3750)  return 157.50;
        else if (grossMonthlySalary < 4250)  return 180.00;
        else if (grossMonthlySalary < 4750)  return 202.50;
        else if (grossMonthlySalary < 5250)  return 225.00;
        else if (grossMonthlySalary < 5750)  return 247.50;
        else if (grossMonthlySalary < 6250)  return 270.00;
        else if (grossMonthlySalary < 6750)  return 292.50;
        else if (grossMonthlySalary < 7250)  return 315.00;
        else if (grossMonthlySalary < 7750)  return 337.50;
        else if (grossMonthlySalary < 8250)  return 360.00;
        else if (grossMonthlySalary < 8750)  return 382.50;
        else if (grossMonthlySalary < 9250)  return 405.00;
        else if (grossMonthlySalary < 9750)  return 427.50;
        else if (grossMonthlySalary < 10250) return 450.00;
        else if (grossMonthlySalary < 10750) return 472.50;
        else if (grossMonthlySalary < 11250) return 495.00;
        else if (grossMonthlySalary < 11750) return 517.50;
        else if (grossMonthlySalary < 12250) return 540.00;
        else if (grossMonthlySalary < 12750) return 562.50;
        else if (grossMonthlySalary < 13250) return 585.00;
        else if (grossMonthlySalary < 13750) return 607.50;
        else if (grossMonthlySalary < 14250) return 630.00;
        else if (grossMonthlySalary < 14750) return 652.50;
        else if (grossMonthlySalary < 15250) return 675.00;
        else if (grossMonthlySalary < 15750) return 697.50;
        else if (grossMonthlySalary < 16250) return 720.00;
        else if (grossMonthlySalary < 16750) return 742.50;
        else if (grossMonthlySalary < 17250) return 765.00;
        else if (grossMonthlySalary < 17750) return 787.50;
        else if (grossMonthlySalary < 18250) return 810.00;
        else if (grossMonthlySalary < 18750) return 832.50;
        else if (grossMonthlySalary < 19250) return 855.00;
        else if (grossMonthlySalary < 19750) return 877.50;
        else if (grossMonthlySalary < 20250) return 900.00;
        else if (grossMonthlySalary < 20750) return 922.50;
        else if (grossMonthlySalary < 21250) return 945.00;
        else if (grossMonthlySalary < 21750) return 967.50;
        else if (grossMonthlySalary < 22250) return 990.00;
        else if (grossMonthlySalary < 22750) return 1012.50;
        else if (grossMonthlySalary < 23250) return 1035.00;
        else if (grossMonthlySalary < 23750) return 1057.50;
        else if (grossMonthlySalary < 24250) return 1080.00;
        else if (grossMonthlySalary < 24750) return 1102.50;
        else                                 return 1125.00; // max at 25,000+
    }

    /**
     * FR-05: Computes PhilHealth deduction.
     * Rate: 5% of basic monthly salary, employee share = 2.5%.
     * Minimum monthly salary base: PHP 10,000. Maximum: PHP 100,000.
     *
     * @param basicMonthlySalary Employee's basic monthly salary
     * @return Employee PhilHealth contribution
     */
    public double computePhilHealth(double basicMonthlySalary) {
        // PhilHealth 2024: 5% of monthly salary, employee pays half (2.5%)
        double salary = basicMonthlySalary;
        if (salary < 10000) salary = 10000;         // floor
        if (salary > 100000) salary = 100000;        // ceiling
        double employeeShare = salary * 0.025;
        return employeeShare;
    }

    /**
     * FR-06: Computes Pag-IBIG deduction.
     * Rate: 2% of monthly salary, capped at PHP 100.00 employee share.
     *
     * @param grossMonthlySalary Employee's gross monthly salary
     * @return Employee Pag-IBIG contribution
     */
    public double computePagIbig(double grossMonthlySalary) {
        double contribution = grossMonthlySalary * 0.02;
        // Cap at PHP 100.00 per month
        return Math.min(contribution, 100.0);
    }

    /**
     * FR-07: Computes withholding tax using BIR monthly tax table (TRAIN Law).
     * Taxable income = Gross Pay - SSS - PhilHealth - Pag-IBIG - Late Deductions.
     *
     * @param taxableIncome Gross pay minus non-taxable deductions
     * @return Monthly withholding tax amount
     */
    public double computeWithholdingTax(double taxableIncome) {
        // BIR Withholding Tax Table - Monthly (TRAIN Law, effective 2023)
        if (taxableIncome <= 20833) {
            // Tax exempt
            return 0.0;
        } else if (taxableIncome <= 33332) {
            // 20% of excess over 20,833
            return (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome <= 66666) {
            // PHP 2,500 + 25% of excess over 33,332
            return 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome <= 166666) {
            // PHP 10,833 + 30% of excess over 66,666
            return 10833 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome <= 666666) {
            // PHP 40,833.33 + 32% of excess over 166,666
            return 40833 + (taxableIncome - 166667) * 0.32;
        } else {
            // PHP 200,833.33 + 35% of excess over 666,666
            return 200833 + (taxableIncome - 666667) * 0.35;
        }
    }
}
