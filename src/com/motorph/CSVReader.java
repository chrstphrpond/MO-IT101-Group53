package com.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVReader.java - Utility class for reading CSV data files.
 * Handles loading employee master data and attendance logs.
 */
public class CSVReader {

    /**
     * Reads all lines from a CSV file, skipping the header row.
     *
     * @param filePath Path to the CSV file (e.g., "data/employees.csv")
     * @return List of String arrays, each representing one CSV row
     */
    public List<String[]> readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header row
            br.readLine();

            while ((line = br.readLine()) != null) {
                // Split by comma, handle quoted fields
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                // Trim whitespace from each field
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim().replace("\"", "");
                }
                records.add(fields);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            System.err.println(e.getMessage());
        }

        return records;
    }

    /**
     * Loads all employees from the employee master CSV file.
     *
     * @param filePath Path to employees.csv
     * @return List of Employee objects
     */
    public List<Employee> loadEmployees(String filePath) {
        List<Employee> employees = new ArrayList<>();
        List<String[]> records = readCSV(filePath);

        for (String[] fields : records) {
            try {
                // TODO: Adjust field indexes to match actual MotorPH CSV column order
                String empNum    = fields[0];
                String lastName  = fields[1];
                String firstName = fields[2];
                String birthday  = fields[3];
                String sss       = fields[6];
                String philHealth= fields[7];
                String tin       = fields[8];
                String pagIbig   = fields[9];
                String status    = fields[10];
                String position  = fields[11];
                double salary    = Double.parseDouble(fields[13].replace(",", ""));

                employees.add(new Employee(empNum, lastName, firstName, birthday,
                        sss, philHealth, tin, pagIbig, status, position, salary));
            } catch (Exception e) {
                System.err.println("Skipping invalid record: " + String.join(",", fields));
            }
        }

        return employees;
    }
}
