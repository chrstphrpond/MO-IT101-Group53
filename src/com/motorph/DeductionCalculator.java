package com.motorph;

import java.util.ArrayList;
import java.util.List;

/**
 * DeductionCalculator.java - Computes all statutory deductions.
 * Implements FR-04 (SSS), FR-05 (PhilHealth), FR-06 (Pag-IBIG), FR-07 (Withholding Tax).
 * Loads SSS and Tax lookup tables dynamically from CSVs.
 */
public class DeductionCalculator {

    // SSS Table: index 0 = maxSalary (exclusive boundary), index 1 = contribution
    private List<double[]> sssTable;
    
    // Tax Table: index 0 = minSalary (inclusive), index 1 = baseTax, index 2 = additionalRate, index 3 = excessOver
    private List<double[]> taxTable;

    public DeductionCalculator() {
        sssTable = new ArrayList<>();
        taxTable = new ArrayList<>();
        loadTables();
    }

    private void loadTables() {
        CSVReader reader = new CSVReader();
        
        // Load SSS
        List<String[]> sssData = reader.readCSV("data/sss.csv");
        for (String[] row : sssData) {
            if (row.length < 4) continue;
            try {
                String maxStr = row[2].trim().replace("\"", "").replace(",", "");
                double maxVal;
                
                if (maxStr.isEmpty() && row[0].contains("Below")) {
                    String[] parts = row[0].replace("\"", "").replace(",", "").split(" ");
                    maxVal = Double.parseDouble(parts[1]);
                } else if (maxStr.equalsIgnoreCase("Over")) {
                    maxVal = Double.MAX_VALUE;
                } else {
                    maxVal = Double.parseDouble(maxStr);
                }
                
                double contribution = Double.parseDouble(row[3].trim().replace("\"", "").replace(",", ""));
                sssTable.add(new double[]{maxVal, contribution});
            } catch (Exception e) {
                // Ignore parsing errors for malformed or header rows
            }
        }
        
        // Load Tax
        List<String[]> taxData = reader.readCSV("data/tax.csv");
        for (String[] row : taxData) {
            if (row.length < 2) continue;
            try {
                String rangeStr = row[0].toLowerCase().replace(",", "").replace("\"", "");
                String taxRuleStr = row[1].toLowerCase().replace(",", "").replace("\"", "");
                
                double minSalary = 0;
                double baseTax = 0;
                double additionalRate = 0;
                double excessOver = 0;
                
                if (rangeStr.contains("and below")) {
                    minSalary = 0;
                } else if (rangeStr.contains("to below")) {
                    minSalary = Double.parseDouble(rangeStr.split(" to ")[0].trim());
                } else if (rangeStr.contains("and above")) {
                    minSalary = Double.parseDouble(rangeStr.split(" and ")[0].trim());
                }
                
                if (taxRuleStr.contains("no withholding tax")) {
                    baseTax = 0; 
                    additionalRate = 0;
                    excessOver = 0;
                } else {
                    String[] parts = taxRuleStr.split("plus");
                    if (parts.length > 1) {
                        baseTax = Double.parseDouble(parts[0].trim());
                        String[] rateParts = parts[1].split("% in excess of");
                        additionalRate = Double.parseDouble(rateParts[0].trim()) / 100.0;
                        excessOver = Double.parseDouble(rateParts[1].trim());
                    } else {
                        String[] rateParts = taxRuleStr.split("% in excess of");
                        baseTax = 0;
                        additionalRate = Double.parseDouble(rateParts[0].trim()) / 100.0;
                        excessOver = Double.parseDouble(rateParts[1].trim());
                    }
                }
                taxTable.add(new double[]{minSalary, baseTax, additionalRate, excessOver});
            } catch (Exception e) {
                 // Ignore
            }
        }
        
        // Sort tax table descending by minSalary to evaluate top-down
        taxTable.sort((a, b) -> Double.compare(b[0], a[0]));
    }

    /**
     * FR-04: Computes SSS deduction using the loaded SSS Contribution Table.
     *
     * @param grossMonthlySalary Employee's gross monthly salary
     * @return Employee SSS contribution amount
     */
    public double computeSSS(double grossMonthlySalary) {
        if (sssTable.isEmpty()) {
            return 0; // Fallback if table failed to load
        }
        for (double[] bracket : sssTable) {
            if (grossMonthlySalary < bracket[0]) {
                return bracket[1];
            }
        }
        // If it exceeds the maximum mapped value
        return sssTable.get(sssTable.size() - 1)[1];
    }

    /**
     * FR-05: Computes PhilHealth deduction.
     * Rate: 5% of basic monthly salary, employee share = 2.5%.
     * Minimum monthly salary base: PHP 10,000. Maximum: PHP 100,000.
     * Note: Explicitly defined by system requirements (FR-05).
     *
     * @param basicMonthlySalary Employee's basic monthly salary
     * @return Employee PhilHealth contribution
     */
    public double computePhilHealth(double basicMonthlySalary) {
        double salary = Math.max(10000, Math.min(basicMonthlySalary, 100000));
        return salary * 0.025;
    }

    /**
     * FR-06: Computes Pag-IBIG deduction.
     * Rate: 2% of monthly salary, capped at PHP 100.00 employee share.
     * Note: Explicitly defined by system requirements (FR-06).
     *
     * @param grossMonthlySalary Employee's gross monthly salary
     * @return Employee Pag-IBIG contribution
     */
    public double computePagIbig(double grossMonthlySalary) {
        return Math.min(grossMonthlySalary * 0.02, 100.0);
    }

    /**
     * FR-07: Computes withholding tax using loaded BIR monthly tax table.
     * Taxable income = Gross Pay - SSS - PhilHealth - Pag-IBIG - Late Deductions.
     *
     * @param taxableIncome Gross pay minus non-taxable deductions
     * @return Monthly withholding tax amount
     */
    public double computeWithholdingTax(double taxableIncome) {
        if (taxTable.isEmpty()) {
            return 0;
        }
        
        for (double[] bracket : taxTable) {
            if (taxableIncome >= bracket[0]) {
                double baseTax = bracket[1];
                double additionalRate = bracket[2];
                double excessOver = bracket[3];
                
                double excess = Math.max(0, taxableIncome - excessOver);
                return baseTax + (excess * additionalRate);
            }
        }
        return 0;
    }
}
